package idc.cv.emotiondetector.pointsOfInterestDetection;

import idc.cv.emotiondetector.utillities.Utilities;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class BottomLipPointsDetector
{
    private static final double colorDiffThreshold = -0.5;

    public static Collection<Point> findBottomLipPoints(Mat smileImage, Rect mouthRect)
    {
        Point middleBottomLip = findLocalMinimum(smileImage, new Point(mouthRect.x + mouthRect.width / 2, mouthRect.y + mouthRect.height), mouthRect.height / 3, mouthRect.height, mouthRect.height / 2, colorDiffThreshold);

        //left points from middle:
        List<Point> lowerLipPoints = findLowerLipLeftPoints(middleBottomLip, mouthRect, smileImage);
        //middle point
        lowerLipPoints.add(middleBottomLip);
        //right points from middle
        lowerLipPoints.addAll(findLowerLipRightPoints(middleBottomLip, mouthRect, smileImage));

        Utilities.drawCollectionLineOf(smileImage, lowerLipPoints);

        return normalizeCollectionByBase(lowerLipPoints, middleBottomLip);
    }

    private static List<Point> findLowerLipLeftPoints(Point bottomMiddle, Rect mouth, Mat image)
    {
        List<Point> lowerLipPoints = new ArrayList<Point>();

        for (double xIndex = bottomMiddle.x - 1; xIndex > mouth.x; xIndex -= 1)
        {
            lowerLipPoints.add(findLocalMinimum(image, new Point(xIndex, bottomMiddle.y), 0, mouth.height, mouth.height, colorDiffThreshold));
        }

        return lowerLipPoints;
    }

    private static List<Point> findLowerLipRightPoints(Point bottomMiddle, Rect mouth, Mat image)
    {
        List<Point> lowerLipPoints = new ArrayList<Point>();

        for (double xIndex = bottomMiddle.x + 1; xIndex <= mouth.x + mouth.width; xIndex += 1)
        {
            lowerLipPoints.add(findLocalMinimum(image, new Point(xIndex, bottomMiddle.y), 0, mouth.height, mouth.height, colorDiffThreshold));
        }

        return lowerLipPoints;
    }

    private static Point findLocalMinimum(Mat image, Point startPoint, int offset, int mouthHeight, int yCount, double colorDiffThreshold)
    {
        for (int yAxisAdder = 0; yAxisAdder < yCount; yAxisAdder++)
        {
            //System.out.println("Point: [" + ((int) startPoint.x) + "," + ((int) startPoint.y + offset - yAxisAdder) + "], " + "color: " + image.get((int) startPoint.y + offset - yAxisAdder, (int) startPoint.x)[0]);

            int imageX = (int) startPoint.x;
            int imageY = (int) startPoint.y + offset - yAxisAdder;
            double pixelColorValue = image.get(imageY, imageX)[0];

            double averageColorValue = averageOf(image, imageX, imageY, mouthHeight);
            double standardDeviation = standardDeviationOf(image, imageX, imageY, mouthHeight);

            if ((pixelColorValue - averageColorValue) / standardDeviation < colorDiffThreshold)
            {
                //System.out.println((pixelColorValue - averageColorValue) / standardDeviation);
                return new Point(startPoint.x, startPoint.y + offset - yAxisAdder);
            }
        }
        return startPoint;
    }

    private static double averageOf(Mat image, int x, int startY, int amount)
    {
        double summary = 0;
        for (int y = startY; y < startY + amount; y++)
        {
            summary += image.get(y, x)[0];
        }

        return summary / amount;
    }

    private static double standardDeviationOf(Mat image, int x, int startY, int amount)
    {
        double average = averageOf(image, x, startY, amount);
        double squaresSum = 0;

        for (int y = startY; y < startY + amount; y++)
        {
            double colorValue = image.get(y, x)[0];
            squaresSum += Math.pow(colorValue, 2);
        }

        return Math.sqrt
                (
                        (squaresSum - (amount * Math.pow(average, 2))) / (amount - 1)
                );
    }

    private static Point normalizeByBase(Point point, Point base)
    {
        return new Point(point.x - base.x, base.y - point.y);
    }

    private static Collection<Point> normalizeCollectionByBase(Collection<Point> points, Point base)
    {
        List<Point> normalizedPoints = new ArrayList<Point>();

        for (Point point : points)
        {
            normalizedPoints.add(normalizeByBase(point, base));
        }

        return normalizedPoints;
    }

}
