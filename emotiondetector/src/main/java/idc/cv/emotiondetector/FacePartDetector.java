package idc.cv.emotiondetector;

import java.io.UnsupportedEncodingException;

import idc.cv.emotiondetector.utillities.Utilities;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;

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
