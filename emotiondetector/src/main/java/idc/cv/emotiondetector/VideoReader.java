package idc.cv.emotiondetector;

import static org.opencv.imgproc.Imgproc.COLOR_RGB2GRAY;
import static org.opencv.imgproc.Imgproc.Canny;
import static org.opencv.imgproc.Imgproc.GaussianBlur;
import static org.opencv.imgproc.Imgproc.cvtColor;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.VideoCapture;

public class VideoReader {
	public static void main(String[] args) throws Exception {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		VideoCapture videoCapture = new VideoCapture();

		videoCapture.open("aviadi.avi");

		if (!videoCapture.isOpened())
			System.exit(-1);

		Mat edges = new Mat();
		Mat frame = new Mat();

		FacePartDetector faceRecognizer = FacePartDetector.getInstance();
		Integer index = 1;
		while (videoCapture.read(frame) && index < 20) {
			cvtColor(frame, edges, COLOR_RGB2GRAY);

			GaussianBlur(edges, edges, new Size(7, 7), 1.5, 1.5);

			Canny(edges, edges, 0, 30);

			faceRecognizer.detectAndWrite(frame, FacePartCascaders.EYE.getClasifier(), "edges" + (index++) + ".png");

			frame = new Mat();
		}
	}
}
