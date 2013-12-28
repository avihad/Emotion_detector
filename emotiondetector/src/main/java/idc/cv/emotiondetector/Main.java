package idc.cv.emotiondetector;

import idc.cv.emotiondetector.detectors.*;
import idc.cv.emotiondetector.utillities.*;
import org.opencv.core.*;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.opencv.imgproc.Imgproc.*;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        // Load the native library.
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Mat smileImage = Utilities.readImage("/womanSmile.jpg");

        Optional<Rect> smilingMouth = MouthDetector.instance.detectMouth(smileImage);

        throwIfMouthIsNotFoundIn(smilingMouth);

        Rect mouthRect = smilingMouth.get();
        Utilities.drawRect(mouthRect, smileImage);

        cvtColor(smileImage, smileImage, COLOR_RGB2GRAY);

        //GaussianBlur(smileImage, smileImage, new Size(7, 7), 1.5, 1.5);

        //Canny(smileImage, smileImage, 10, 20);

        Point middleLipCorner = findLowerLipPoint(smileImage, new Point(mouthRect.x + mouthRect.width / 2, mouthRect.y + mouthRect.height), 65, 65);
        Point leftLipCorner = findLowerLipPoint(smileImage, new Point(mouthRect.x + mouthRect.width / 3, mouthRect.y + mouthRect.height), 30, 40);
        Point rightLipCorner = findLowerLipPoint(smileImage, new Point(mouthRect.x + 2 * (mouthRect.width / 3), mouthRect.y + mouthRect.height), 30, 40);
        Point right2LipCorner = findLowerLipPoint(smileImage, new Point(mouthRect.x + mouthRect.width - 10, mouthRect.y + mouthRect.height), -100, 30);

        Utilities.drawConnectingLineBetween(smileImage, leftLipCorner, middleLipCorner, rightLipCorner, right2LipCorner);

        double[] parabolaCoefficients = ParabolicLinearRegression.linearRegressionOf
                (
                        normalizeByBase(leftLipCorner, middleLipCorner),
                        normalizeByBase(middleLipCorner, middleLipCorner),
                        normalizeByBase(rightLipCorner, middleLipCorner),
                        normalizeByBase(right2LipCorner, middleLipCorner)
                );

        double derive = 2*parabolaCoefficients[1];
        System.out.println("Derive is: " + derive);

        Utilities.writeImageToFile("gray.png", smileImage);
    }

    private static Point findLowerLipPoint(Mat image, Point startPoint, int offset, int yRange)
    {
        double minimumValue = 255;
        Point minimum = startPoint;

        for (int yAxisAdder = 0; yAxisAdder < yRange; yAxisAdder++)
        {
            if (image.get((int) startPoint.y + offset - yAxisAdder, (int) startPoint.x)[0] < minimumValue)
            {
                minimum = new Point(startPoint.x, startPoint.y + offset - yAxisAdder);
                minimumValue = image.get((int) minimum.y, (int) minimum.x)[0];
            }

            System.out.println(Arrays.toString(image.get((int) minimum.y, (int) minimum.x)));
        }
        return minimum;
    }

    private static Point normalizeByBase(Point point, Point base)
    {
        return new Point(point.x - base.x, base.y - point.y);
    }

    private static void throwIfMouthIsNotFoundIn(Optional<Rect> smilingMouth) throws Exception
    {
        if (!smilingMouth.isPresent())
        {
            throw new Exception("Couldn't find smiling mouth");
        }
    }

    private static void anExampleWithPreGivenPoints() throws UnsupportedEncodingException
    {
        Mat smileImage = Utilities.readImage("/smile.png");
        //smiling :
        ArrayList<Point> points = new ArrayList<Point>();
        Point base = new Point(673, 658);
        Point point1 = new Point(505, 519);
        points.add(normalizeByBase(point1, base));
        Point point2 = new Point(557, 612);
        points.add(normalizeByBase(point2, base));
        Point point3 = new Point(609, 654);
        points.add(normalizeByBase(point3, base));
        Point point4 = new Point(673, 658);
        points.add(normalizeByBase(point4, base));//base
        Point point5 = new Point(727, 643);
        points.add(normalizeByBase(point5, base));
        Point point6 = new Point(824, 524);
        points.add(normalizeByBase(point6, base));
        double smileDeriv = 2 * ParabolicLinearRegression.linearRegressionOf(points)[1];

        Utilities.drawLine(point1, point2, smileImage);
        Utilities.drawLine(point2, point3, smileImage);
        Utilities.drawLine(point3, point4, smileImage);
        Utilities.drawLine(point4, point5, smileImage);
        Utilities.drawLine(point5, point6, smileImage);
        Utilities.writeImageToFile("smileNew.png", smileImage);

        //neutral:
        Mat neutralImage = Utilities.readImage("/neutral.png");
        points = new ArrayList<Point>();
        Point base2 = new Point(663, 626);
        Point pointN1 = new Point(540, 581);
        points.add(normalizeByBase(pointN1, base2));
        Point pointN2 = new Point(619, 626);
        points.add(normalizeByBase(pointN2, base2));
        Point pointN3 = new Point(663, 626);        //base
        points.add(normalizeByBase(pointN3, base2));
        Point pointN4 = new Point(733, 612);
        points.add(normalizeByBase(pointN4, base2));
        Point pointN5 = new Point(753, 607);
        points.add(normalizeByBase(pointN5, base2));
        Point pointN6 = new Point(793, 569);
        points.add(normalizeByBase(pointN6, base2));
        double neutralDeriv = 2 * ParabolicLinearRegression.linearRegressionOf(points)[1];

        System.out.println("Deriv smile: " + smileDeriv + " neutral deriv: " + neutralDeriv + " ratio: " + smileDeriv / neutralDeriv);
        Utilities.drawLine(pointN1, pointN2, neutralImage);
        Utilities.drawLine(pointN2, pointN3, neutralImage);
        Utilities.drawLine(pointN3, pointN4, neutralImage);
        Utilities.drawLine(pointN4, pointN5, neutralImage);
        Utilities.drawLine(pointN5, pointN6, neutralImage);
        Utilities.writeImageToFile("neutralNew.png", neutralImage);
    }
}
