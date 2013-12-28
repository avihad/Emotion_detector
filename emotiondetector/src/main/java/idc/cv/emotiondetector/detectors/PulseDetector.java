package idc.cv.emotiondetector.detectors;

import idc.cv.emotiondetector.MovieMouthTracker;
import idc.cv.emotiondetector.utillities.VideoReader;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.highgui.VideoCapture;

public enum PulseDetector
{

	instance;

	/*
	 * Execute Rubinshtain algorithm for magnifying the video colors and
	 * calculate the difference between the color of the pixels in the movie
	 * frames
	 */
	public static Map<Integer, double[][]> detectPulse(String movieFileName) throws Exception {

		// Call video magnifying algorithm
		String magnifyMovieFileName = executeVideoMagnifyer(movieFileName);
		System.out.println("Finish video magnifier run");

		VideoReader.instance.open(movieFileName);

		Map<Integer, double[][]> frameSamples = new HashMap<Integer, double[][]>();

		VideoCapture videoAfterMagnifying = VideoReader.instance.open(magnifyMovieFileName);

		Map<Integer, Rect> mouthPositionsAlongMovie = MovieMouthTracker.getMouthPositionsAlongMovieWithIndices(movieFileName);
		System.out.println("Finish mouth tracker run.");

		Mat frame = new Mat();
		Integer index = 1;

		while (videoAfterMagnifying.read(frame)) {
			Rect r = mouthPositionsAlongMovie.get(index);
			frameSamples.put(index, sampleFrame(frame, r));
		}
		System.out.println("Finish extracting pulse run");
		return frameSamples;
	}

	private static double[][] sampleFrame(Mat frame, Rect r) {

		double[][] samples = new double[4][];

		// Mouth frame sample
		int yIndex = r.height / 2 + r.y;
		int xIndex = r.width / 2 + r.x;
		samples[0] = frame.get(xIndex, yIndex);

		// Nose frame sample
		yIndex = (int) (r.y + r.height * 1.5);
		xIndex = r.width / 2 + r.x;
		samples[1] = frame.get(xIndex, yIndex);

		// Left chick frame sample
		yIndex = (int) (r.y + r.height * 1.5);
		xIndex = (int) (r.x + r.width * 0.1);
		samples[2] = frame.get(xIndex, yIndex);

		// Right chick frame sample
		yIndex = (int) (r.y + r.height * 1.5);
		xIndex = (int) (r.x + r.width * 0.9);
		samples[3] = frame.get(xIndex, yIndex);

		return samples;

	}

	private static String executeVideoMagnifyer(String movieFileName) throws IOException, InterruptedException {

		String relativePath = ".\\target\\classes\\";
		String commandAndArgs = new String("cmd /c start " + relativePath + "VideoMagnification\\videoMagnifier.bat " + relativePath + movieFileName);

		// Executing VideoMagnification , after the execution finish it create
		// anew file called finishMagnifierExe
		Runtime.getRuntime().exec(commandAndArgs);

		File f = new File("finishMagnifierExe");

		while (!f.exists()) {
			Thread.sleep(3000);
		}

		f.delete();

		StringBuilder sb = new StringBuilder();
		sb.append("/");
		sb.append(movieFileName.substring(0, movieFileName.length() - 4));
		sb.append("-ideal-from-0.83333-to-1-alpha-50-level-6-chromAtn-1.avi");
		return sb.toString();
	}
}
