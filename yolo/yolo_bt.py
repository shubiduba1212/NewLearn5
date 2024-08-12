import cv2
import numpy as np
import torch
from sort.sort import Sort  # SORT 모듈에서 import
from datetime import datetime

# YOLOv5 모델 로드
model = torch.hub.load('ultralytics/yolov5', 'yolov5s', pretrained=True)  # 'yolov5s'는 작은 모델

# 웹캠 열기
cap = cv2.VideoCapture(0)

# 배경 차분기법 초기화
fgbg = cv2.createBackgroundSubtractorMOG2()

# SORT 초기화
tracker = Sort()

# 객체 상태를 저장할 딕셔너리
object_states = {}  # Format: {object_id: {"start_time": datetime, "end_time": datetime, "class_label": label}}

while True:
    ret, frame = cap.read()
    if not ret:
        break

    # 배경 차분을 사용하여 움직임 감지
    fgmask = fgbg.apply(frame)

    # 감지된 움직임을 이진 이미지로 변환
    _, thresh = cv2.threshold(fgmask, 200, 255, cv2.THRESH_BINARY)

    # 컨투어를 찾아서 움직임이 있는 영역 추출
    contours, _ = cv2.findContours(thresh, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)

    # YOLOv5로 객체 감지할 영역을 설정
    detection_boxes = []

    for contour in contours:
        x, y, w, h = cv2.boundingRect(contour)
        if cv2.contourArea(contour) > 500:  # 최소 면적을 설정하여 작은 움직임 제외
            # ROI(관심 영역)를 잘라서 YOLOv5로 감지
            roi = frame[y:y+h, x:x+w]
            rgb_roi = cv2.cvtColor(roi, cv2.COLOR_BGR2RGB)
            results = model(rgb_roi)
            detections = results.xyxy[0].cpu().numpy()  # [x1, y1, x2, y2, conf, class]
            
            # 감지 결과를 전체 이미지 좌표로 변환
            for detection in detections:
                x1, y1, x2, y2, conf, cls = detection
                x1 += x
                x2 += x
                y1 += y
                y2 += y
                detection_boxes.append([x1, y1, x2, y2, conf])
    
    # detection_boxes가 비어 있지 않은 경우에만 SORT로 객체 추적
    if len(detection_boxes) > 0:
        tracked_objects = tracker.update(np.array(detection_boxes))

        # 객체 추적 정보 처리
        for track in tracked_objects:
            bbox_left, bbox_top, bbox_right, bbox_bottom, identity = track[:5]
            bbox_left, bbox_top, bbox_right, bbox_bottom = map(int, [bbox_left, bbox_top, bbox_right, bbox_bottom])

            # 객체가 처음 포착된 시간 기록
            if identity not in object_states:
                object_states[identity] = {"start_time": datetime.now(), "end_time": None, "class_label": model.names[int(detection[5])]}
                print("움직임 포착 시작 시간 : {0}".format(object_states[identity]))
            else:
                # 객체가 사라진 시간 기록
                object_states[identity]["end_time"] = datetime.now()
                print("움직임 포착 종료 시간 : {0}".format(object_states[identity]["end_time"]))

            # 바운딩 박스와 ID 표시
            cv2.rectangle(frame, (bbox_left, bbox_top), (bbox_right, bbox_bottom), (0, 255, 0), 2)
            cv2.putText(frame, f'ID {int(identity)}', (bbox_left, bbox_top - 10), cv2.FONT_HERSHEY_SIMPLEX, 0.75, (0, 255, 0), 2)

            # 객체 레이블 표시 (YOLOv5의 클래스 레이블을 사용)
            class_label = model.names[int(detection[5])]
            cv2.putText(frame, class_label, (bbox_left, bbox_top - 25), cv2.FONT_HERSHEY_SIMPLEX, 0.75, (0, 255, 0), 2)

            # 포착 및 사라진 시간 표시
            if object_states[identity]["start_time"]:
                start_time_str = object_states[identity]["start_time"].strftime("%H:%M:%S")
                # cv2.putText(frame, f'Start: {start_time_str}', (bbox_left, bbox_top - 40), cv2.FONT_HERSHEY_SIMPLEX, 0.75, (255, 0, 0), 2)
                print("움직임 포착 시작 시간 : {0}".format(start_time_str))
            if object_states[identity]["end_time"]:
                end_time_str = object_states[identity]["end_time"].strftime("%H:%M:%S")
                # cv2.putText(frame, f'End: {end_time_str}', (bbox_left, bbox_top - 55), cv2.FONT_HERSHEY_SIMPLEX, 0.75, (255, 0, 0), 2)
                print("움직임 포착 종료 시간 : {0}".format(end_time_str))

    # 화면에 출력
    cv2.imshow('YOLOv5 + SORT', frame)

    # 'q' 키를 누르면 종료
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

# 웹캠 및 창 닫기
cap.release()
cv2.destroyAllWindows()
