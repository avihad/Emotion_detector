package idc.cv.emotiondetector.detectors;

import idc.cv.emotiondetector.Main;
import idc.cv.emotiondetector.utillities.Utilities;

import org.opencv.objdetect.CascadeClassifier;

public enum FacePartCascade
{

	EYE(Main.cascadePath + "/haarcascade_eye.xml"), EYE_PAIR(Main.cascadePath + "/haarcascade_mcs_eyepair_small.xml"), MOUTH(Main.cascadePath
			+ "/haarcascade_smile.xml"), NOSE(Main.cascadePath + "/haarcascade_mcs_nose.xml"), FACE(Main.cascadePath
			+ "/haarcascade_frontalface_alt2.xml");

	private final CascadeClassifier	cascadeClassifier;

	private FacePartCascade(String cascadePath) {
		this.cascadeClassifier = new CascadeClassifier(Utilities.readResource(cascadePath));
	}

	public CascadeClassifier getCascadeClassifier() {
		return cascadeClassifier;
	}

}
