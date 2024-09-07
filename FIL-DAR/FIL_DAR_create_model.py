import spacy
from FIL_DAR_custom_component import custom_translator
# from custom_component import custom_translator  # Import your custom component

nlp = spacy.blank("xx")  # Create a blank multi-language model
nlp.add_pipe("custom_translator")  # Add your custom component to the pipeline
nlp.to_disk("FIL-DAR_translator.model")  # Save the model
