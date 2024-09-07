import spacy
from FIL_CAM_custom_component import custom_translator  # Import your custom component

nlp = spacy.blank("xx")  # Create a blank multi-language model
nlp.add_pipe("custom_translator")  # Add your custom component to the pipeline
nlp.to_disk("FIL-CAM_translator.model")  # Save the model
