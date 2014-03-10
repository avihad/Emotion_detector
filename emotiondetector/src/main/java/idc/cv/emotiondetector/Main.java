package idc.cv.emotiondetector;

import idc.cv.emotiondetector.detectors.MouthDetectorImproved;
import idc.cv.emotiondetector.entities.DetectedFrame;
import idc.cv.emotiondetector.entities.DetectedMouth;
import idc.cv.emotiondetector.entities.DetectedMovie;
import idc.cv.emotiondetector.smileDetection.SmileCurveFinder;
import idc.cv.emotiondetector.utillities.Optional;
import idc.cv.emotiondetector.utillities.Utilities;
import idc.cv.emotiondetector.utillities.VideoReader;

import java.util.SortedMap;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.highgui.VideoCapture;

public class Main
{
	public static final String	resourcePath	= System.getenv("resourcePath");
	public static final String	outputPath		= System.getenv("outputPath");
	public static final String	cascadePath		= Main.resourcePath + "\\cascades\\";

	public static void main(String[] args) throws Exception {
		if (args.length != 7) {
			System.out.println("USAGE: not enough parametes (require 7 got " + args.length + ")");
			System.exit(-1);
		}
		String naturalInput = resourcePath + args[0];
		String smileInput = resourcePath + args[1];
		String movieName = resourcePath + args[2];
		// 0.002
		Double smileThreshold = Double.valueOf(args[3]);
		// 0.001
		Double naturalThreshold = Double.valueOf(args[4]);
		Double lowFreq = Double.valueOf(args[5]);
		Double highFreq = Double.valueOf(args[6]);

		// Load the native library.
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		analyzeMovie(naturalInput, smileInput, movieName, smileThreshold, naturalThreshold, lowFreq, highFreq);
	}

	private static void analyzeMovie(String neutralImagePath, String smileImagePath, String movieFilePath, double smileThreshold,
			double neutralThreshold, double lowFreq, double highFreq) throws Exception {
		// Run the pulse application:
		SortedMap<Integer, Integer> pulseByFrame = PulseDetectorMain.detectPulse(movieFilePath, lowFreq, highFreq, 10);

		DetectedMovie movie = findSmilesAndGenerateXml(neutralImagePath, smileImagePath, movieFilePath, smileThreshold, neutralThreshold,
				pulseByFrame);

		Utilities.witeToXml(movie, Main.outputPath + "movieAnalysisResult.xml");
	}

	private static DetectedMovie findSmilesAndGenerateXml(String neutralImagePath, String smileImagePath, String movieFilePath,
			double smileThreshold, double neutralThreshold, SortedMap<Integer, Integer> pulseByFrame) throws Exception {
		Mat neutralImage = Utilities.readImage(neutralImagePath);
		Mat smileImage = Utilities.readImage(smileImagePath);

		Rect neutralMouth = MouthDetectorImproved.instance.detectIn(neutralImage).get();
		Rect smilingMouth = MouthDetectorImproved.instance.detectIn(smileImage).get();

		Utilities.writeImageToFile(Main.outputPath + "neutralResult.jpg", neutralImage);
		Utilities.writeImageToFile(Main.outputPath + "smileResult.jpg", smileImage);

		double neutralCurve = SmileCurveFinder.smileCurveOf(neutralImage, neutralMouth)[1];
		double smileCurve = SmileCurveFinder.smileCurveOf(smileImage, smilingMouth)[1];

		System.out.println("neutral curve: " + neutralCurve);
		System.out.println("smile curve: " + smileCurve);

		Mat movieFrame = new Mat();
		VideoCapture videoCapture = VideoReader.instance.open(movieFilePath);
		int frameNumber = 1;

		int numOfFramesOfSmile = 0;
		int numOfFramesOfMouthMovement = 0;

		DetectedMovie movie = new DetectedMovie();
		movie.setName(movieFilePath);

		while (videoCapture.read(movieFrame)) {
			DetectedFrame detectedFrame = new DetectedFrame();
			detectedFrame.setFrameNumber(frameNumber);
			detectedFrame.setPulse(pulseByFrame.get(frameNumber));

			Optional<Rect> optionalMouth = MouthDetectorImproved.instance.detectIn(movieFrame);

			if (optionalMouth.isPresent()) {
				DetectedMouth detectedMouth;

				Rect mouth = optionalMouth.get();
				double mouthCurve = SmileCurveFinder.smileCurveOf(movieFrame, mouth)[1];

				if (mouthCurve >= smileCurve || smileCurve - mouthCurve <= smileThreshold) {
					System.out.println("Smile is detected at frame number: " + frameNumber);
					numOfFramesOfSmile++;

					detectedMouth = new DetectedMouth(mouth, Boolean.TRUE);
				} else {
					detectedMouth = new DetectedMouth(mouth, Boolean.FALSE);

					if (mouthCurve > ((neutralCurve + smileCurve) / 3) + neutralThreshold) {
						System.out.println("Mouth movement is detected at frame number: " + frameNumber);
						numOfFramesOfMouthMovement++;
					}
				}

				detectedFrame.addDetectedPart(detectedMouth);
			}
			frameNumber++;
			movie.addFrame(detectedFrame);

			if (frameNumber % 50 == 0) {
				System.out.println("Passed: " + frameNumber + " frames");
			}
		}

		System.out.println("Number of frames with a smile: " + numOfFramesOfSmile);
		System.out.println("Number of frames with mouth movement: " + numOfFramesOfMouthMovement);
		movie.setNumOfFramesWithASmile(numOfFramesOfSmile);

		return movie;
	}
}
