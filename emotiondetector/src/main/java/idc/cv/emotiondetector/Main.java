package idc.cv.emotiondetector;

import idc.cv.emotiondetector.detectors.MouthDetectorImproved;
import idc.cv.emotiondetector.entities.*;
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
import java.util.SortedMap;

import static org.opencv.imgproc.Imgproc.COLOR_RGB2GRAY;
import static org.opencv.imgproc.Imgproc.cvtColor;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        // Load the native library.
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        //analyzeSamples();

        //VideoReader.instance.storeAsFrames("/1.avi");

        analyzeMovie("/neutralInput.png", "/smileInput.png", "/1.avi", 0.002, 0.001);
    }

    private static void analyzeMovie(String neutralImagePath, String smileImagePath, String movieFilePath, double smileThreshold, double neutralThreshold) throws Exception
    {
        //Run the pulse application:
        SortedMap<Integer,Integer> pulseByFrame = PulseDetectorMain.detectPulse(movieFilePath);

        DetectedMovie movie = findSmilesAndGenerateXml(neutralImagePath, smileImagePath, movieFilePath, smileThreshold, neutralThreshold, pulseByFrame);

        Utilities.witeToXml(movie, "movieAnalysisResult.xml");
    }

    private static DetectedMovie findSmilesAndGenerateXml(String neutralImagePath, String smileImagePath, String movieFilePath, double smileThreshold, double neutralThreshold, SortedMap<Integer, Integer> pulseByFrame) throws Exception
    {
        Mat neutralImage = Utilities.readImage(neutralImagePath);
        Mat smileImage = Utilities.readImage(smileImagePath);

        Rect neutralMouth = MouthDetectorImproved.instance.detectIn(neutralImage).get();
        Rect smilingMouth = MouthDetectorImproved.instance.detectIn(smileImage).get();

        Utilities.writeImageToFile("neutralResult.jpg", neutralImage);
        Utilities.writeImageToFile("smileResult.jpg", smileImage);

        double neutralCurve = smileCurveOf(neutralImage, neutralMouth)[1];
        double smileCurve = smileCurveOf(smileImage, smilingMouth)[1];

        System.out.println("neutral curve: " + neutralCurve);
        System.out.println("smile curve: " + smileCurve);

        Mat movieFrame = new Mat();
        VideoCapture videoCapture = VideoReader.instance.open(movieFilePath);
        int frameNumber = 1;

        int numOfFramesOfSmile = 0;
        int numOfFramesOfMouthMovement = 0;

        DetectedMovie movie = new DetectedMovie();
        movie.setName(movieFilePath);


        while (videoCapture.read(movieFrame))
        {
            DetectedFrame detectedFrame = new DetectedFrame();
            detectedFrame.setFrameNumber(frameNumber);
            detectedFrame.setPulse(pulseByFrame.get(frameNumber));

            Optional<Rect> optionalMouth = MouthDetectorImproved.instance.detectIn(movieFrame);

            if (optionalMouth.isPresent())
            {
                DetectedMouth detectedMouth;

                Rect mouth = optionalMouth.get();
                double mouthCurve = smileCurveOf(movieFrame, mouth)[1];

                if (mouthCurve >= smileCurve || smileCurve - mouthCurve <= smileThreshold)
                {
                    System.out.println("Smile is detected at frame number: "+frameNumber);
                    numOfFramesOfSmile++;

                    detectedMouth = new DetectedMouth(mouth, Boolean.TRUE);
                }
                else
                {
                    detectedMouth = new DetectedMouth(mouth, Boolean.FALSE);

                    if (mouthCurve > ((neutralCurve + smileCurve) / 3) + neutralThreshold)
                    {
                        System.out.println("Mouth movement is detected at frame number: "+frameNumber);
                        numOfFramesOfMouthMovement++;
                    }
                }

                detectedFrame.addDetectedPart(detectedMouth);
            }
            frameNumber++;
            movie.addFrame(detectedFrame);

            if (frameNumber % 50 == 0)
            {
                System.out.println("Passed: " + frameNumber + " frames");
            }
        }

        System.out.println("Number of frames with a smile: " + numOfFramesOfSmile);
        System.out.println("Number of frames with mouth movement: " + numOfFramesOfMouthMovement);
        movie.setNumOfFramesWithASmile(numOfFramesOfSmile);

        return movie;
    }

    private static void analyzeSamples() throws Exception
    {
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

        double[] curve1 = smileCurveOf(image1, rect1);
        double[] curve2 = smileCurveOf(image2, rect2);
        double[] curve3 = smileCurveOf(image3, rect3);
        double[] curve4 = smileCurveOf(image4, rect4);
        double[] curve5 = smileCurveOf(image5, rect5);
        double[] curve6 = smileCurveOf(image6, rect6);

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
