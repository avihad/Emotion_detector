package idc.cv.emotiondetector.detectors;

import idc.cv.emotiondetector.utillities.Optional;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;

public enum EyesPairDetector
{
    instance;

    public Optional<Rect> detectEyePair(Mat image) throws Exception
    {
        MatOfRect suspectedEyePairs = FacePartDetector.instance.detect(image, FacePartCascade.EYE_PAIR);

        if (suspectedEyePairs.toArray().length < 1)
        {
            return Optional.absent();
        }

        Optional<Rect> bestChoice = Optional.absent();
        double maxWidth = 0;

        for (Rect suspectedEyePair : suspectedEyePairs.toArray())
        {
            if (suspectedEyePair.width > maxWidth)
            {
                bestChoice = Optional.of(suspectedEyePair);
                maxWidth = suspectedEyePair.width;
            }
        }

        return bestChoice;

    }
}
