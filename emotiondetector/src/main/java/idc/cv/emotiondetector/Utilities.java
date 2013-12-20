package idc.cv.emotiondetector;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.opencv.core.*;
import org.opencv.highgui.Highgui;

public class Utilities
{
    public static String readResource(String resourceName)
    {
        String resource = Object.class.getResource(resourceName).getPath();

        // this will replace %20 with spaces
        if (resource.startsWith("/", 0))
        {
            resource = resource.replaceFirst("/", "");
        }

        try
        {
            return URLDecoder.decode(resource, "UTF-8");
        } 
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        
        return null;
    }

    public static Mat createImageMat(String fileName) throws UnsupportedEncodingException
    {
        return Highgui.imread(Utilities.readResource(fileName));
    }

    public static void drawDetectedParts(MatOfRect detectedParts, Mat image, String outputFileName)
    {
        // Draw a bounding box around each face.
        for (Rect rect : detectedParts.toArray())
        {
            drawRect(rect, image);
        }

        writeImageToFile(outputFileName, image);
    }

    public static void drawRectAndStore(Rect rect, Mat image, String outputFileName)
    {
        drawRect(rect, image);

        writeImageToFile(outputFileName, image);
    }

    public static void drawRect(Rect rect, Mat image)
    {
        Core.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
    }

    private static void writeImageToFile(String fileName, Mat image)
    {
        System.out.println(String.format("Writing %s", fileName));
        Highgui.imwrite(fileName, image);
    }
}
