from ultralyticsplus import YOLO, render_result

# 모델 로드
model = YOLO('ultralyticsplus/yolov8s')

# 모델 파라미터 설정
model.overrides['conf'] = 0.25  # NMS confidence threshold
model.overrides['iou'] = 0.45  # NMS IoU threshold
model.overrides['agnostic_nms'] = False  # NMS class-agnostic
model.overrides['max_det'] = 1000  # maximum number of detections per image

# 이미지 설정
image = 't1_output.jpg'

# 추론 수행
results = model.predict(image)

# 탐지 결과 텍스트로 출력
for result in results:
    for box in result.boxes:
        x1, y1, x2, y2 = map(int, box.xyxy[0].tolist())
        conf = box.conf[0].item()
        class_id = int(box.cls[0].item())
        
        # 클래스 이름과 신뢰도 추출
        class_name = model.names[class_id] if hasattr(model, 'names') else str(class_id)
        print(f"Detected object: {class_name}, Confidence: {conf:.2f}")
        print(f"Bounding box coordinates: ({x1}, {y1}), ({x2}, {y2})")

# 결과 시각화
render = render_result(model=model, image=image, result=results[0])
render.show()
