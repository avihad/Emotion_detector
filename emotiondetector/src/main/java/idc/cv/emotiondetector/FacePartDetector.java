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

    public void detectAndWrite(Mat frame, CascadeClassifier cascadeClassifier, String outputFileName) throws UnsupportedEncodingException
    {

        MatOfRect detectedParts = detect(frame, cascadeClassifier);

        write(detectedParts, frame, outputFileName);
    }

    public void write(MatOfRect detectedParts, Mat image, String outputFileName)
    {

        // Draw a bounding box around each face.
        for (Rect rect : detectedParts.toArray())
        {
            Core.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
        }

        // Save the visualized detection.
        writeImageToFile(outputFileName, image);
    }

    public MatOfRect detect(Mat image, CascadeClassifier cascadeClassifier) throws UnsupportedEncodingException
    {
        MatOfRect detectedParts = new MatOfRect();

        cascadeClassifier.detectMultiScale(image, detectedParts);

        System.out.println(String.format("Detected %s parts", detectedParts.toArray().length));

        return detectedParts;
    }

    private void writeImageToFile(String fileName, Mat image)
    {
        System.out.println(String.format("Writing %s", fileName));
        Highgui.imwrite(fileName, image);
    }
}
