package idc.cv.emotiondetector;

import idc.cv.emotiondetector.utillities.Utilities;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;

import java.util.List;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        System.out.println("Hello, OpenCV");

        // Load the native library.
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Mat image = Utilities.createImageMat("/aviadSmile.png");

        List<Rect> mouthsAlongMovie = VideoReader.instance.getMouthsAlongMovie(Utilities.readResource("/aviad.avi"));

        for (Rect mouth : mouthsAlongMovie)
        {
            Utilities.drawRect(mouth, image);
        }

        Utilities.writeImageToFile("faceRecognition.png", image);
    }
}
