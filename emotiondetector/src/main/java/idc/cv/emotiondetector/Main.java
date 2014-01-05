package idc.cv.emotiondetector;

import idc.cv.emotiondetector.detectors.MouthDetectorImproved;
import idc.cv.emotiondetector.pointsOfInterestDetection.BottomLipPointsDetector;
import idc.cv.emotiondetector.utillities.Optional;
import idc.cv.emotiondetector.utillities.ParabolicLinearRegression;
import idc.cv.emotiondetector.utillities.Utilities;
import idc.cv.emotiondetector.utillities.VideoReader;
import org.opencv.core.*;
import org.opencv.highgui.VideoCapture;

import java.util.Collection;
import java.util.List;

import static org.opencv.imgproc.Imgproc.*;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        // Load the native library.
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Mat neutralImage = Utilities.readImage("/nonSmile.jpg");
        Mat smileImage = Utilities.readImage("/womanSmiles.png");

        Rect neutralMouth = MouthDetectorImproved.instance.detectIn(neutralImage).get();
        Rect smilingMouth = MouthDetectorImproved.instance.detectIn(smileImage).get();

        double[] neutralCurve = smileCurveOf(neutralImage, neutralMouth);
        double[] smileCurve = smileCurveOf(smileImage, smilingMouth);


        Utilities.writeImageToFile("neutralResult.jpg", neutralImage);
        Utilities.writeImageToFile("smileResult.jpg", smileImage);

        System.out.println("neutral coefficient: " + neutralCurve[1]);
        System.out.println("smile coefficient: " + smileCurve[1]);

        //VideoReader.instance.storeAsFrames("/shiran.avi");
        /*Mat movieFrame = new Mat();
        VideoCapture videoCapture = VideoReader.instance.open("/shiran.avi");
        int frameNumber = 1;

        while (videoCapture.read(movieFrame))
        {
            Optional<Rect> optionalMouth = MouthDetectorImproved.instance.detectIn(movieFrame);

            if (optionalMouth.isPresent())
            {
                Rect mouth = optionalMouth.get();

                double[] parabolaCoefficients = smileCurveOf(movieFrame, mouth);

                if (parabolaCoefficients[1] >= 0.001)
                {
                    System.out.println("Smile is detected at frame number: "+frameNumber+ ", coefficient of x^2 is: " + parabolaCoefficients[1]);
                }

                if (frameNumber == 15)
                {
                    Utilities.drawRect(mouth, movieFrame);
                    Utilities.writeImageToFile("gray.png", movieFrame);
                }
            }
            frameNumber++;
        }*/
    }

    private static double[] smileCurveOf(Mat smileImage, Rect mouth)
    {
        cvtColor(smileImage, smileImage, COLOR_RGB2GRAY);

        //GaussianBlur(smileImage, smileImage, new Size(3, 3), 0, 0, BORDER_DEFAULT);

        Collection<Point> lowerLipPoints = BottomLipPointsDetector.findBottomLipPoints(smileImage, mouth);

        double[] parabolaCoefficients = ParabolicLinearRegression.linearRegressionOf(lowerLipPoints);

        //ParabolicLinearRegression.testResult(lowerLipPoints, parabolaCoefficients);

        return parabolaCoefficients;
    }
}
