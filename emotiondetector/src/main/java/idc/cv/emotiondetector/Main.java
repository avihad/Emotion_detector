package idc.cv.emotiondetector;

import idc.cv.emotiondetector.detectors.MouthDetector;
import idc.cv.emotiondetector.utillities.Optional;
import idc.cv.emotiondetector.utillities.Utilities;
import idc.cv.emotiondetector.utillities.VideoReader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.highgui.VideoCapture;

import java.util.ArrayList;
import java.util.List;

import static org.opencv.imgproc.Imgproc.*;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        // Load the native library.
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Mat image = Utilities.createImageMat("/aviadSmile.png");

        List<Rect> mouthsAlongMovie = MovieMouthTracker.getMouthPositionsAlongMovie("/aviad.avi");

        for (Rect mouth : mouthsAlongMovie)
        {
            Utilities.drawRect(mouth, image);
        }

        Utilities.writeImageToFile("faceRecognition.png", image);
    }
}
