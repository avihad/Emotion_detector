package idc.cv.emotiondetector.detectors;

import java.io.UnsupportedEncodingException;

import idc.cv.emotiondetector.utillities.Optional;
import idc.cv.emotiondetector.utillities.Pair;
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
    public Optional<Rect> detectMouth(Mat image) throws Exception
    {
        Point eyesBottomMiddlePoint = BottomMiddleEyesDetector.instance.getPointOfBottomMiddleEyes(image);

        MatOfRect suspectedMouths = FacePartDetector.instance.detect(image, FacePartCascade.MOUTH);

        Optional<Rect> mouthBestCandidate = Optional.absent();
        double threshold = Double.MAX_VALUE;

        for (Rect suspectedMouth : suspectedMouths.toArray())
        {
            if (isMouthReasonablyBelowTheEyes(eyesBottomMiddlePoint.y, suspectedMouth))
            {
                double averageVerticalLineOfMouth = suspectedMouth.x + suspectedMouth.width / 2;

                double differenceBetweenMouthAndEyes = Math.abs(eyesBottomMiddlePoint.x - averageVerticalLineOfMouth);

                if (differenceBetweenMouthAndEyes < threshold)
                {
                    mouthBestCandidate = Optional.of(suspectedMouth);
                    threshold = differenceBetweenMouthAndEyes;
                }
            }
        }

        if (mouthBestCandidate.isPresent())
        {
            System.out.println("Mouth is detected at: " + mouthBestCandidate.get());
        } else
        {
            System.out.println("Mouth not found at: " + image);
        }

        return mouthBestCandidate;
    }



    private boolean isMouthReasonablyBelowTheEyes(double eyesBottomYCoordinate, Rect suspectedMouth)
    {
        return suspectedMouth.y > eyesBottomYCoordinate + 50;
    }
}
