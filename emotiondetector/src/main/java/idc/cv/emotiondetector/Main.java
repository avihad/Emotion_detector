package idc.cv.emotiondetector;

import org.opencv.core.Core;

import java.io.UnsupportedEncodingException;

public class Main
{
    public static void main(String[] args) throws UnsupportedEncodingException
    {
        System.out.println("Hello, OpenCV");

        // Load the native library.
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        new FaceRecognizer().recognize();
    }
}

