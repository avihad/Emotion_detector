package idc.cv.emotiondetector;

import idc.cv.emotiondetector.detectors.MouthDetector;
import idc.cv.emotiondetector.utillities.Optional;
import idc.cv.emotiondetector.utillities.VideoReader;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.highgui.VideoCapture;

import java.util.ArrayList;
import java.util.List;

import static org.opencv.imgproc.Imgproc.*;

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
}
