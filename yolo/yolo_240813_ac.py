import cv2
import torch
from yolov5 import YOLOv5
from datetime import datetime

# YOLOv5 모델 로드
model_path = 'yolov5s.pt'
model = YOLOv5(model_path)

# 동영상 로드
video_path = "video/street.mp4"
cap = cv2.VideoCapture(video_path)

if not cap.isOpened():
    print("Error: Could not open video.")
    exit()

fps = cap.get(cv2.CAP_PROP_FPS)
if fps == 0:
    fps = 30
frame_time = int(1000 / fps)

# 객체의 첫 등장 및 사라진 시간을 저장할 딕셔너리
object_timelines = {}
object_last_seen = {}

# 객체 ID 관리
next_object_id = 1
object_id_map = {}
object_positions = {}

# 사용자가 인식하려는 객체의 라벨 입력
target_label = input("Enter the label of the object you want to track (e.g., 'person', 'car'): ")

def calculate_iou(box1, box2):
    x1 = max(box1[0], box2[0])
    y1 = max(box1[1], box2[1])
    x2 = min(box1[2], box2[2])
    y2 = min(box1[3], box2[3])

    intersection = max(0, x2 - x1) * max(0, y2 - y1)
    area1 = (box1[2] - box1[0]) * (box1[3] - box1[1])
    area2 = (box2[2] - box2[0]) * (box2[3] - box2[1])
    union = area1 + area2 - intersection

    return intersection / union if union > 0 else 0

def assign_object_id(label, bbox):
    global next_object_id, object_positions
    x1, y1, x2, y2 = bbox

    max_iou = 0
    best_match_key = None
    for key, pos in object_positions.items():
        iou = calculate_iou(bbox, pos)
        if iou > max_iou:
            max_iou = iou
            best_match_key = key

    iou_threshold = 0.5

    if max_iou > iou_threshold:
        return int(best_match_key.split('_')[-1])
    else:
        new_id = next_object_id
        next_object_id += 1
        new_key = f"{label}_{new_id}"
        object_positions[new_key] = bbox
        return new_id
    
def draw_bounding_boxes(frame):
    for key, bbox in object_positions.items():
        label, obj_id = key.split('_')
        x1, y1, x2, y2 = map(int, bbox)
        cv2.rectangle(frame, (x1, y1), (x2, y2), (255, 0, 0), 2)
        cv2.putText(frame, f"{label} (ID: {obj_id})", (x1, y1 - 10),
                    cv2.FONT_HERSHEY_SIMPLEX, 0.5, (255, 0, 0), 2)
    return frame

def process_frame(frame):
    rgb_frame = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
    results = model.predict(rgb_frame)
    predictions = results.pandas().xyxy[0]
    current_time = datetime.now().strftime('%Y/%m/%d(%H:%M:%S)')

    current_frame_objects = []

    for _, row in predictions.iterrows():
        label_name = row['name']
        
        # 타겟 라벨과 일치하는 객체만 처리
        if label_name != target_label:
            continue
        
        bbox = [row['xmin'], row['ymin'], row['xmax'], row['ymax']]
        obj_id = assign_object_id(label_name, bbox)
        obj_key = f"{label_name}_{obj_id}"

        is_new_object = obj_key not in object_timelines
        if is_new_object:
            object_timelines[obj_key] = {"label": label_name, "first_seen": current_time, "last_seen": current_time}
            print(f"New {label_name} (ID: {obj_id}) appeared at {current_time}")
            
            # 새로운 객체에 대해서만 바운딩 박스 그리기
            x1, y1, x2, y2 = int(row['xmin']), int(row['ymin']), int(row['xmax']), int(row['ymax'])
            cv2.rectangle(frame, (x1, y1), (x2, y2), (255, 0, 0), 2)
            cv2.putText(frame, f"{label_name} (ID: {obj_id}): {round(row['confidence'], 3)}", (x1, y1 - 10),
                        cv2.FONT_HERSHEY_SIMPLEX, 0.5, (255, 0, 0), 2)

        # 현재 프레임에서 탐지된 객체 목록에 추가
        current_frame_objects.append(obj_key)

        # 객체 위치 업데이트
        object_positions[obj_key] = bbox

    for key in list(object_last_seen.keys()):
        if key not in current_frame_objects:
            last_seen_time = object_timelines[key]["last_seen"]
            label, obj_id = key.split('_')
            print(f"{label} (ID: {obj_id}) disappeared at {last_seen_time}")
            del object_last_seen[key]
            if key in object_positions:
                del object_positions[key]

    for key in current_frame_objects:
        object_last_seen[key] = current_time

    return frame

frame_idx = 0
detect_interval = 10
last_detected_frame = -1

while True:
    ret, frame = cap.read()
    if not ret:
        break

    frame_idx += 1

    if frame_idx % detect_interval == 0:
        frame = process_frame(frame)
    
    # 매 프레임 바운딩 박스 그리기
    frame = draw_bounding_boxes(frame)

    cv2.imshow('Frame', frame)
    key = cv2.waitKey(int(frame_time))
    if key & 0xFF == ord('q'):
        break

cap.release()
cv2.destroyAllWindows()

print("\nTimeline of objects:")
for key, times in object_timelines.items():
    label, obj_id = key.split('_')
    print(f"{label} (ID: {obj_id}): First seen at {times['first_seen']}, Last seen at {times['last_seen']}")
