package idc.cv.emotiondetector;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.objdetect.CascadeClassifier;

public class FacePartDetector {
	public void recognize(Mat frame, String cascaseFileName, String outputFileName) throws UnsupportedEncodingException {

		MatOfRect detectedParts = detect(cascaseFileName, frame);

		write(detectedParts, frame, outputFileName);
	}

	public void recognize(String fileName, String cascaseFileName, String outputFileName) throws UnsupportedEncodingException {

		Mat image = createImageMat(fileName);

		recognize(image, cascaseFileName, outputFileName);
	}

	private void write(MatOfRect detectedParts, Mat image, String outputFileName) {

		// Draw a bounding box around each face.
		for (Rect rect : detectedParts.toArray()) {
			Core.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
		}

		// Save the visualized detection.
		writeImageToFile(outputFileName, image);
	}

	public MatOfRect detect(String facePartsDetectorName, Mat image) throws UnsupportedEncodingException {
		CascadeClassifier faceDetector = createCascadeClassifier(facePartsDetectorName);
		// MatOfRect is a special container class for Rect.
		MatOfRect detectedParts = new MatOfRect();
		faceDetector.detectMultiScale(image, detectedParts);

		System.out.println(String.format("Detected %s faces", detectedParts.toArray().length));

		return detectedParts;

	}

	/*
	 * Create a face detector from the cascade file in the resources directory.
	 */
	private CascadeClassifier createCascadeClassifier(String cascaseFileName) throws UnsupportedEncodingException {
		return new CascadeClassifier(readResource(cascaseFileName));
	}

	private Mat createImageMat(String fileName) throws UnsupportedEncodingException {
		return Highgui.imread(readResource(fileName));
	}

	private String readResource(String resourceName) throws UnsupportedEncodingException {
		String resource = getClass().getResource(resourceName).getPath();

		if (resource.startsWith("/", 0)) {
			resource = resource.replaceFirst("/", "");
		}

		return URLDecoder.decode(resource, "UTF-8"); // this will replace %20
														// with spaces
	}

	private void writeImageToFile(String fileName, Mat image) {
		System.out.println(String.format("Writing %s", fileName));
		Highgui.imwrite(fileName, image);
	}
}
