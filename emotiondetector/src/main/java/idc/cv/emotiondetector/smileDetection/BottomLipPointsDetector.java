package idc.cv.emotiondetector.smileDetection;

import idc.cv.emotiondetector.utillities.Utilities;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BottomLipPointsDetector
{
    public static Collection<Point> findBottomLipPoints(Mat smileImage, Rect mouthRect)
    {
        Point middleBottomLip = findLocalMinimum(smileImage, new Point(mouthRect.x + mouthRect.width / 2, mouthRect.y + mouthRect.height), 30, mouthRect.height);

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
            lowerLipPoints.add(findLocalMinimum(image, new Point(xIndex, bottomMiddle.y), 0, mouth.height));
        }

        return lowerLipPoints;
    }

    private static List<Point> findLowerLipRightPoints(Point bottomMiddle, Rect mouth, Mat image)
    {
        List<Point> lowerLipPoints = new ArrayList<Point>();

        for (double xIndex = bottomMiddle.x + 1; xIndex <= mouth.x + mouth.width; xIndex += 1)
        {
            lowerLipPoints.add(findLocalMinimum(image, new Point(xIndex, bottomMiddle.y), 0, mouth.height));
        }

        return lowerLipPoints;
    }

    private static Point findLocalMinimum(Mat image, Point startPoint, int offset, int mouthHeight)
    {
        for (int yAxisAdder = 0; startPoint.y + offset - yAxisAdder > 0 ; yAxisAdder++)
        {
            //System.out.println("Point: ["+((int) startPoint.x)+","+((int) startPoint.y + offset - yAxisAdder)+"], " + "color: " + image.get((int) startPoint.y + offset - yAxisAdder, (int) startPoint.x)[0]);

            int imageX = (int) startPoint.x;
            int imageY = (int) startPoint.y + offset - yAxisAdder;
            double pixelColorValue = image.get(imageY, imageX)[0];

            boolean suspectedToBeLocalMinimum = true;

            for (int y = 1; y <= mouthHeight && suspectedToBeLocalMinimum ; y++)
            {
                suspectedToBeLocalMinimum = image.get(imageY + y, imageX)[0] > pixelColorValue;
            }

            if (suspectedToBeLocalMinimum)
            {
                return new Point(startPoint.x, startPoint.y + offset - yAxisAdder);
            }
        }

        //System.out.println();
        return startPoint;
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
