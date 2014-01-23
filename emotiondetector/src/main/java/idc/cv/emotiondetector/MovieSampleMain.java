package idc.cv.emotiondetector;

import idc.cv.emotiondetector.utillities.VideoReader;
import org.opencv.core.Core;

public class MovieSampleMain
{
    public static void main(String[] args) throws Exception
    {
        // Load the native library.
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        VideoReader.instance.storeAsFrames("/1.avi");
    }
}
