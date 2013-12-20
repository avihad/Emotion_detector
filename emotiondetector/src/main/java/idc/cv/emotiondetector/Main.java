package idc.cv.emotiondetector;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        System.out.println("Hello, OpenCV");

        // Load the native library.
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Mat image = Utility.createImageMat("/images.jpg");
        Pair<Rect, Rect> detectedEyes = EyeDetector.instance.detectEyes(image);

        MatOfRect face = FacePartDetector.instance.detect(image, FacePartCascades.FACE.getCascadeClassifier());
        FacePartDetector.instance.write(face, image, "faceRecognition.png");
        System.out.println(face);
    }
}
