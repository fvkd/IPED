# IPED Unlocked Features

This document describes advanced and "hidden" features in IPED that are disabled by default or require specific configurations/dependencies.

## 1. Face Recognition

IPED can detect and recognize faces in images and videos. This feature is implemented in Python.

### Requirements
- Python 3 installed.
- Python packages: `face_recognition`, `opencv-python` (cv2), `numpy`.
- **Note:** `numpy` version must be < 2.x.

### How to Enable
1.  Open your `LocalConfig.txt`.
2.  Add or uncomment:
    ```properties
    enableFaceRecognition = true
    ```
3.  Optionally configure parameters in `conf/FaceRecognitionConfig.txt`.

## 2. Age Estimation

Estimates the age group of detected faces. Requires Face Recognition to be enabled.

### Requirements
- Face Recognition requirements (above).
- Python packages: `transformers`, `torch`, `pillow`, `numpy`.
- Internet access on first run (or manual model download).

### How to Enable
1.  Open `LocalConfig.txt`.
2.  Add:
    ```properties
    enableAgeEstimation = true
    enableFaceRecognition = true
    ```
3.  Configure `conf/AgeEstimationConfig.txt` if needed (e.g., enable GPU).

## 3. NSFW / Nudity Detection (Yahoo Open NSFW)

Detects nudity in images and videos using a deep learning model.

### Requirements
- Python packages: `pillow`, `tensorflow`, `keras`, `numpy`.
- Model file: `nsfw-keras-1.0.0.h5` in `iped-root/models/` (usually bundled or must be downloaded).

### How to Enable
1.  Open `LocalConfig.txt`.
2.  Add:
    ```properties
    enableYahooNSFWDetection = true
    ```

## 4. Remote Image Classifier

Classifies images by sending them to a remote API (e.g., for more advanced CSAM detection or object recognition).

### How to Enable
1.  Open `LocalConfig.txt`.
2.  Add:
    ```properties
    enableRemoteImageClassifier = true
    ```
3.  Configure `conf/RemoteImageClassifierConfig.txt`:
    ```properties
    url = http://your-classifier-service/api
    ```

## 5. PhotoDNA (Restricted)

PhotoDNA is a hashing algorithm used to detect CSAM.

### Requirements
- **Restricted Library:** Requires `br.dpf.sepinf.photodna.PhotoDNA` class (jar file) provided to law enforcement.

### How to Enable
1.  Open `LocalConfig.txt`.
2.  Add:
    ```properties
    enablePhotoDNA = true
    ```

## 6. NixOS / Linux Improvements

### Sleuthkit Path
We have patched IPED to support `-Diped.tsk.jar.path` system property.
This allows running IPED with system-installed Sleuthkit.

Example:
```bash
java -Diped.tsk.jar.path=/usr/share/java/sleuthkit.jar -jar iped.jar ...
```
The provided `flake.nix` handles this automatically.
