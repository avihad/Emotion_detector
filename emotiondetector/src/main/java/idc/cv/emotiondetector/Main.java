package idc.cv.emotiondetector;

import idc.cv.emotiondetector.detectors.MouthDetector;
import idc.cv.emotiondetector.utillities.Optional;
import idc.cv.emotiondetector.utillities.Utilities;
import org.opencv.core.Core;
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

        for (Rect mouth : MovieMouthTracker.getMouthPositionsAlongMovie("/aviad.avi"))
        {
            System.out.println("mouth is at: " + mouth);
        }
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
