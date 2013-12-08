package idc.cv.emotiondetector;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;

public class Main {

	public static void main(String[] args) throws Exception {
		System.out.println("Hello, OpenCV");

		// Load the native library.
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		Mat image = Utility.createImageMat("/images.jpg");
		Pair<Rect, Rect> detectedEyes = EyeDetector.getInstance().detectEyes(image);

		MatOfRect face = FacePartDetector.getInstance().detect(image, FacePartCascaders.FACE.getClasifier());
		FacePartDetector.getInstance().write(face, image, "faceRecognition.png");
		System.out.println(face);
	}
}
