import cv2
import torch
import numpy as np
from transformers import AutoImageProcessor, TimesformerForVideoClassification

# Load the model and processor
image_processor = AutoImageProcessor.from_pretrained("MCG-NJU/videomae-base-finetuned-kinetics")
model = TimesformerForVideoClassification.from_pretrained("facebook/timesformer-base-finetuned-k400")

# Define parameters
frame_height = 224  # Set to the height required by the model
frame_width = 224   # Set to the width required by the model
frame_count = 8     # Number of frames to sample for prediction
frame_sample_rate = 1  # Frame sample rate for real-time

# Load an object detection model (e.g., YOLOv5)
object_detection_model = torch.hub.load('ultralytics/yolov5', 'yolov5s', pretrained=True)

def process_frames(frames):
    """ Process a list of frames with the model. """
    inputs = image_processor(list(frames), return_tensors="pt")
    with torch.no_grad():
        outputs = model(**inputs)
        logits = outputs.logits
    predicted_label = logits.argmax(-1).item()
    return model.config.id2label[predicted_label]

def capture_and_predict():
    cap = cv2.VideoCapture(0)  # Open the webcam

    if not cap.isOpened():
        print("Error: Could not open webcam.")
        return

    frames_buffer = []
    while True:
        ret, frame = cap.read()
        if not ret:
            break

        # Resize frame to the required size for object detection
        resized_frame = cv2.resize(frame, (frame_width, frame_height))

        # Object detection
        results = object_detection_model(resized_frame)
        detections = results.xyxy[0].cpu().numpy()  # Bounding boxes and scores

        # Append frame to buffer
        frames_buffer.append(cv2.cvtColor(resized_frame, cv2.COLOR_BGR2RGB))

        # Ensure we only process a fixed number of frames
        if len(frames_buffer) > frame_count:
            frames_buffer.pop(0)

        # Only process if we have enough frames
        if len(frames_buffer) == frame_count:
            label = process_frames(np.array(frames_buffer))

            # Draw bounding boxes and labels
            for *xyxy, conf, cls in detections:
                x1, y1, x2, y2 = map(int, xyxy)
                cv2.rectangle(frame, (x1, y1), (x2, y2), (0, 255, 0), 2)
                cv2.putText(frame, f'{object_detection_model.names[int(cls)]}: {conf:.2f}', (x1, y1 - 10),
                            cv2.FONT_HERSHEY_SIMPLEX, 0.75, (0, 255, 0), 2)

            # Display the label on the frame
            cv2.putText(frame, f'Predicted Action: {label}', (10, 30), cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 255, 0), 2)

        # Show the frame with predictions and bounding boxes
        cv2.imshow('Real-Time Video Classification with Object Detection', frame)

        # Exit the loop if 'q' is pressed
        if cv2.waitKey(1) & 0xFF == ord('q'):
            break

    cap.release()
    cv2.destroyAllWindows()

capture_and_predict()
