package idc.cv.emotiondetector;

import java.io.UnsupportedEncodingException;

import org.opencv.core.*;

public enum MouthDetector
{
    instance;

    /**
     * Detecting mouth in image using @FacePartDetector to find candidates and
     * choosing the best one according to the candidate alignment in refer to
     * the eyes
     *
     * @throws UnsupportedEncodingException
     */
    public Rect detectMouth(Mat image) throws Exception
    {
        Pair<Rect, Rect> detectedEyes = EyeDetector.instance.detectEyes(image);

        MatOfRect suspectedMouths = FacePartDetector.instance.detect(image, FacePartCascade.MOUTH);

        int leftEyeLeftEdge = detectedEyes.first.x;
        int rightEyeRightEdge = detectedEyes.second.x + detectedEyes.second.width;

        Rect mouthBestCandidate = null;
        double threshold = Double.MAX_VALUE;

        for (Rect suspectedMouth : suspectedMouths.toArray())
        {
            if (aboveTheEyes(detectedEyes, suspectedMouth)) continue;

            int mouthLeftEdge = suspectedMouth.x;
            int mouthRightEdge = suspectedMouth.x + suspectedMouth.width;
            double averageVerticalLineOfEyes = (rightEyeRightEdge - leftEyeLeftEdge) / 2;
            double averageVerticalLineOfMouth = (mouthRightEdge - mouthLeftEdge) / 2;

            double differenceBetweenMouthAndEyes = Math.abs(averageVerticalLineOfEyes - averageVerticalLineOfMouth);

            if (differenceBetweenMouthAndEyes < threshold)
            {
                mouthBestCandidate = suspectedMouth;
                threshold = differenceBetweenMouthAndEyes;
            }
        }

        if (mouthBestCandidate == null)
        {
            throw new Exception("Mouth not found!");
        }

        return mouthBestCandidate;
    }

    private boolean aboveTheEyes(Pair<Rect, Rect> detectedEyes, Rect suspectedMouth)
    {
        return suspectedMouth.y <= detectedEyes.first.y;
    }
}
