package idc.cv.emotiondetector.detectors;

import idc.cv.emotiondetector.MovieMouthTracker;
import idc.cv.emotiondetector.utillities.VideoReader;

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
	public static Map<Integer, double[]> detectPulse(String movieFileName) throws Exception {

		// Call video magnifying algorithm
		String magnifyMovieFileName = executeVideoMagnifyer(movieFileName);

		Map<Integer, double[]> frameSamples = new HashMap<Integer, double[]>();
		VideoCapture videoAfterMagnifying = VideoReader.instance.open(magnifyMovieFileName);

		Map<Integer, Rect> mouthPositionsAlongMovie = MovieMouthTracker.getMouthPositionsAlongMovieWithIndices(movieFileName);
		Mat frame = new Mat();
		Integer index = 1;

		while (videoAfterMagnifying.read(frame)) {
			Rect r = mouthPositionsAlongMovie.get(index);
			int yIndex = r.height / 2 + r.y;
			int xIndex = r.width / 2 + r.x;
			double[] frameSample = frame.get(xIndex, yIndex);
			frameSamples.put(index, frameSample);
		}

		return frameSamples;
	}

	private static String executeVideoMagnifyer(String movieFileName) throws IOException {
		String[] commandAndArgs = new String[] { "./VideoMagnifier/videoMagnifier.bat", movieFileName };
		Process process = Runtime.getRuntime().exec(commandAndArgs);
		process.exitValue();

		StringBuilder sb = new StringBuilder();
		sb.append(movieFileName).substring(0, movieFileName.length() - 4);
		sb.append("-ideal-from-0.83333-to-1-alpha-50-level-6-chromAtn-1.avi");
		return sb.toString();
	}
}
