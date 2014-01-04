package idc.cv.emotiondetector;

import idc.cv.emotiondetector.detectors.MouthDetectorImproved;
import idc.cv.emotiondetector.pointsOfInterestDetection.BottomLipPointsDetector;
import idc.cv.emotiondetector.utillities.Optional;
import idc.cv.emotiondetector.utillities.ParabolicLinearRegression;
import idc.cv.emotiondetector.utillities.Utilities;
import idc.cv.emotiondetector.utillities.VideoReader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.highgui.VideoCapture;

import java.util.Collection;

import static org.opencv.imgproc.Imgproc.COLOR_RGB2GRAY;
import static org.opencv.imgproc.Imgproc.cvtColor;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        // Load the native library.
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        //Mat smileImage = Utilities.readImage("/womanSmiles.png");
        //Mat smileImage = Utilities.readImage("/nonSmile.jpg");

        //VideoReader.instance.storeAsFrames("/avihad2.avi");
        Mat movieFrame = new Mat();
        VideoCapture videoCapture = VideoReader.instance.open("/avihad2.avi");

        while (videoCapture.read(movieFrame))
        {
            Optional<Rect> optionalMouth = MouthDetectorImproved.instance.detectIn(movieFrame);

            if (optionalMouth.isPresent())
            {
                Rect mouth = optionalMouth.get();

                double[] parabolaCoefficients = smileCurveOf(movieFrame, mouth);

                System.out.println("Coefficient of x^2 is: " + parabolaCoefficients[1]);

                Utilities.writeImageToFile("gray.png", movieFrame);
            }
        }
    }

    private static double[] smileCurveOf(Mat smileImage, Rect mouth)
    {
        cvtColor(smileImage, smileImage, COLOR_RGB2GRAY);

        Collection<Point> lowerLipPoints = BottomLipPointsDetector.findBottomLipPoints(smileImage, mouth);

        double[] parabolaCoefficients = ParabolicLinearRegression.linearRegressionOf(lowerLipPoints);

        ParabolicLinearRegression.testResult(lowerLipPoints, parabolaCoefficients);

        return parabolaCoefficients;
    }
}
