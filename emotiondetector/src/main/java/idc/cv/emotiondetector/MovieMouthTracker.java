package idc.cv.emotiondetector;

import idc.cv.emotiondetector.detectors.MouthDetector;
import idc.cv.emotiondetector.utillities.Optional;
import idc.cv.emotiondetector.utillities.Utilities;
import idc.cv.emotiondetector.utillities.VideoReader;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.highgui.VideoCapture;

import java.util.HashMap;
import java.util.Map;

public enum MovieMouthTracker
{
    instance;

    public static Map<Integer, Rect> getMouthPositionsAlongMovieWithIndices(String movieFileName) throws Exception
    {
        VideoCapture videoCapture = VideoReader.instance.open(movieFileName);
        return getMouthPositionsAlongMovie(videoCapture);
    }

    /*
     * calculate mouth position in each frame and return a map of the detected
     * mouth indexed by frame number
     */
    public static Map<Integer, Rect> getMouthPositionsAlongMovie(VideoCapture videoCapture) throws Exception
    {
        Mat edges = new Mat();
        Mat frame = new Mat();

        Integer index = 1;
        Map<Integer, Rect> mouthsAlongMovie = new HashMap<Integer, Rect>();
        while (videoCapture.read(frame))
        {
            Optional<Rect> optionalMouth = MouthDetector.instance.detectMouth(frame);
            /* Rect mouth = optionalMouth.get();
            for (int row = 0; row < frame.size().height; row++)
            {
                for (int column = 0; column < frame.size().width; column++)
                {
                    if ()
                }
            }
            cvtColor(frame, frame, COLOR_RGB2GRAY);

            GaussianBlur(frame, frame, new Size(7, 7), 1.5, 1.5);

            Canny(frame, frame, 0, 30);
                */

            if (optionalMouth.isPresent())
            {
                mouthsAlongMovie.put(index, optionalMouth.get());
                Utilities.drawRect(optionalMouth.get(), frame);
            }
            Utilities.writeImageToFile("cannyFrame" + index + ".png", frame);

            frame = new Mat();
            index++;
        }
        return mouthsAlongMovie;
    }
}
