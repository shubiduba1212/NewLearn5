import cv2
import torch
from transformers import DetrImageProcessor, DetrForObjectDetection

# DETR 모델 및 프로세서 로드
model = DetrForObjectDetection.from_pretrained("facebook/detr-resnet-50")
processor = DetrImageProcessor.from_pretrained("facebook/detr-resnet-50")

# 웹캠 열기
cap = cv2.VideoCapture(0)

while True:
    ret, frame = cap.read()
    if not ret:
        break

    # 이미지 전처리
    inputs = processor(images=frame, return_tensors="pt")
    
    # 모델 예측
    with torch.no_grad():
        outputs = model(**inputs)
    
    # 후처리 및 바운딩 박스 추출
    target_sizes = torch.tensor([frame.shape[:2]])
    results = processor.post_process_object_detection(outputs, target_sizes=target_sizes, threshold=0.9)[0]
    
    # 바운딩 박스 그리기
    for score, label, box in zip(results["scores"], results["labels"], results["boxes"]):
        box = [int(i) for i in box.tolist()]
        cv2.rectangle(frame, (box[0], box[1]), (box[2], box[3]), color=(0, 255, 0), thickness=2)
        cv2.putText(frame, f'{model.config.id2label[label.item()]} {score.item():.2f}', (box[0], box[1] - 10), cv2.FONT_HERSHEY_SIMPLEX, 0.5, (0, 255, 0), 2)
    
    # 화면에 출력
    cv2.imshow('DETR Object Detection', frame)

    # 'q' 키를 누르면 종료
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

# 웹캠 및 창 닫기
cap.release()
cv2.destroyAllWindows()


# import cv2
# import torch
# from transformers import DetrImageProcessor, DetrForObjectDetection
# from collections import deque

# # DETR 모델 및 프로세서 로드
# model = DetrForObjectDetection.from_pretrained("facebook/detr-resnet-50")
# processor = DetrImageProcessor.from_pretrained("facebook/detr-resnet-50")

# # 웹캠 열기
# cap = cv2.VideoCapture(0)

# # 추적기 객체 리스트
# trackers = []
# tracker_id = 0

# # 객체의 위치를 추적하기 위한 사전
# tracked_objects = {}

# # 최근 프레임을 저장하기 위한 deque (최대 10프레임)
# recent_frames = deque(maxlen=10)

# def is_box_valid(x, y, w, h, frame_shape):
#     """ Check if the bounding box is within the frame boundaries. """
#     h_max, w_max = frame_shape[:2]
#     if x < 0 or y < 0 or x + w > w_max or y + h > h_max:
#         return False
#     return True

# while True:
#     ret, frame = cap.read()
#     if not ret:
#         break

#     # 이미지 전처리
#     inputs = processor(images=frame, return_tensors="pt")

#     # 모델 예측
#     with torch.no_grad():
#         outputs = model(**inputs)

#     # 후처리 및 바운딩 박스 추출
#     target_sizes = torch.tensor([frame.shape[:2]])
#     results = processor.post_process_object_detection(outputs, target_sizes=target_sizes, threshold=0.9)[0]

#     # 현재 감지된 객체들
#     detected_objects = []

#     for score, label, box in zip(results["scores"], results["labels"], results["boxes"]):
#         box = [int(i) for i in box.tolist()]
#         detected_objects.append((box, label.item()))

#     # 최근 프레임 저장
#     recent_frames.append(frame)

#     # 기존 추적기 업데이트
#     for tracker in trackers:
#         success, box = tracker.update(frame)
#         if success:
#             x, y, w, h = [int(i) for i in box]
#             # 바운딩 박스 그리기
#             cv2.rectangle(frame, (x, y), (x + w, y + h), color=(0, 255, 0), thickness=2)
#             cv2.putText(frame, f'Track ID {tracker_id}', (x, y - 10), cv2.FONT_HERSHEY_SIMPLEX, 0.5, (0, 255, 0), 2)
#         else:
#             # 추적 실패 시 추적기 제거
#             trackers.remove(tracker)

#     # 새로운 객체 추적기 생성
#     for box, label in detected_objects:
#         x, y, w, h = box
#         if is_box_valid(x, y, w, h, frame.shape):
#             # 추적기 생성 및 초기화
#             tracker = cv2.TrackerCSRT_create()
#             tracker.init(frame, (x, y, w, h))
#             trackers.append(tracker)
#             tracked_objects[tracker_id] = box
#             tracker_id += 1

#     # 바운딩 박스 그리기 (새로 감지된 객체)
#     for box, label in detected_objects:
#         x, y, w, h = box
#         if is_box_valid(x, y, w, h, frame.shape):
#             # 바운딩 박스 그리기
#             cv2.rectangle(frame, (x, y), (x + w, y + h), color=(255, 0, 0), thickness=2)
#             cv2.putText(frame, f'Label {label}', (x, y - 10), cv2.FONT_HERSHEY_SIMPLEX, 0.5, (255, 0, 0), 2)

#     # 화면에 출력
#     cv2.imshow('Object Tracking', frame)

#     # 'q' 키를 누르면 종료
#     if cv2.waitKey(1) & 0xFF == ord('q'):
#         break

# # 웹캠 및 창 닫기
# cap.release()
# cv2.destroyAllWindows()
