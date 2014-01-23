import idc.cv.emotiondetector.detectors.MouthDetectorImproved;
import idc.cv.emotiondetector.smileDetection.SmileCurveFinder;
import idc.cv.emotiondetector.utillities.Utilities;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;

public class SamplesMain
{
    public static void main(String[] args) throws Exception
    {
        // Load the native library.
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        analyzeSamples();
    }

    private static void analyzeSamples() throws Exception {
        Mat image1 = Utilities.readImage("/neutralInput.png");
        Mat image2 = Utilities.readImage("/2.png");
        Mat image3 = Utilities.readImage("/3.jpg");
        Mat image4 = Utilities.readImage("/3a.jpg");
        Mat image5 = Utilities.readImage("/4.jpg");
        Mat image6 = Utilities.readImage("/5.jpg");

        Rect rect1 = MouthDetectorImproved.instance.detectIn(image1).get();
        Rect rect2 = MouthDetectorImproved.instance.detectIn(image2).get();
        Rect rect3 = MouthDetectorImproved.instance.detectIn(image3).get();
        Rect rect4 = MouthDetectorImproved.instance.detectIn(image4).get();
        Rect rect5 = MouthDetectorImproved.instance.detectIn(image5).get();
        Rect rect6 = MouthDetectorImproved.instance.detectIn(image6).get();

        double[] curve1 = SmileCurveFinder.smileCurveOf(image1, rect1);
        double[] curve2 = SmileCurveFinder.smileCurveOf(image2, rect2);
        double[] curve3 = SmileCurveFinder.smileCurveOf(image3, rect3);
        double[] curve4 = SmileCurveFinder.smileCurveOf(image4, rect4);
        double[] curve5 = SmileCurveFinder.smileCurveOf(image5, rect5);
        double[] curve6 = SmileCurveFinder.smileCurveOf(image6, rect6);

        Utilities.writeImageToFile("1Result.jpg", image1);
        Utilities.writeImageToFile("2Result.jpg", image2);
        Utilities.writeImageToFile("3Result.jpg", image3);
        Utilities.writeImageToFile("4Result.jpg", image4);
        Utilities.writeImageToFile("5Result.jpg", image5);
        Utilities.writeImageToFile("6Result.jpg", image6);

        System.out.println("1 coefficient: " + curve1[1]);
        System.out.println("2 coefficient: " + curve2[1]);
        System.out.println("3 coefficient: " + curve3[1]);
        System.out.println("4 coefficient: " + curve4[1]);
        System.out.println("5 coefficient: " + curve5[1]);
        System.out.println("6 coefficient: " + curve6[1]);
    }
}
