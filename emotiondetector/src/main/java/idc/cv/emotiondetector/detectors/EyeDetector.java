package idc.cv.emotiondetector.detectors;

import idc.cv.emotiondetector.utillities.Optional;
import idc.cv.emotiondetector.utillities.Pair;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;

public enum EyeDetector
{
    instance;

    /**
     * Returns the best pair out of all suspected rectangles as yielded by the haar_cascade
     * Result contains  at first - the left eye
     *                 the second - the right eye
     * */
    public Optional<Pair<Rect, Rect>> detectEyes(Mat image) throws Exception
    {
        MatOfRect eyes = FacePartDetector.instance.detect(image, FacePartCascade.EYE);

        double minimalHeightDifference = Double.MAX_VALUE;
        double minimalWidthDifference = Double.MAX_VALUE;

        Optional<Pair<Rect, Rect>> bestChoice = Optional.absent();

        for (Rect first : eyes.toArray())
        {
            for (Rect second : eyes.toArray())
            {
                int yPointDifference = Math.abs((second.y - first.y));
                int widthDifference = Math.abs((second.width - first.width));

                if (!first.equals(second) && (yPointDifference < minimalHeightDifference || widthDifference < minimalWidthDifference))
                {
                    bestChoice = Optional.of(Pair.of(first, second));
                    minimalHeightDifference = yPointDifference;
                    minimalWidthDifference = widthDifference;
                }
            }
        }

        if (bestChoice.isPresent() && bestChoice.get().first.x > bestChoice.get().second.x)
        {
            return Optional.of(Pair.of(bestChoice.get().second, bestChoice.get().first));
        }

        return bestChoice;
    }
}
