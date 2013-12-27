package idc.cv.emotiondetector;

import static org.opencv.imgproc.Imgproc.COLOR_RGB2GRAY;
import static org.opencv.imgproc.Imgproc.Canny;
import static org.opencv.imgproc.Imgproc.GaussianBlur;
import static org.opencv.imgproc.Imgproc.cvtColor;
import idc.cv.emotiondetector.detectors.MouthDetector;
import idc.cv.emotiondetector.utillities.Optional;
import idc.cv.emotiondetector.utillities.VideoReader;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.highgui.VideoCapture;

public enum MovieMouthTracker
{
    instance;

    public static List<Rect> getMouthPositionsAlongMovie(String movieFileName) throws Exception
    {
        VideoCapture videoCapture = VideoReader.instance.open(movieFileName);

        Mat edges = new Mat();
        Mat frame = new Mat();

        Integer index = 1;
        List<Rect> mouthsAlongMovie = new ArrayList<Rect>();
        while (videoCapture.read(frame) && index < 40)
        {
            cvtColor(frame, edges, COLOR_RGB2GRAY);

            GaussianBlur(edges, edges, new Size(7, 7), 1.5, 1.5);

            Canny(edges, edges, 0, 30);

            Optional<Rect> optionalMouth = MouthDetector.instance.detectMouth(frame);

            if (optionalMouth.isPresent())
            {
                mouthsAlongMovie.add(optionalMouth.get());
            }

            frame = new Mat();
            index++;
        }
        return mouthsAlongMovie;
    }
	instance;

	public static Collection<Rect> getMouthPositionsAlongMovie(String movieFileName) throws Exception {
		VideoCapture videoCapture = VideoReader.instance.open(movieFileName);
		return getMouthPositionsAlongMovie(videoCapture).values();

	}

	public static Map<Integer, Rect> getMouthPositionsAlongMovieWithIndexs(String movieFileName) throws Exception {
		VideoCapture videoCapture = VideoReader.instance.open(movieFileName);
		return getMouthPositionsAlongMovie(videoCapture);

	}

	/*
	 * calculate mouth position in each frame and return a map of the detected
	 * mouth indexed by frame number
	 */
	public static Map<Integer, Rect> getMouthPositionsAlongMovie(VideoCapture videoCapture) throws Exception {
		Mat edges = new Mat();
		Mat frame = new Mat();

		Integer index = 1;
		Map<Integer, Rect> mouthsAlongMovie = new HashMap<>();
		while (videoCapture.read(frame)) {
			cvtColor(frame, edges, COLOR_RGB2GRAY);

			GaussianBlur(edges, edges, new Size(7, 7), 1.5, 1.5);

			Canny(edges, edges, 0, 30);

			Optional<Rect> optionalMouth = MouthDetector.instance.detectMouth(frame);

			if (optionalMouth.isPresent()) {
				mouthsAlongMovie.put(index, optionalMouth.get());
			}

			frame = new Mat();
			index++;
		}
		return mouthsAlongMovie;
	}
}
