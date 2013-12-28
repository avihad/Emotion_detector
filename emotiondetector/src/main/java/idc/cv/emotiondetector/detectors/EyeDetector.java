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

        double minimalAreasDifference = Double.MAX_VALUE;

        Optional<Pair<Rect, Rect>> bestChoice = Optional.absent();

        for (Rect first : eyes.toArray())
        {
            for (Rect second : eyes.toArray())
            {
                double areasDifference = Math.abs((second.area() - first.area()));

                if (positionsAreValid(first, second, image) && (areasDifference < minimalAreasDifference))
                {
                    bestChoice = Optional.of(Pair.of(first, second));
                    minimalAreasDifference = areasDifference;
                }
            }
        }

        if (bestChoice.isPresent() && bestChoice.get().first.x > bestChoice.get().second.x)
        {
            return Optional.of(Pair.of(bestChoice.get().second, bestChoice.get().first));
        }

        return bestChoice;
    }

    private boolean positionsAreValid(Rect first, Rect second, Mat image)
    {
        return notOverlappingAtX(first, second) &&
                almostAtTheSameHeight(first, second) &&
                first.y < image.size().height/2;
    }

    private boolean notOverlappingAtX(Rect first, Rect second)
    {
        return (first.x+first.width < second.x || second.x+second.width < first.x);
    }

    private boolean almostAtTheSameHeight(Rect first, Rect second)
    {
        return (Math.abs(first.y-second.y) < 10);
    }
}
