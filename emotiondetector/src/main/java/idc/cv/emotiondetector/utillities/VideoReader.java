package idc.cv.emotiondetector.utillities;

import org.opencv.core.Mat;
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
            Utilities.writeImageToFile("frame"+frameNumber+".png", newFrame);
            frameNumber++;
        }
    }
}
