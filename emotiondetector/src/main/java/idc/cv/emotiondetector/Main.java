package idc.cv.emotiondetector;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        System.out.println("Hello, OpenCV");

        // Load the native library.
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Mat image = Utilities.createImageMat("/edges5.png");

        Pair<Rect, Rect> eyes = EyeDetector.instance.detectEyes(image);

        Utilities.drawRect(eyes.first, image);
        Utilities.drawRect(eyes.second, image);

        Rect mouthRect = MouthDetector.instance.detectMouth(image);

        Utilities.drawRectAndStore(mouthRect, image, "faceRecognition.png");
    }
}
