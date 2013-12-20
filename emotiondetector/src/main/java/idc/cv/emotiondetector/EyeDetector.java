package idc.cv.emotiondetector;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Size;

public enum EyeDetector
{
    instance;

    /**
     * Returns the best pair out of all suspected rectangles as yielded by the haar_cascade
     * Result contains  at first - the left eye
     *                 the second - the right eye
     * */
    public Pair<Rect, Rect> detectEyes(Mat image) throws Exception
    {
        MatOfRect eyes = FacePartDetector.instance.detect(image, FacePartCascade.EYE);

        double minimalHeightDifference = Double.MAX_VALUE;
        double minimalWidthDifference = Double.MAX_VALUE;

        Pair<Rect, Rect> bestChoice = null;

        for (Rect first : eyes.toArray())
        {
            for (Rect second : eyes.toArray())
            {
                int yPointDifference = Math.abs((second.y - first.y));
                int widthDifference = Math.abs((second.width - first.width));

                if (!first.equals(second) && (yPointDifference < minimalHeightDifference || widthDifference < minimalWidthDifference))
                {
                    bestChoice = Pair.of(first, second);
                    minimalHeightDifference = yPointDifference;
                    minimalWidthDifference = widthDifference;
                }
            }
        }

        if (bestChoice.first.x > bestChoice.second.x)
        {
            return Pair.of(bestChoice.second, bestChoice.first);
        }

        return bestChoice;
    }
}
