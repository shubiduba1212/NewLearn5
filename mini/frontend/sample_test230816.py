# main.py
from fastapi import FastAPI
from pydantic import BaseModel
from typing import List, Dict, Any
from fastapi.middleware.cors import CORSMiddleware  # CORSMiddleware 임포트

app = FastAPI()

# Pydantic 모델 정의
class LabelList(BaseModel):
    label_list: List[str]

# CORS 미들웨어 추가
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],  # 허용할 출처를 지정할 수 있습니다. "*"는 모든 출처를 허용합니다.
    allow_credentials=True,
    allow_methods=["*"],  # 허용할 HTTP 메소드
    allow_headers=["*"],  # 허용할 HTTP 헤더
)

@app.post("/test")
def read_root():
    result = {
        "label_list": ["obj1", "obj2", "obj3"],
        "det_result": {}
    }
    return result
    # return {"Hello": "World"}

@app.post("/update_labels/")
async def update_labels(label_data: LabelList):
    # 클라이언트에서 전송된 데이터 조회
    label_list = label_data.label_list
    print(f"Received labels: {label_list}")

    # 데이터 처리 (여기서는 단순히 반환)
    result_list = process_labels(label_list)

    return {"result_list": result_list}

def process_labels(label_list: List[str]) -> List[Dict[str, Any]]:
    # 여기서는 단순히 더미 데이터를 반환합니다.
    # 실제 데이터 처리 로직을 작성하여 필요한 결과를 반환합니다.
    result_list = [
        {"label": label, "score": 0.9, "box": {"xmin": 10, "ymin": 20, "xmax": 50, "ymax": 60}}
        for label in label_list
    ]
    return result_list
