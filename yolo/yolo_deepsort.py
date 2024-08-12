import torch
import cv2
from deep_sort_realtime.deepsort_tracker import DeepSort
from datetime import datetime

# YOLOv5 모델 로드
model = torch.hub.load('ultralytics/yolov5', 'yolov5s', pretrained=True)

# DeepSORT 초기화
deepsort = DeepSort()

# 객체 정보를 저장할 딕셔너리
object_info = {}

# 웹캠 열기
cap = cv2.VideoCapture(0)

while True:
    ret, frame = cap.read()
    if not ret:
        break

    # YOLOv5로 객체 감지
    results = model(frame)

    # YOLOv5 결과에서 bbox와 confidence 추출
    bbox_xywh = []
    confs = []
    clss = []

    for *xyxy, conf, cls in results.pred[0]:
        x1, y1, x2, y2 = xyxy
        x_c, y_c, bbox_w, bbox_h = (x1 + x2) / 2, (y1 + y2) / 2, x2 - x1, y2 - y1
        bbox_xywh.append([x_c, y_c, bbox_w, bbox_h])
        confs.append(conf.item())
        clss.append(int(cls.item()))

    # Convert lists to tensors
    bbox_xywh = torch.tensor(bbox_xywh, dtype=torch.float32)
    confs = torch.tensor(confs, dtype=torch.float32)

    # Ensure bbox_xywh is in the format [x_center, y_center, width, height]
    detections = [(bbox_xywh[i].tolist(), confs[i].item()) for i in range(len(bbox_xywh))]
    outputs = deepsort.update_tracks(detections, frame)


    # 객체 추적 정보 처리
    for track in outputs:
        # `track` 객체에서 바운딩 박스 좌표를 추출합니다
        bbox_left, bbox_top, bbox_right, bbox_bottom = track.to_tlbr()  # Use method to get bounding box in TLBR format
        identity = int(track.track_id)  # Convert track ID to integer
        
        # Ensure identity is a valid index
        obj_class = clss[identity] if 0 <= identity < len(clss) else 'unknown'

        # 객체의 처음 감지된 시점 기록
        if identity not in object_info:
            object_info[identity] = {
                'first_detected': datetime.now(),
                'last_detected': None,
                'class': obj_class,
            }

        # 객체의 마지막 감지된 시점 업데이트
        object_info[identity]['last_detected'] = datetime.now()

        # 화면에 바운딩 박스 그리기
        cv2.rectangle(frame, (int(bbox_left), int(bbox_top)), (int(bbox_right), int(bbox_bottom)), (0, 255, 0), 2)
        cv2.putText(frame, f'ID {identity}', (int(bbox_left), int(bbox_top) - 10), cv2.FONT_HERSHEY_SIMPLEX, 0.75, (0, 255, 0), 2)

    # 화면에 출력
    cv2.imshow('YOLOv5 + DeepSORT', frame)

    # 'q' 키를 누르면 종료
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

# 객체 정보 출력
for obj_id, info in object_info.items():
    first_detected = info['first_detected']
    last_detected = info['last_detected']
    obj_class = info['class']

    print(f"Object ID: {obj_id}")
    print(f"  - First Detected: {first_detected}")
    print(f"  - Last Detected: {last_detected}")
    print(f"  - Class: {obj_class}")

# 웹캠 및 창 닫기
cap.release()
cv2.destroyAllWindows()