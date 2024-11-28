from fastapi import FastAPI, Response
from huggingface_hub import login
from transformers import pipeline
import os
import json

model_id = "spamfighters/bert-base-uncased-finetuned-spam-detection"
token = os.environ['HG_TOKEN']

class ClassifierModel:
    def __init__(self, token:str, model_name: str):
        login(token)
        self.classifier = pipeline("text-classification", model=model_name)

    # Returns a dictionary in following format:
    # {"spam": 0.1, "ham": 0.9}
    def predict(self, text: str):
        result = {}
        preds = self.classifier(text, top_k=None)
        for item in preds:
            label = item["label"]
            score = item["score"]
            if (label == HAM_LABEL):
                result["ham"] = score
    
            if (label == SPAM_LABEL):
                result["spam"] = score
        return result

app = FastAPI()
classifier = ClassifierModel(token, model_id)

HAM_LABEL = "LABEL_0"
SPAM_LABEL = "LABEL_1"

@app.get("/check")
async def check(text: str):
    preds = classifier.predict(text)
    json_str = json.dumps(preds, indent=4, default=str)
    return Response(content=json_str, media_type='application/json')

    
