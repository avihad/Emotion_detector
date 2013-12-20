package idc.cv.emotiondetector;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;

public enum EyeDetector
{
    instance;

    public Pair<Rect, Rect> detectEyes(Mat image) throws Exception
    {
        MatOfRect eyes = FacePartDetector.instance.detect(image, FacePartCascades.EYE.getCascadeClassifier());

        if (eyes.toArray().length != 2)
        {
            throw new Exception("Found more than two eyes!!");
        }

        Rect first = eyes.toArray()[0];
        Rect second = eyes.toArray()[1];

        if (first.x > second.x)
        {

            return Pair.of(second, first);
        } else
        {
            return Pair.of(first, second);
        }
    }
}
