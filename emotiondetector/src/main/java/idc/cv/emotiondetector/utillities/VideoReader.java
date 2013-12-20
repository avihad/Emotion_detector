package idc.cv.emotiondetector.utillities;

import org.opencv.highgui.VideoCapture;

public enum VideoReader
{
    instance;

    public VideoCapture open(String videoFileName) throws Exception
    {
        VideoCapture videoCapture = new VideoCapture();

        videoCapture.open(videoFileName);

        if (!videoCapture.isOpened())
        {
            throw new Exception("Couldn't read movie: " + videoFileName);
        }

        return videoCapture;
    }
}
