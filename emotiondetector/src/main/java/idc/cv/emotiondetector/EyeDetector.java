package idc.cv.emotiondetector;

import java.io.UnsupportedEncodingException;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;

public class EyeDetector {

	private static final EyeDetector	instance	= new EyeDetector();

	private EyeDetector() {
	}

	public static EyeDetector getInstance() {
		return instance;
	}

	/**
	 * @throws UnsupportedEncodingException
	 * */
	public Pair<Rect, Rect> detectEyes(Mat image) throws Exception {

		MatOfRect eyes = FacePartDetector.getInstance().detect(image, FacePartCascaders.EYE.getClasifier());

		if (eyes.toArray().length != 2) {
			throw new Exception("Found more than two eyes!!");
		}

		Rect first = eyes.toArray()[0];
		Rect second = eyes.toArray()[1];

		if (first.x > second.x) {

			return Pair.of(second, first);
		} else {
			return Pair.of(first, second);
		}
	}
}
