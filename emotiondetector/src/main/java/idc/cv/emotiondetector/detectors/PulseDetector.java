package idc.cv.emotiondetector.detectors;

import idc.cv.emotiondetector.MovieMouthTracker;
import idc.cv.emotiondetector.utillities.VideoReader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

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
	public SortedMap<Integer, double[][]> detectPulse(String movieFileName) throws Exception {

		// Call video magnifying algorithm
		String magnifyMovieFileName = executeVideoMagnifyer(movieFileName);
		System.out.println("Finish video magnifier run");

		SortedMap<Integer, double[][]> frameSamples = new TreeMap<Integer, double[][]>();

		VideoCapture videoAfterMagnifying = VideoReader.instance.open(magnifyMovieFileName);

		Map<Integer, Rect> mouthPositionsAlongMovie = MovieMouthTracker.getMouthPositionsAlongMovieWithIndices(movieFileName);
		System.out.println("Finish mouth tracker run.");

		Mat frame = new Mat();
		Integer index = 1;

		while (videoAfterMagnifying.read(frame)) {
			Rect r = mouthPositionsAlongMovie.get(index);
			if (r != null) {
				frameSamples.put(index, sampleFrame(frame, r));
			}

			frame = new Mat();
			index++;
		}
		System.out.println("Finish extracting pulse run");
		return frameSamples;
	}

	private double[][] sampleFrame(Mat frame, Rect r) {

		double[][] samples = new double[4][];

		// Mouth frame sample
		int yIndex = r.height / 2 + r.y;
		int xIndex = r.width / 2 + r.x;
		samples[0] = frame.get(yIndex, xIndex);

		// Nose frame sample
		yIndex = (int) (r.y + r.height * 1.5);
		xIndex = r.width / 2 + r.x;
		samples[1] = frame.get(yIndex, xIndex);

		// Left chick frame sample
		yIndex = (int) (r.y + r.height * 1.5);
		xIndex = (int) (r.x + r.width * 0.1);
		samples[2] = frame.get(yIndex, xIndex);

		// Right chick frame sample
		yIndex = (int) (r.y + r.height * 1.5);
		xIndex = (int) (r.x + r.width * 0.9);
		samples[3] = frame.get(yIndex, xIndex);

		return samples;

	}

	public SortedMap<Integer, Integer> calcPulseFromSamples(SortedMap<Integer, double[][]> frameSamples, int threshold, int movieFrameRate) {

		List<Integer> identicaleSampleFrameNum = new ArrayList<>();

		Integer comparationFrameNum = frameSamples.firstKey();
		double[][] comprationsValue = frameSamples.get(comparationFrameNum);

		for (Map.Entry<Integer, double[][]> sample : frameSamples.entrySet()) {

			Integer sampleKey = sample.getKey();
			if ((sampleKey - comparationFrameNum) > 10 && isSampleNotDiffer(comprationsValue, sample.getValue(), threshold)) {
				identicaleSampleFrameNum.add(sampleKey);
				comparationFrameNum = sampleKey;
				comprationsValue = sample.getValue();
			}
		}

		int frameResolution = 2;
		return calcPulseRateByFrameResolution(movieFrameRate, identicaleSampleFrameNum, frameResolution);
	}

	private SortedMap<Integer, Integer> calcPulseRateByFrameResolution(double movieFrameRate, List<Integer> identicaleSampleFrameNum,
			int frameResolution) {

		SortedMap<Integer, Integer> pulseByFrame = new TreeMap<>();

		Collections.sort(identicaleSampleFrameNum);

		for (int i = 0; i < identicaleSampleFrameNum.size() - frameResolution; i++) {

			Integer firstFrame = identicaleSampleFrameNum.get(i);
			Integer secondFrame = identicaleSampleFrameNum.get(i + frameResolution);
			Double pulse = (movieFrameRate / (secondFrame - firstFrame)) * 60;

			pulseByFrame.put(secondFrame, pulse.intValue());
			System.out.println("Pulse calculation for frames #" + firstFrame + " #" + secondFrame + " is: " + pulse.intValue());
		}

		return pulseByFrame;
	}

	/**
	 * Check if one of the samples in comprationsValue and value don't differ by
	 * more then threshold
	 * 
	 * @param comprationsValue
	 * @param value
	 * @param threshold
	 * */
	private boolean isSampleNotDiffer(double[][] comprationsValue, double[][] value, int threshold) {

		if (comprationsValue == null || value == null || comprationsValue.length != value.length) {
			return Boolean.FALSE;
		}

		boolean isSampleMatch = Boolean.FALSE;

		for (int i = 0; i < value.length && !isSampleMatch; i++) {

			if (comprationsValue[i] == null || value[i] == null || comprationsValue[i].length != value[i].length) {
				continue;
			}

			isSampleMatch = Boolean.TRUE;
			for (int j = 0; j < value[i].length && isSampleMatch; j++) {
				if (Math.abs(comprationsValue[i][j] - value[i][j]) > threshold) {
					isSampleMatch = Boolean.FALSE;
					continue;
				}
			}

		}
		return isSampleMatch;
	}

	public void printSamples(Map<Integer, double[][]> pulseSamples) {

		for (Map.Entry<Integer, double[][]> entry : pulseSamples.entrySet()) {
			System.out.println();
			System.out.println("Frame #: " + entry.getKey());

			double[][] samples = entry.getValue();
			for (int i = 0; i < samples.length; i++) {
				System.out.print("Sample #: " + i + " Value: ");
				for (int j = 0; j < entry.getValue()[i].length; j++) {
					System.out.print(" " + entry.getValue()[i][j]);

				}
				System.out.println();
			}

		}
	}

	private String executeVideoMagnifyer(String movieFileName) throws IOException, InterruptedException {

		StringBuilder sb = new StringBuilder();
		sb.append(movieFileName.substring(0, movieFileName.length() - 4));
		sb.append("-ideal-from-0.83333-to-1-alpha-50-level-6-chromAtn-1.avi");
		String outputFileName = sb.toString();

		if (Object.class.getResource(outputFileName) == null) {

			String relativePath = ".\\target\\classes\\";
			String commandAndArgs = new String("cmd /c start " + relativePath + "VideoMagnification\\videoMagnifier.bat " + relativePath
					+ movieFileName);

			// Executing VideoMagnification , after the execution finish it
			// create a new file called finishMagnifierExe
			Runtime.getRuntime().exec(commandAndArgs);

			File f = new File("finishMagnifierExe");

			while (!f.exists()) {
				Thread.sleep(2000);
			}

			f.delete();
		}

		return outputFileName;
	}
}
