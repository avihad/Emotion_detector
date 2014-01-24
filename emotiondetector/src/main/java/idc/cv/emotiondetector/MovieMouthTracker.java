package idc.cv.emotiondetector;

import idc.cv.emotiondetector.detectors.MouthDetectorImproved;
import idc.cv.emotiondetector.utillities.Optional;
import idc.cv.emotiondetector.utillities.Utilities;
import idc.cv.emotiondetector.utillities.VideoReader;

import java.util.HashMap;
import java.util.Map;

import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.highgui.VideoCapture;

public enum MovieMouthTracker
{
	instance;

	public static Map<Integer, Rect> getMouthPositionsSelectivlyWithIndices(String movieFileName, int selectiveRate) throws Exception {
		VideoCapture videoCapture = VideoReader.instance.open(movieFileName);
		return getMouthPositionSelectivly(videoCapture, selectiveRate);
	}

	/*
	 * calculate mouth position in each frame and return a map of the detected
	 * mouth indexed by frame number
	 */
	public static Map<Integer, Rect> getMouthPositionsAlongMovieWithIndices(String movieFileName) throws Exception {
		VideoCapture videoCapture = VideoReader.instance.open(movieFileName);
		return getMouthPositionSelectivly(videoCapture, 0);
	}

	private static Map<Integer, Rect> getMouthPositionSelectivly(VideoCapture videoCapture, int selectiveRate) throws Exception {
		Mat edges = new Mat();
		Mat frame = new Mat();

		Integer index = 1;
		Map<Integer, Rect> mouthsAlongMovie = new HashMap<Integer, Rect>();
		Optional<Rect> lastDetected = Optional.absent();

		while (videoCapture.read(frame)) {
			Optional<Rect> optionalMouth = lastDetected;
			if (!optionalMouth.isPresent() || ((index - 1) % selectiveRate) == 0) {
				optionalMouth = MouthDetectorImproved.instance.detectIn(frame);
			}

			if (optionalMouth.isPresent()) {
				mouthsAlongMovie.put(index, optionalMouth.get());
				Utilities.drawRect(optionalMouth.get(), frame);
			}

			frame = new Mat();
			index++;
		}
		return mouthsAlongMovie;
	}
}
