package idc.cv.emotiondetector;

import org.opencv.core.*;
import org.opencv.highgui.Highgui;
import org.opencv.objdetect.CascadeClassifier;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class FaceRecognizer
{
    public void recognize() throws UnsupportedEncodingException
    {
        System.out.println("\nRunning DetectFaceDemo");

        CascadeClassifier faceDetector = createCascadeClassifier();

        Mat image = createImageMat();

        // Detect faces in the image.
        // MatOfRect is a special container class for Rect.
        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(image, faceDetections);

        System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));

        // Draw a bounding box around each face.
        for (Rect rect : faceDetections.toArray())
        {
            Core.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
        }

        // Save the visualized detection.
        writeImageToFile("faceDetection.png", image);
    }

    /*
        Create a face detector from the cascade file in the resources directory.
     */
    private CascadeClassifier createCascadeClassifier() throws UnsupportedEncodingException
    {
        return new CascadeClassifier(readResource("/lbpcascade_frontalface.xml"));
    }

    private Mat createImageMat() throws UnsupportedEncodingException
    {
        return Highgui.imread(readResource("/lena.png"));
    }

    private String readResource(String resourceName) throws UnsupportedEncodingException
    {
        String resource = getClass().getResource(resourceName).getPath();

        if (resource.startsWith("/", 0))
        {
            resource=resource.replaceFirst("/", "");
        }

        return URLDecoder.decode(resource, "UTF-8"); //this will replace %20 with spaces
    }

    private void writeImageToFile(String fileName, Mat image)
    {
        System.out.println(String.format("Writing %s", fileName));
        Highgui.imwrite(fileName, image);
    }
}
