import cv2
import torch
import numpy as np
from deep_sort_realtime.deepsort_tracker import DeepSort

# Load YOLOv5 model for object detection
object_detection_model = torch.hub.load('ultralytics/yolov5', 'yolov5s', pretrained=True)

# Initialize DeepSORT for object tracking
deepsort = DeepSort(metric="cosine")

def detect_and_track(frame):
    # YOLOv5 object detection
    results = object_detection_model(frame)
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
    
    # Handle case where no detections are available
    if not detections:
        detections = []
    
    outputs = deepsort.update_tracks(detections, frame)

    return outputs, bbox_xywh, clss


def main():
    cap = cv2.VideoCapture(0)  # Open the webcam

    if not cap.isOpened():
        print("Error: Could not open webcam.")
        return

    while True:
        ret, frame = cap.read()
        if not ret:
            break

        outputs, bbox_xywh, clss = detect_and_track(frame)

        # Draw bounding boxes and labels
        for track in outputs:
            bbox_left, bbox_top, bbox_right, bbox_bottom = track.to_tlbr()
            identity = int(track.track_id)
            obj_class = clss[identity] if 0 <= identity < len(clss) else 'unknown'

            # Draw bounding box
            cv2.rectangle(frame, (int(bbox_left), int(bbox_top)), (int(bbox_right), int(bbox_bottom)), (0, 255, 0), 2)
            cv2.putText(frame, f'ID {identity}', (int(bbox_left), int(bbox_top) - 10), cv2.FONT_HERSHEY_SIMPLEX, 0.75, (0, 255, 0), 2)

        # Display the frame with bounding boxes and labels
        cv2.imshow('Real-Time Object Tracking', frame)

        # Exit the loop if 'q' is pressed
        if cv2.waitKey(1) & 0xFF == ord('q'):
            break

    cap.release()
    cv2.destroyAllWindows()

if __name__ == "__main__":
    main()
