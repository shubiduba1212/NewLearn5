import cv2
from ultralytics import YOLO

# Load YOLOv5 model
model = YOLO("yolov5s.pt")

# Video capture
cap = cv2.VideoCapture('video/sample.mp4')

# Dictionary to keep track of first detection times
first_detection_times = {}

# Frame count
frame_count = 0

while cap.isOpened():
    ret, frame = cap.read()
    if not ret:
        break
    
    # Perform inference
    results = model(frame)
    
    # Initialize current detections for this frame
    current_detections = set()
    
    for result in results:
        for box in result.boxes:
            x1, y1, x2, y2 = map(int, box.xyxy[0].tolist())
            conf = box.conf[0].item()
            class_id = int(box.cls[0].item())
            
            # Get class name from model
            class_name = model.names[class_id] if hasattr(model, 'names') else str(class_id)
            detection_id = (class_name, x1, y1, x2, y2)  # Unique ID for this detection
            
            # Record the first detection time if not already recorded
            if detection_id not in first_detection_times:
                first_detection_times[detection_id] = frame_count
            
            # Draw bounding boxes and labels
            cv2.rectangle(frame, (x1, y1), (x2, y2), (255, 0, 0), 2)
            cv2.putText(frame, f"{class_name} {conf:.2f}", (x1, y1 - 10),
                        cv2.FONT_HERSHEY_SIMPLEX, 0.5, (255, 0, 0), 2)
            
            # Add current detection to the set
            current_detections.add(detection_id)
    
    # Save the resulting frame to a file
    output_filename = f'output/frame_{frame_count:04d}.jpg'
    cv2.imwrite(output_filename, frame)
    
    # Update frame count
    frame_count += 1
    
    # Print out the first detection times of objects that are currently detected
    for detection_id in current_detections:
        if detection_id in first_detection_times:
            print(f"Object: {detection_id[0]}, First Detected at Frame: {first_detection_times[detection_id]}")
    
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

cap.release()
cv2.destroyAllWindows()
