package idc.cv.emotiondetector.utillities;

import org.opencv.core.*;
import org.opencv.highgui.Highgui;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

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

    public static Mat readImage(String fileName) throws UnsupportedEncodingException
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

    public static void drawPoint(Point point, Mat image)
    {
        Core.line(image, point, new Point(point.x, point.y + 1), new Scalar(0, 255, 0), 5);
    }

    public static void drawLine(Point first, Point second, Mat image)
    {
        Core.line(image, first, second, new Scalar(0, 255, 0));
    }

    public static void writeImageToFile(String fileName, Mat image)
    {
        System.out.println(String.format("Writing %s", fileName));
        Highgui.imwrite(fileName, image);
    }

    public static void drawConnectingLineBetween(Mat image, Point... points)
    {
        for (Point point : points)
        {
            Utilities.drawPoint(point, image);
        }

        int i = 0;
        int j = 1;
        while(j < points.length)
        {
            Utilities.drawLine(points[i++], points[j++], image);
        }
    }
}
