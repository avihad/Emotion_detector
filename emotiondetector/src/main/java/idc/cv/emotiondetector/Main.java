package idc.cv.emotiondetector;

import idc.cv.emotiondetector.detectors.*;
import idc.cv.emotiondetector.utillities.Optional;
import idc.cv.emotiondetector.utillities.Pair;
import idc.cv.emotiondetector.utillities.Utilities;
import idc.cv.emotiondetector.utillities.VideoReader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;

import java.util.List;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        // Load the native library.
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Optional<Rect> neutralMouth = MouthDetector.instance.detectMouth(Utilities.readImage("/neutral.png"));
        Optional<Rect> smilingMouth = MouthDetector.instance.detectMouth(Utilities.readImage("/smile.png"));

        throwIfMouthIsNotFoundIn(neutralMouth, smilingMouth);

        System.out.println("neutral mouth is at: " + neutralMouth.get());
        System.out.println("smiling mouth is at: " + smilingMouth.get());

        double neutralFaceDeriv = interpolationOf(neutralMouth.get());
        double smilingFaceDeriv = interpolationOf(smilingMouth.get());

        int frameNum = 1;
        for (Rect mouth : MovieMouthTracker.getMouthPositionsAlongMovie("/aviad.avi"))
        {
            double mouthDeriv = interpolationOf(mouth);

            if (mouthDeriv / smilingFaceDeriv > 0.8)
            {
                System.out.println("Smile detected at: " + frameNum);
            }

            frameNum++;
        }

        //VideoReader.instance.storeAsFrames("/aviad.avi");
    }

    private static double interpolationOf(Rect mouth)
    {
        double leftX = (-1)*(mouth.width / 2);                 //x0
        double leftY = mouth.height;                                //f(x0)
        double centerX = 0;                                         //x1
        double centerY = 0;                                         //f(x1)
        double rightX = mouth.width / 2;//x2
        double rightY = mouth.height;                               //f(x2)

        // interpolation by lagrange: to find: a*x^2 + b*x + c

        double partA = leftY / ((leftX - centerX) * (leftX - rightX));
        double partB = centerY / ((centerX - leftX) * (centerX - rightX));
        double partC = rightY / ((rightX - leftX) * (rightX - centerX));

        double a = partA = partB + partC;
        double b = (partA * (centerX + rightX)) + (partB * (leftX + rightX)) + (partC * (leftX + centerX));
        double c = (partA * (centerX * rightX)) + (partB * (leftX * rightX)) + (partC * (leftX * centerX));

        System.out.println(a + "*x^2 + " + b + "*x + " + c);
        System.out.println("Derive is: " + (2*a));

        return 2*a;
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
