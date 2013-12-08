package idc.cv.emotiondetector;

import org.opencv.objdetect.CascadeClassifier;

public enum FacePartCascaders {

	EYE("/haarcascade_eye.xml"), MOUTH("/haarcascade_smile.xml"), FACE("/haarcascade_frontalface_alt2.xml");

	private final CascadeClassifier	clasifier;

	private FacePartCascaders(String cascadePath) {
		this.clasifier = new CascadeClassifier(Utility.readResource(cascadePath));
	}

	public CascadeClassifier getClasifier() {
		return clasifier;
	}

}
