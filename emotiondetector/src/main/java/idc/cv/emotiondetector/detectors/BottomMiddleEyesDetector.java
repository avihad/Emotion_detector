package idc.cv.emotiondetector.detectors;

import idc.cv.emotiondetector.utillities.Optional;
import idc.cv.emotiondetector.utillities.Pair;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;

public enum BottomMiddleEyesDetector
{
    instance;

    public Point getPointOfBottomMiddleEyes(Mat image) throws Exception
    {
        Optional<Rect> optionalEyesPair = EyesPairDetector.instance.detectEyePair(image);

        if (optionalEyesPair.isPresent())
        {
           return new Point((optionalEyesPair.get().x + optionalEyesPair.get().width/2), optionalEyesPair.get().y + optionalEyesPair.get().height);
        }

        Optional<Pair<Rect, Rect>> eyesSeparately = EyeDetector.instance.detectEyes(image);

        if (eyesSeparately.isPresent())
        {
            int leftEyeLeftEdge = eyesSeparately.get().first.x;
            int rightEyeRightEdge = eyesSeparately.get().second.x + eyesSeparately.get().second.width;

            return new Point((rightEyeRightEdge - leftEyeLeftEdge) / 2, eyesSeparately.get().first.y + eyesSeparately.get().first.height);
        }

        return new Point(image.size().width / 2, image.size().height / 3);
    }
}
