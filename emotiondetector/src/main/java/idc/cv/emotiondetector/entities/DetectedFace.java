package idc.cv.emotiondetector.entities;

import org.opencv.core.Rect;

public class DetectedFace extends DetectedPart
{

	public DetectedFace() {
		super();
	}

	public DetectedFace(Rect detectedPart) {
		super(detectedPart);
	}

}
