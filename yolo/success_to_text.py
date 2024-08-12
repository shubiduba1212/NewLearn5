import cv2
from transformers import DetrImageProcessor, DetrForObjectDetection
import torch
import time
import os

# Load the model and processor
model = DetrForObjectDetection.from_pretrained("facebook/detr-resnet-50")
processor = DetrImageProcessor.from_pretrained("facebook/detr-resnet-50")

# Create a directory for saving detected objects if it doesn't exist
os.makedirs('detected_objects', exist_ok=True)

# Initialize video capture
cap = cv2.VideoCapture('video/street.mp4')  # Replace with 0 for default webcam

# Track detected objects
detected_objects = {}

while cap.isOpened():
    ret, frame = cap.read()
    if not ret:
        break
    
    # Preprocess the image
    inputs = processor(images=frame, return_tensors="pt")
    
    # Perform inference
    with torch.no_grad():
        outputs = model(**inputs)
    
    # Extract results
    target_sizes = torch.tensor([frame.shape[:2]])
    results = processor.post_process_object_detection(outputs, target_sizes=target_sizes, threshold=0.9)[0]
    
    # Get current timestamp
    current_time = time.strftime("%Y-%m-%d_%H-%M-%S")
    
    # Draw bounding boxes and labels on the frame
    for score, label, box in zip(results["scores"], results["labels"], results["boxes"]):
        x1, y1, x2, y2 = box
        class_name = model.config.id2label[label.item()]
        
        # Convert coordinates to integers
        x1, y1, x2, y2 = int(x1), int(y1), int(x2), int(y2)
        
        # Draw bounding box
        cv2.rectangle(frame, (x1, y1), (x2, y2), (255, 0, 0), 2)
        cv2.putText(frame, f"{class_name} {score:.2f}", (x1, y1 - 10),
                    cv2.FONT_HERSHEY_SIMPLEX, 0.5, (255, 0, 0), 2)
        
        # Check if this object is new
        object_id = f"{class_name}_{x1}_{y1}_{x2}_{y2}"
        if object_id not in detected_objects:
            detected_objects[object_id] = current_time
            # Save the detected object's image
            object_img = frame[y1:y2, x1:x2]
            cv2.imwrite(f"detected_objects/{class_name}_{current_time}.jpg", object_img)
            print(f"Detected new object: {class_name}, Time: {current_time}")
    
    # Display the resulting frame
    cv2.imshow('Object Detection', frame)
    
    # Exit on 'q' key
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

# Release resources
cap.release()
cv2.destroyAllWindows()
