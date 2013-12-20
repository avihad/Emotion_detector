package idc.cv.emotiondetector;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

public class Utility
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
            return null;
        }
    }

    public static Mat createImageMat(String fileName) throws UnsupportedEncodingException
    {
        return Highgui.imread(Utility.readResource(fileName));
    }
}
