from fastapi import FastAPI, Response
from huggingface_hub import login
from transformers import pipeline
import os
import json
import torch
import time
import logging

model_id = "spamfighters/bert-base-uncased-finetuned-spam-detection"
token = os.environ['HG_TOKEN']

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

# Check CUDA availability
if torch.cuda.is_available():
    device = 0
    gpu_name = torch.cuda.get_device_name(0)
    logger.info(f"CUDA is available. Using GPU: {gpu_name} (device=0).")
else:
    device = -1
    logger.info("CUDA is NOT available. Using CPU (device=-1).")

class ClassifierModel:
    def __init__(self, token:str, model_name: str, device: int = -1):
        login(token)
        self.classifier = pipeline("text-classification", model=model_name, device=device)

    # Returns a dictionary in following format:
    # {"spam": 0.1, "ham": 0.9}
    def predict(self, text: str):
        start_time = time.time()
        result = {}
        preds = self.classifier(text, top_k=None)
        for item in preds:
            label = item["label"]
            score = item["score"]
            if (label == HAM_LABEL):
                result["ham"] = score
    
            if (label == SPAM_LABEL):
                result["spam"] = score
        
        inference_time_ms = (time.time() - start_time) * 1000
        logger.info(f"Inference completed in {inference_time_ms:.2f} ms")
        return result

app = FastAPI()
classifier = ClassifierModel(token, model_id, device)

HAM_LABEL = "LABEL_0"
SPAM_LABEL = "LABEL_1"

@app.get("/check")
async def check(text: str):
    preds = classifier.predict(text)
    json_str = json.dumps(preds, indent=4, default=str)
    return Response(content=json_str, media_type='application/json')

    
