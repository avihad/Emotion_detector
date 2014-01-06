package idc.cv.emotiondetector.entities;

import org.opencv.core.Rect;

public class DetectedEyePair extends DetectedPart
{

	public DetectedEyePair() {
		super();
	}

	public DetectedEyePair(Rect detectedPart) {
		super(detectedPart);
	}

}
