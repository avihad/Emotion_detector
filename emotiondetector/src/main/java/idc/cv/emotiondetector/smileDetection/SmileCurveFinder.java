package idc.cv.emotiondetector.smileDetection;

import idc.cv.emotiondetector.pointsOfInterestDetection.BottomLipPointsDetector;
import idc.cv.emotiondetector.utillities.ParabolicLinearRegression;
import idc.cv.emotiondetector.utillities.Utilities;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.opencv.imgproc.Imgproc.COLOR_RGB2GRAY;
import static org.opencv.imgproc.Imgproc.cvtColor;

public class SmileCurveFinder
{
    public static double[] smileCurveOf(Mat smileImage, Rect mouth)
    {
        Mat graySmileImage = new Mat();

        cvtColor(smileImage, graySmileImage, COLOR_RGB2GRAY);

        Point middleBottomLip = BottomLipPointsDetector.findMiddleBottomLip(graySmileImage, mouth);

        List<Point> lowerLipPoints = BottomLipPointsDetector.findBottomLipPoints(graySmileImage, mouth, middleBottomLip);

        Utilities.drawCollectionLineOf(smileImage, lowerLipPoints);

        Collection<Point> lipPointsNormalized = normalizeCollectionByBase(lowerLipPoints, middleBottomLip);

        double[] parabolaCoefficients = ParabolicLinearRegression.linearRegressionOf(lipPointsNormalized);

        // ParabolicLinearRegression.testResult(lowerLipPoints,
        // parabolaCoefficients);

        return parabolaCoefficients;
    }

    private static Collection<Point> normalizeCollectionByBase(Collection<Point> points, Point base)
    {
        List<Point> normalizedPoints = new ArrayList<>();

        for (Point point : points)
        {
            normalizedPoints.add(normalizeByBase(point, base));
        }

        return normalizedPoints;
    }

    private static Point normalizeByBase(Point point, Point base)
    {
        return new Point(point.x - base.x, base.y - point.y);
    }
}
