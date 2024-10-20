import os
import tensorflow as tf
from tensorflow.keras.layers import Input, LSTM, Embedding, Dense
from tensorflow.keras.models import Model
from tensorflow.keras.preprocessing.text import Tokenizer
from tensorflow.keras.preprocessing.sequence import pad_sequences
import numpy as np
import pandas as pd

def read_sentence_pairs(file_path):
    sentence_pairs = []
    if not os.path.exists(file_path):
        print(f"File not found: {file_path}")
        return sentence_pairs
    df = pd.read_excel(file_path)
    for index, row in df.iterrows():
        src, tgt = row[0], row[1]
        if pd.notna(src) and pd.notna(tgt):
            sentence_pairs.append((src, tgt))
        else:
            print(f"Skipping row with NaN values: {row}")
    return sentence_pairs

file_path = 'fil-manito/Manito.xlsx'
data = read_sentence_pairs(file_path)

print("Data:", data)

if not data:
    print("No data found. Please check the file and its format.")
else:
    input_texts = [pair[0] for pair in data]
    target_texts = ['start ' + pair[1] + ' end' for pair in data]

    print("Input Texts:", input_texts)
    print("Target Texts:", target_texts)

    tokenizer_input = Tokenizer(filters='')
    tokenizer_output = Tokenizer(filters='', oov_token='<OOV>')

    tokenizer_input.fit_on_texts(input_texts)
    tokenizer_output.fit_on_texts(target_texts)

    input_sequences = tokenizer_input.texts_to_sequences(input_texts)
    target_sequences = tokenizer_output.texts_to_sequences(target_texts)

    # Debug
    print("Input Sequences:", input_sequences)
    print("Target Sequences:", target_sequences)

    input_word_index = tokenizer_input.word_index
    target_word_index = tokenizer_output.word_index

    max_encoder_seq_length = max([len(seq) for seq in input_sequences])
    max_decoder_seq_length = max([len(seq) for seq in target_sequences])

    input_sequences = pad_sequences(input_sequences, maxlen=max_encoder_seq_length, padding='post')
    target_sequences = pad_sequences(target_sequences, maxlen=max_decoder_seq_length, padding='post')

    ## Build the model ##
    latent_dim = 256

    # Encoder
    encoder_inputs = Input(shape=(None,))
    encoder_embedding = Embedding(len(input_word_index) + 1, latent_dim)(encoder_inputs)
    encoder_lstm = LSTM(latent_dim, return_state=True)
    encoder_outputs, state_h, state_c = encoder_lstm(encoder_embedding)
    encoder_states = [state_h, state_c]

    # Decoder
    decoder_inputs = Input(shape=(None,))
    decoder_embedding = Embedding(len(target_word_index) + 1, latent_dim)(decoder_inputs)
    decoder_lstm = LSTM(latent_dim, return_sequences=True, return_state=True)
    decoder_outputs, _, _ = decoder_lstm(decoder_embedding, initial_state=encoder_states)
    decoder_dense = Dense(len(target_word_index) + 1, activation='softmax')
    decoder_outputs = decoder_dense(decoder_outputs)

    model = Model([encoder_inputs, decoder_inputs], decoder_outputs)

    # Compile the model
    model.compile(optimizer='rmsprop', loss='sparse_categorical_crossentropy')

    target_sequences_input = target_sequences[:, :-1]
    target_sequences_output = target_sequences[:, 1:]

    ## Reshape target data to match the model output
    target_sequences_output = np.expand_dims(target_sequences_output, -1)

    # Train the model
    model.fit([input_sequences, target_sequences_input], target_sequences_output, batch_size=64, epochs=100,
              validation_split=0.2)

    model_save_path = 'fil-manito/tagalog_daraga_translation_model.keras'
    model.save(model_save_path)

    encoder_model = Model(encoder_inputs, encoder_states)

    # Define the decoder model
    decoder_state_input_h = Input(shape=(latent_dim,))
    decoder_state_input_c = Input(shape=(latent_dim,))
    decoder_states_inputs = [decoder_state_input_h, decoder_state_input_c]
    decoder_outputs, state_h, state_c = decoder_lstm(
        decoder_embedding, initial_state=decoder_states_inputs)
    decoder_states = [state_h, state_c]
    decoder_outputs = decoder_dense(decoder_outputs)
    decoder_model = Model(
        [decoder_inputs] + decoder_states_inputs,
        [decoder_outputs] + decoder_states)

    def translate_sentence(sentence, tokenizer_input, tokenizer_output, encoder_model, decoder_model,
                           max_encoder_seq_length, max_decoder_seq_length, target_word_index,
                           reverse_target_word_index):
        input_seq = tokenizer_input.texts_to_sequences([sentence])
        input_seq = pad_sequences(input_seq, maxlen=max_encoder_seq_length, padding='post')

        states_value = encoder_model.predict(input_seq)

        target_seq = np.zeros((1, 1))
        target_seq[0, 0] = target_word_index['start']

        stop_condition = False
        decoded_sentence = ''
        while not stop_condition:
            output_tokens, h, c = decoder_model.predict([target_seq] + states_value)

            sampled_token_index = np.argmax(output_tokens[0, -1, :])
            sampled_token = reverse_target_word_index[sampled_token_index]
            if sampled_token == 'end':
                stop_condition = True
            else:
                decoded_sentence += ' ' + sampled_token

            target_seq = np.zeros((1, 1))
            target_seq[0, 0] = sampled_token_index

            states_value = [h, c]

        return decoded_sentence.strip()

    reverse_target_word_index = dict((i, word) for word, i in target_word_index.items())

    # Example usage with your test sentences
    test_sentences = [
        "Ang tamang-tama ng lasa, sulit!",
        "Pinangat na Isda ang paboritong ulam ni kuya tuwing lunch."
    ]

    for test_sentence in test_sentences:
        translated_sentence = translate_sentence(
            test_sentence,
            tokenizer_input,
            tokenizer_output,
            encoder_model,
            decoder_model,
            max_encoder_seq_length,
            max_decoder_seq_length,
            target_word_index,
            reverse_target_word_index
        )
        print('Original sentence:', test_sentence)
        print('Translated sentence:', translated_sentence)
