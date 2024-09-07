import spacy
from spacy.language import Language
from spacy.tokens import Doc
import pandas as pd
import re

# Load the XLSX file and create a translation dictionary
file_path = "FIL-CAM.xlsx"
df = pd.read_excel(file_path)
translations = {}

def normalize_text(text):
    # Remove punctuation and convert to lower case
    return re.sub(r'[^\w\s]', '', text.lower())

# Create translations mapping full phrases instead of words
for index, row in df.iterrows():
    if isinstance(row['Filipino'], str) and isinstance(row['Camarines Norte'], str):
        tagalog_phrase = normalize_text(row['Filipino'])
        bikol_phrase = normalize_text(row['Camarines Norte'])
        translations[tagalog_phrase] = bikol_phrase
        translations[bikol_phrase] = tagalog_phrase

@Language.component("custom_translator")
def custom_translator(doc):
    translated_text = []
    tokens = [token.text for token in doc]
    i = 0
    while i < len(tokens):
        # Try to find the longest possible match in the dictionary
        for j in range(len(tokens), i, -1):
            phrase = normalize_text(" ".join(tokens[i:j]))
            if phrase in translations:
                translated_text.append(translations[phrase])
                i = j - 1
                break
        else:
            # If no match is found, keep the original token
            translated_text.append(tokens[i])
        i += 1
    new_doc = Doc(doc.vocab, words=translated_text, spaces=[True] * len(translated_text))
    return new_doc
