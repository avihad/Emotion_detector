package idc.cv.emotiondetector.detectors;

import idc.cv.emotiondetector.Main;
import idc.cv.emotiondetector.MovieMouthTracker;
import idc.cv.emotiondetector.utillities.Utilities;
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
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.highgui.VideoCapture;

import static org.opencv.imgproc.Imgproc.COLOR_RGB2YUV;
import static org.opencv.imgproc.Imgproc.cvtColor;

public enum PulseDetector
{

	instance;

	/*
	 * Execute Rubinshtain algorithm for magnifying the video colors and
	 * calculate the difference between the color of the pixels in the movie
	 * frames
	 */
	public SortedMap<Integer, double[][]> detectPulse(String movieFileName, int samplePositionChangeRate) throws Exception {

		Map<Integer, Rect> mouthPositionsAlongMovie = MovieMouthTracker.getMouthPositionsSelectivlyWithIndices(movieFileName,
				samplePositionChangeRate);
		System.out.println("Finish mouth tracker run.");

		return this.detectPulse(movieFileName, mouthPositionsAlongMovie);
	}

	/*
	 * Execute Rubinshtain algorithm for magnifying the video colors and
	 * calculate the difference between the color of the pixels in the movie
	 * frames
	 */
	public SortedMap<Integer, double[][]> detectPulse(String movieFileName, Map<Integer, Rect> mouthPositionsAlongMovie) throws Exception {

		// Call video magnifying algorithm
		String magnifyMovieFileName = executeVideoMagnifyer(movieFileName);
		System.out.println("Finish video magnifier run");

		SortedMap<Integer, double[][]> frameSamples = new TreeMap<>();

		VideoCapture videoAfterMagnifying = VideoReader.instance.open(magnifyMovieFileName);

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

	public static int	index	= 1;

	private double[][] sampleFrame(Mat frame, Rect r) {

        Mat yuvFrame = new Mat();

        cvtColor(frame, yuvFrame, COLOR_RGB2YUV);
		double[][] samples = new double[4][];

		// Forehead frame sample
		int yIndex = r.y - r.height * 2 - 130;
		int xIndex = r.width / 2 + r.x;
		samples[0] = yuvFrame.get(yIndex, xIndex);
		Utilities.drawPoint(new Point(xIndex, yIndex), frame);

		// Nose frame sample
		yIndex = r.y - r.height + 10;
		xIndex = r.width / 2 + r.x;
		samples[1] = yuvFrame.get(yIndex, xIndex);
		Utilities.drawPoint(new Point(xIndex, yIndex), frame);

		// Left chick frame sample
		yIndex = r.y - r.height + 20;
		xIndex = (int) (r.x + r.width * 0.1) - 30;
		samples[2] = yuvFrame.get(yIndex, xIndex);
		Utilities.drawPoint(new Point(xIndex, yIndex), frame);

		// Right chick frame sample
		yIndex = r.y - r.height + 20;
		xIndex = (int) (r.x + r.width * 0.9) + 30;
		samples[3] = yuvFrame.get(yIndex, xIndex);
		Utilities.drawPoint(new Point(xIndex, yIndex), frame);

        if (index == 1)
        {
		    Utilities.writeImageToFile("pointsLocations" + index++ + ".png", frame);
        }

		return samples;

	}

	public SortedMap<Integer, Integer> calcPulseFromSamples(SortedMap<Integer, double[][]> frameSamples, int threshold, int movieFrameRate) {

		List<Integer> identicaleSampleFrameNum = new ArrayList<>();

		Integer comparationFrameNum = frameSamples.firstKey();
		double[][] comprationsValue = frameSamples.get(comparationFrameNum);

		for (Map.Entry<Integer, double[][]> sample : frameSamples.entrySet()) {

			Integer sampleKey = sample.getKey();
			if ((sampleKey - comparationFrameNum) > 9 && (sampleKey - comparationFrameNum) < 34
					&& isSampleNotDiffer(comprationsValue, sample.getValue(), threshold)) {
				identicaleSampleFrameNum.add(sampleKey);
				comparationFrameNum = sampleKey;
				// comprationsValue = sample.getValue();
			}
		}

		int frameResolution = 1;
		return calcPulseRateByFrameResolution(movieFrameRate, identicaleSampleFrameNum, frameResolution);
	}

	private SortedMap<Integer, Integer> calcPulseRateByFrameResolution(double movieFrameRate, List<Integer> identicaleSampleFrameNum,
			int frameResolution) {

		SortedMap<Integer, Integer> pulseByFrame = new TreeMap<>();

		Collections.sort(identicaleSampleFrameNum);

		for (int i = 0; i < identicaleSampleFrameNum.size() - frameResolution; i++) {

			Integer firstFrame = identicaleSampleFrameNum.get(i);
			Integer secondFrame = identicaleSampleFrameNum.get(i + frameResolution);
			Double pulse = (movieFrameRate / ((secondFrame - firstFrame) / frameResolution)) * 60;

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

		for (int i = 0; i < value.length; i++) {

			if (comprationsValue[i] == null || value[i] == null || comprationsValue[i].length != value[i].length) {
				continue;
			}

            double yDiff = Math.pow(Math.abs(comprationsValue[i][0] - value[i][0]), 2);
            double uDiff = Math.pow(Math.abs(comprationsValue[i][1] - value[i][1]), 2);
            double vDiff = Math.pow(Math.abs(comprationsValue[i][2] - value[i][2]), 2);
            double diff = Math.sqrt(yDiff + uDiff + vDiff);

            if (diff > threshold)
            {
                return false;
            }
		}

		return true;
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
		sb.append(movieFileName.substring(Main.resourcePath.length() - 1, movieFileName.length() - 4));
		sb.append("-ideal-from-1.6667-to-2-alpha-50-level-1-chromAtn-1.avi");
		String outputFileName = sb.toString();

		if (Object.class.getResource(outputFileName) == null) {

			String commandAndArgs = new String("cmd /c start " + Main.resourcePath + "\\VideoMagnification\\videoMagnifier.bat " + movieFileName);
			System.out.println("Magnifier cmd: " + commandAndArgs);
			// Executing VideoMagnification , after the execution finish it
			// create a new file called finishMagnifierExe
			Runtime.getRuntime().exec(commandAndArgs);

			File f = new File(Main.outputPath + "finishMagnifierExe");

			while (!f.exists()) {
				Thread.sleep(2000);
			}

			f.delete();
		}

		return Main.outputPath + outputFileName;
	}
}
