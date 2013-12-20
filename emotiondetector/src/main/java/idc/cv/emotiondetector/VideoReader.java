package idc.cv.emotiondetector;

import static org.opencv.imgproc.Imgproc.COLOR_RGB2GRAY;
import static org.opencv.imgproc.Imgproc.Canny;
import static org.opencv.imgproc.Imgproc.GaussianBlur;
import static org.opencv.imgproc.Imgproc.cvtColor;

import idc.cv.emotiondetector.utillities.Optional;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.highgui.VideoCapture;

import java.util.ArrayList;
import java.util.List;

public enum VideoReader
{
    instance;

    public VideoCapture open(String videoFileName) throws Exception
    {
        VideoCapture videoCapture = new VideoCapture();

        videoCapture.open(videoFileName);

        if (!videoCapture.isOpened())
        {
            throw new Exception("Couldn't read movie: " + videoFileName);
        }

        return videoCapture;
    }

    public List<Rect> getMouthsAlongMovie(String movieFileName) throws Exception
    {
        VideoCapture videoCapture = open(movieFileName);

        Mat edges = new Mat();
        Mat frame = new Mat();

        Integer index = 1;
        List<Rect> mouthsAlongMovie = new ArrayList<Rect>();
        while (videoCapture.read(frame) && index < 20)
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
        }

        return mouthsAlongMovie;
    }
}
