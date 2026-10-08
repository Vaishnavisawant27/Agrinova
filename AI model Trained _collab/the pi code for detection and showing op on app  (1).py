from ultralytics import YOLO
from picamera2 import Picamera2
import cv2
import time
import threading
from flask import Flask, Response

model = YOLO("best.pt")

picam2 = Picamera2()
picam2.configure(picam2.create_preview_configuration(main={"format": "RGB888", "size": (640, 480)}))
picam2.start()
time.sleep(2)
print("Camera started!")

flask_app = Flask(__name__)
annotated_frame = None
lock = threading.Lock()

def capture_and_detect():
    global annotated_frame
    while True:
        frame = picam2.capture_array()
        frame = cv2.cvtColor(frame, cv2.COLOR_RGB2BGR)
        results = model(frame, conf=0.4)
        annotated = results[0].plot()
        annotated = cv2.cvtColor(annotated, cv2.COLOR_BGR2RGB)
        with lock:
            annotated_frame = annotated.copy()

def generate_frames():
    global annotated_frame
    while True:
        with lock:
            if annotated_frame is None:
                continue
            ret, buffer = cv2.imencode('.jpg', annotated_frame)
            if not ret:
                continue
            frame_bytes = buffer.tobytes()
        yield (b'--frame\r\n'
               b'Content-Type: image/jpeg\r\n\r\n' +
               frame_bytes + b'\r\n')

@flask_app.route('/video_feed')
def video_feed():
    return Response(generate_frames(),
                    mimetype='multipart/x-mixed-replace; boundary=frame')

@flask_app.route('/status')
def status():
    return {"status": "running"}

detect_thread = threading.Thread(target=capture_and_detect)
detect_thread.daemon = True
detect_thread.start()

print("Starting Flask on Pi...")
flask_app.run(host='0.0.0.0', port=8000, debug=False)