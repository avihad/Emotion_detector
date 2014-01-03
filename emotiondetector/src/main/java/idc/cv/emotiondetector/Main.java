package idc.cv.emotiondetector;

import idc.cv.emotiondetector.detectors.MouthDetectorImproved;
import idc.cv.emotiondetector.utillities.Optional;
import idc.cv.emotiondetector.utillities.ParabolicLinearRegression;
import idc.cv.emotiondetector.utillities.Utilities;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.opencv.imgproc.Imgproc.COLOR_RGB2GRAY;
import static org.opencv.imgproc.Imgproc.cvtColor;

public class Main
{
    private static final int xGapBetweenPoints = 10;
    private static final double yGrowthRate = 7;

    public static void main(String[] args) throws Exception
    {
        // Load the native library.
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Mat smileImage = Utilities.readImage("/womanSmiles.png");

        Optional<Rect> smilingMouth = MouthDetectorImproved.instance.detectMouth(smileImage);

        Rect mouthRect = smilingMouth.get();

        cvtColor(smileImage, smileImage, COLOR_RGB2GRAY);

        Collection<Point> lowerLipPoints = findPointsAlongBottomLipEdge(smileImage, mouthRect);

        double[] parabolaCoefficients = ParabolicLinearRegression.linearRegressionOf(lowerLipPoints);

        System.out.println("Coefficient of x^2 is: " + parabolaCoefficients[1]);

        ParabolicLinearRegression.testResult(lowerLipPoints, parabolaCoefficients);

        Utilities.writeImageToFile("gray.png", smileImage);
    }

    private static Collection<Point> findPointsAlongBottomLipEdge(Mat smileImage, Rect mouthRect)
    {
        Point middleLipCorner = findLocalMinimum(smileImage, new Point(mouthRect.x + mouthRect.width / 2, mouthRect.y + mouthRect.height), 65, 65);

        //left points from middle:
        List<Point> lowerLipPoints = findLowerLipLeftPoints(middleLipCorner, mouthRect, smileImage);
        //middle point
        lowerLipPoints.add(middleLipCorner);
        //right points from middle
        lowerLipPoints.addAll(findLowerLipRightPoints(middleLipCorner, mouthRect, smileImage));

        Utilities.drawCollectionLineOf(smileImage, lowerLipPoints);

        return normalizeCollectionByBase(lowerLipPoints, middleLipCorner);
    }

    private static List<Point> findLowerLipLeftPoints(Point bottomMiddle, Rect mouth, Mat image)
    {
        List<Point> lowerLipPoints = new ArrayList<Point>();

        double heightLookupRange = 0;
        for (double xIndex = bottomMiddle.x - xGapBetweenPoints; xIndex > mouth.x; xIndex -= xGapBetweenPoints)
        {
            lowerLipPoints.add(findLocalMinimum(image, new Point(xIndex, bottomMiddle.y), 0, (int)heightLookupRange));
            heightLookupRange += yGrowthRate;
        }

        return lowerLipPoints;
    }

    private static List<Point> findLowerLipRightPoints(Point bottomMiddle, Rect mouth, Mat image)
    {
        List<Point> lowerLipPoints = new ArrayList<Point>();

        double heightLookupRange = 0;
        for (double xIndex = bottomMiddle.x + xGapBetweenPoints; xIndex <= mouth.x + mouth.width; xIndex += xGapBetweenPoints)
        {
            lowerLipPoints.add(findLocalMinimum(image, new Point(xIndex, bottomMiddle.y), 0, (int)heightLookupRange));
            heightLookupRange += yGrowthRate;
        }

        return lowerLipPoints;
    }

    private static Point findLocalMinimum(Mat image, Point startPoint, int offset, int numOfPixelsToGoUp)
    {
        double minimumValue = 255;
        Point minimum = startPoint;

        for (int yAxisAdder = 0; yAxisAdder < numOfPixelsToGoUp; yAxisAdder++)
        {
            if (image.get((int) startPoint.y + offset - yAxisAdder, (int) startPoint.x)[0] < minimumValue)
            {
                minimum = new Point(startPoint.x, startPoint.y + offset - yAxisAdder);
                minimumValue = image.get((int) minimum.y, (int) minimum.x)[0];
            }
        }
        return minimum;
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
