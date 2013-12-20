package idc.cv.emotiondetector;

import java.io.UnsupportedEncodingException;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.objdetect.CascadeClassifier;

public enum FacePartDetector
{
    instance;

    public void detectAndDraw(Mat frame, FacePartCascade facePartCascade, String outputFileName) throws UnsupportedEncodingException
    {
        MatOfRect detectedParts = detect(frame, facePartCascade);

        Utilities.drawDetectedParts(detectedParts, frame, outputFileName);
    }

    public MatOfRect detect(Mat image, FacePartCascade facePartCascade) throws UnsupportedEncodingException
    {
        MatOfRect detectedParts = new MatOfRect();

        facePartCascade.getCascadeClassifier().detectMultiScale(image, detectedParts);

        System.out.println(String.format("Detected %s %sS", detectedParts.toArray().length, facePartCascade.name()));

        return detectedParts;
    }
}
