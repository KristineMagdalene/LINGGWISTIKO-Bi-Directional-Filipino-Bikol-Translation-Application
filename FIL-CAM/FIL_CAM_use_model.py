import spacy
import pandas as pd
from FIL_CAM_custom_component import custom_translator  # Ensure the component is registered
from FIL_CAM_custom_component import normalize_text  # Import normalize_text function


# Load the translations from the XLSX file
file_path = "FIL-CAM.xlsx"
df = pd.read_excel(file_path)
translations = {}


# Create translations mapping full phrases instead of words
for index, row in df.iterrows():
    if isinstance(row['Filipino'], str) and isinstance(row['Camarines Norte'], str):
        tagalog_phrase = normalize_text(row['Filipino'])
        bikol_phrase = normalize_text(row['Camarines Norte'])
        translations[tagalog_phrase] = bikol_phrase
        translations[bikol_phrase] = tagalog_phrase



def translate_sentence(sentence):
    nlp_loaded = spacy.load("FIL-CAM_translator.model")  # Load the model
    doc = nlp_loaded(sentence)
    translated_text = ' '.join([token.text for token in doc])

    # Split into sentences, capitalize first letter, and rejoin
    sentences = translated_text.split('. ')
    capitalized_sentences = [s.capitalize() for s in sentences]
    translated_sentence = '. '.join(capitalized_sentences)

    # Track non-existent words
    non_existent_words = []
    tokens = sentence.split()
    for token in tokens:
        normalized_token = normalize_text(token)
        if normalized_token not in translations:
            non_existent_words.append(token)  # Append the original token, not the normalized one

    return translated_sentence, non_existent_words


def main():
    while True:
        sentence = input("Enter a sentence to translate (type 'exit' to quit): ")
        if sentence.lower() == 'exit':
            break
        translated, non_existent_words = translate_sentence(sentence)
        print("Translated:", translated)

        if non_existent_words:
            print("Non-existent words/phrases in the translation dictionary:")
            for word in non_existent_words:
                print(word)


if __name__ == "__main__":
    main()

