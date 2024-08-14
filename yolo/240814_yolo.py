# 1. 임포트
from ultralytics import YOLO
import cv2
from PIL import Image
import numpy as np
from collections import defaultdict
import math

# 2. 추론기 생성
yolo_detector = YOLO('yolov8n.pt')

# 3. 파일 가져오기
video_path = 'video/bus.mp4'
cap = cv2.VideoCapture(video_path)

frame_list = []
frame_count = 0
frame_skip = 1
while True:
    ret, frame = cap.read()
    if not ret:
        break
    if frame_count % frame_skip == 0:
        frame_rgb = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
        pil_image = Image.fromarray(frame_rgb)
        frame_list.append(pil_image)
    frame_count += 1

cap.release()

# 4. 추론
result_list = []
for frame in frame_list:
    frame_np = np.array(frame)
    results = yolo_detector(frame_np)
    detections = []
    for result in results:
        boxes = result.boxes.xyxy
        scores = result.boxes.conf
        classes = result.boxes.cls
        
        detections = []
        for box, score, cls in zip(boxes, scores, classes):
            x1, y1, x2, y2 = box
            label = yolo_detector.model.names[int(cls)]
            detections.append({
                'label': label,
                'score': float(score),
                'box': {'xmin': x1.item(), 'ymin': y1.item(), 'xmax': x2.item(), 'ymax': y2.item()}
            })
        result_list.append(detections)

# 5. 활용
label_set = set()
for results in result_list:
    for result in results:
        label_set.add(result['label'])

message = '현재 감지된 객체 리스트'
for label in label_set:
    message += f'| {label}'
print("message : {0}".format(message))

input_label = ''
while True:
    input_label = input('제외할 객체를 입력해주세요. (종료는 q 입력)')
    if input_label == 'q':
        break
    if input_label in label_set:
        label_set.remove(input_label)
    message = ''
    for label in label_set:
        message += f'| {label}'
    print('남은 객체' + message)

# 비디오 속성 가져오기
cap = cv2.VideoCapture(video_path)
fps = cap.get(cv2.CAP_PROP_FPS)

# 객체 고유 ID 관리
next_object_id = 1
object_timeline = defaultdict(lambda: {'start': None, 'end': None})
active_objects = {}  # 현재 화면에 존재하는 객체들 {id: box}

def calculate_iou(box1, box2):
    """ 두 박스 간의 IoU 계산 """
    x1, y1, x2, y2 = box1['xmin'], box1['ymin'], box1['xmax'], box1['ymax']
    x1_p, y1_p, x2_p, y2_p = box2['xmin'], box2['ymin'], box2['xmax'], box2['ymax']

    inter_x1 = max(x1, x1_p)
    inter_y1 = max(y1, y1_p)
    inter_x2 = min(x2, x2_p)
    inter_y2 = min(y2, y2_p)
    inter_area = max(0, inter_x2 - inter_x1) * max(0, inter_y2 - inter_y1)

    box1_area = (x2 - x1) * (y2 - y1)
    box2_area = (x2_p - x1_p) * (y2_p - y1_p)

    iou = inter_area / float(box1_area + box2_area - inter_area)
    return iou

iou_threshold = 0.5  # IoU threshold for matching objects

frame = 1
for results in result_list:
    current_frame_objects = {}
    for result in results:
        if result['label'] in label_set:
            box = result['box']
            matched = False
            
            for obj_id, obj_box in active_objects.items():
                iou = calculate_iou(obj_box, box)
                if iou > iou_threshold:
                    current_frame_objects[obj_id] = box
                    object_timeline[obj_id]['end'] = frame / fps
                    matched = True
                    break

            if not matched:
                # 새로운 객체로 간주
                current_frame_objects[next_object_id] = box
                object_timeline[next_object_id] = {
                    'label': result['label'],
                    'start': frame / fps,
                    'end': frame / fps
                }
                next_object_id += 1

    active_objects = current_frame_objects
    frame += 1

# 등장 시간과 사라진 시간 출력
for obj_id, times in object_timeline.items():
    if times['start'] is not None:
        print(f"객체 ID: {obj_id} ({times['label']}) 등장 시작: {times['start']:.2f}초 | 등장 종료: {times['end']:.2f}초")

cap.release()
out.release()
