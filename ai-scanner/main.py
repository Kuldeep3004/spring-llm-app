from fastapi import FastAPI
from pydantic import BaseModel
import torch
from transformers import AutoTokenizer, AutoModelForSequenceClassification
from peft import PeftModel

app = FastAPI(title="Vulnerability Detection API")

ADAPTER_297 = r"C:\Users\lahar\Downloads\zimbra-model-package\zimbra-model-package\checkpoint-297"
CHECKPOINT_800 = r"C:\Users\lahar\Downloads\zimbra-model-package\zimbra-model-package\checkpoint-800"
BASE_MODEL = "deepseek-ai/deepseek-coder-1.3b-instruct"

id2label = {
    0: "SQL Injection",
    1: "Cross-Site Scripting (XSS)",
    2: "Insecure Deserialization",
    3: "Command Injection",
    4: "Path Traversal"
}

print("Loading base model...")
tokenizer = AutoTokenizer.from_pretrained(ADAPTER_297, trust_remote_code=True)
if tokenizer.pad_token is None:
    tokenizer.pad_token = tokenizer.eos_token

base_model = AutoModelForSequenceClassification.from_pretrained(
    BASE_MODEL,
    num_labels=6,
    trust_remote_code=True,
    device_map="cpu",
    low_cpu_mem_usage=True
)

print("Loading checkpoint-800...")
model = PeftModel.from_pretrained(base_model, CHECKPOINT_800)

print("Loading checkpoint-297 (Zimbra)...")
model = PeftModel.from_pretrained(model, ADAPTER_297, adapter_name="zimbra")
model.set_adapter("zimbra")
model.eval()
print("Model ready!")

class CodeRequest(BaseModel):
    code: str

@app.get("/health")
def health():
    return {"status": "UP", "service": "vulnerability-detection"}

@app.post("/scan")
def scan_code(request: CodeRequest):
    inputs = tokenizer(
        request.code,
        truncation=True,
        max_length=1024,
        return_tensors="pt"
    )
    with torch.no_grad():
        outputs = model(**inputs)
        probs = torch.nn.functional.softmax(outputs.logits, dim=-1)
        pred_class = torch.argmax(probs, dim=-1).item()
        confidence = probs[0][pred_class].item()

    return {
        "vulnerability": id2label.get(pred_class, "Unknown"),
        "confidence": round(confidence * 100, 2),
        "is_vulnerable": confidence > 0.6
    }