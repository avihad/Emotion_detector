package idc.cv.emotiondetector;

import idc.cv.emotiondetector.detectors.*;
import idc.cv.emotiondetector.utillities.*;
import org.apache.commons.math.stat.regression.OLSMultipleLinearRegression;
import org.opencv.core.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.opencv.imgproc.Imgproc.*;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        // Load the native library.
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Optional<Rect> neutralMouth = MouthDetector.instance.detectMouth(Utilities.readImage("/neutral.png"));
        Mat smileImage = Utilities.readImage("/smile.png");
        Optional<Rect> smilingMouth = MouthDetector.instance.detectMouth(smileImage);

        throwIfMouthIsNotFoundIn(neutralMouth, smilingMouth);

        Rect mouthRect = smilingMouth.get();
        /*
        System.out.println("neutral mouth is at: " + neutralMouth.get());
        System.out.println("smiling mouth is at: " + smilingMouth.get());



        cvtColor(smileImage, smileImage, COLOR_RGB2BGR);

        GaussianBlur(smileImage, smileImage, new Size(7, 7), 1.5, 1.5);

        //Canny(smileImage, smileImage, 10, 20);

        double initRed = smileImage.get(mouthRect.y + mouthRect.height + 100, mouthRect.x + mouthRect.width / 2)[0];

        for (int yPivot = 100; yPivot > 0; yPivot--)
        {
            //smileImage.put(mouthRect.y + mouthRect.height + yPivot, mouthRect.x + mouthRect.width / 2, 80, 67, 111);
            //smileImage.put(mouthRect.y + mouthRect.height + yPivot, mouthRect.x + mouthRect.width / 2, 113.0, 116.0, 139.0);
            if (initRed - smileImage.get(mouthRect.y + mouthRect.height + yPivot, mouthRect.x + mouthRect.width / 2)[0] > 30)
            {
                Utilities.drawLine(new Point(mouthRect.y + mouthRect.height + yPivot, mouthRect.x + mouthRect.width / 2), new Point(mouthRect.y + mouthRect.height + yPivot, mouthRect.x + mouthRect.width / 2), smileImage);
            }

           // System.out.println(Arrays.toString(smileImage.get(mouthRect.y + mouthRect.height + yPivot, mouthRect.x + mouthRect.width / 2)));
        }

        double initRed2 = smileImage.get(mouthRect.y + mouthRect.height, mouthRect.x + mouthRect.width / 3)[0];
        for (int yPivot = 0; yPivot > -50; yPivot--)
        {
            //smileImage.put(mouthRect.y + mouthRect.height + yPivot, mouthRect.x + mouthRect.width / 2, 80, 67, 111);
            //smileImage.put(mouthRect.y + mouthRect.height + yPivot, mouthRect.x + mouthRect.width / 2, 113.0, 116.0, 139.0);
            if (initRed2 - smileImage.get(mouthRect.y + mouthRect.height + yPivot, mouthRect.x + mouthRect.width / 3)[0] > 30)
            {
                Utilities.drawLine(new Point(mouthRect.y + mouthRect.height + yPivot, mouthRect.x + mouthRect.width / 3), new Point(mouthRect.y + mouthRect.height + yPivot, mouthRect.x + mouthRect.width / 3), smileImage);
            }

            // System.out.println(Arrays.toString(smileImage.get(mouthRect.y + mouthRect.height + yPivot, mouthRect.x + mouthRect.width / 2)));
        }

        double initRed3 = smileImage.get(mouthRect.y + mouthRect.height, mouthRect.x + 2*(mouthRect.width / 3))[0];
        for (int yPivot = 0; yPivot > -50; yPivot--)
        {
            //smileImage.put(mouthRect.y + mouthRect.height + yPivot, mouthRect.x + mouthRect.width / 2, 80, 67, 111);
            //smileImage.put(mouthRect.y + mouthRect.height + yPivot, mouthRect.x + mouthRect.width / 2, 113.0, 116.0, 139.0);
            if (initRed3 - smileImage.get(mouthRect.y + mouthRect.height + yPivot, mouthRect.x + 2*(mouthRect.width / 3))[0] > 30)
            {
                Utilities.drawLine(new Point(mouthRect.y + mouthRect.height + yPivot, mouthRect.x + 2*(mouthRect.width / 3)), new Point(mouthRect.y + mouthRect.height + yPivot, mouthRect.x + 2*(mouthRect.width / 3)), smileImage);
            }

            // System.out.println(Arrays.toString(smileImage.get(mouthRect.y + mouthRect.height + yPivot, mouthRect.x + mouthRect.width / 2)));
        }
       /* Utilities.drawLine
                (
                        new Point(mouthRect.x + mouthRect.width / 2, mouthRect.y + mouthRect.height),
                        new Point(mouthRect.x + mouthRect.width / 2, mouthRect.y + mouthRect.height + 100),
                        smileImage
                );
        Utilities.writeImageToFile("problem.png", smileImage);

        double neutralFaceDeriv = interpolationOf(neutralMouth.get());
        double smilingFaceDeriv = interpolationOf(smilingMouth.get());

        int frameNum = 1;
        for (Rect mouth : MovieMouthTracker.getMouthPositionsAlongMovieWithIndices("/aviad.avi").values())
        {
            double mouthDeriv = interpolationOf(mouth);

            if (mouthDeriv / smilingFaceDeriv > 0.8)
            {
                System.out.println("Smile detected at: " + frameNum);
            }

            frameNum++;
        }

        */
        ArrayList<Point> points = new ArrayList<Point>();

        Point base = new Point((mouthRect.x + mouthRect.width / 2), mouthRect.y + mouthRect.height);

        points.add(new Point(0, 0));
        Point point1 = normalizeByBase(new Point(mouthRect.x, mouthRect.y), base);
        Point point2 = normalizeByBase(new Point(mouthRect.x + mouthRect.width / 3, mouthRect.y + mouthRect.height), base);
        Point point3 = normalizeByBase(new Point(mouthRect.x + mouthRect.width, mouthRect.y), base);

        Utilities.drawLine(new Point(mouthRect.x, mouthRect.y), new Point(mouthRect.x + mouthRect.width / 3, mouthRect.y + mouthRect.height), smileImage);
        points.add(point1);
        Utilities.drawLine(new Point(mouthRect.x + mouthRect.width / 3, mouthRect.y + mouthRect.height), new Point(mouthRect.x + mouthRect.width, mouthRect.y), smileImage);
        points.add(point2);
        points.add(point3);

        ParabolicLinearRegression.linearRegressionOf(points);

        Utilities.writeImageToFile("problem.png", smileImage);
    }

    private static Point normalizeByBase(Point point, Point base)
    {
        return new Point(point.x - base.x, base.y - point.y);
    }



    private static void throwIfMouthIsNotFoundIn(Optional<Rect> neutralMouth, Optional<Rect> smilingMouth) throws Exception
    {
        if (!neutralMouth.isPresent())
        {
            throw new Exception("Couldn't find neutral mouth");
        }

        if (!smilingMouth.isPresent())
        {
            throw new Exception("Couldn't find smiling mouth");
        }
    }
}
