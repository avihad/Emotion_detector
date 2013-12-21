package idc.cv.emotiondetector.utillities;

import idc.cv.emotiondetector.detectors.EyesPairDetector;
import idc.cv.emotiondetector.detectors.MouthDetector;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.highgui.VideoCapture;

public enum VideoReader
{
    instance;

    public VideoCapture open(String videoFileName) throws Exception
    {
        VideoCapture videoCapture = new VideoCapture();

        videoCapture.open(Utilities.readResource(videoFileName));

        if (!videoCapture.isOpened())
        {
            throw new Exception("Couldn't read movie: " + videoFileName);
        }

        return videoCapture;
    }

    public void storeAsFrames(String videoFileName) throws Exception
    {
        VideoCapture videoCapture = open(videoFileName);

        Mat newFrame = new Mat();
        int frameNumber = 1;
        while (videoCapture.read(newFrame) && frameNumber < 100)
        {
            Optional<Rect> eyes = EyesPairDetector.instance.detectEyePair(newFrame);
            if (eyes.isPresent())
            {
                Utilities.drawRect(eyes.get(), newFrame);
            }
            Optional<Rect> mouth = MouthDetector.instance.detectMouth(newFrame);
            if (mouth.isPresent())
            {
                Utilities.drawRect(mouth.get(), newFrame);
            }

            Utilities.writeImageToFile("frame"+frameNumber+".png", newFrame);
            frameNumber++;
        }
    }
}
