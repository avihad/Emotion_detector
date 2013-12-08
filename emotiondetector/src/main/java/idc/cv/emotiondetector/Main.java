package idc.cv.emotiondetector;

import java.io.UnsupportedEncodingException;

import org.opencv.core.Core;

public class Main {

	public static void main(String[] args) throws UnsupportedEncodingException {
		System.out.println("Hello, OpenCV");

		// Load the native library.
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		new FacePartDetector().recognize("/images.jpg", "/haarcascade_eye.xml", "faceDetection.png");
	}
}
