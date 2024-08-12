import cv2
from ultralytics import YOLO

# YOLOv8 모델 불러오기
model = YOLO("yolov8s.pt")

# 동영상 파일 열기
cap = cv2.VideoCapture('video/sample.mp4')

while cap.isOpened():
    ret, frame = cap.read()
    if not ret:
        break
    
    # YOLO 모델로 객체 탐지
    results = model(frame)
    
    # 탐지 결과 프레임에 표시
    for result in results:
        for box in result.boxes:
            x1, y1, x2, y2 = map(int, box.xyxy[0].tolist())
            conf = box.conf[0].item()
            class_id = int(box.cls[0].item())
            print("class_id : {0}".format(class_id))
            
            # 바운딩 박스 그리기
            class_name = result.names[class_id] if hasattr(result, 'names') else str(class_id)
            print("result.names : {0}".format(result))
            print("result.names[class_id] : {0}".format(result.names[class_id]))
            print("class_name : {0}".format(class_name))
            cv2.rectangle(frame, (x1, y1), (x2, y2), (255, 0, 0), 2)
            cv2.putText(frame, f"{class_name} {conf:.2f}", (x1, y1 - 10),
                        cv2.FONT_HERSHEY_SIMPLEX, 0.5, (255, 0, 0), 2)
    
    # 결과 출력
    cv2.imshow('YOLOv8 Detection', frame)
    
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

cap.release()
cv2.destroyAllWindows()
