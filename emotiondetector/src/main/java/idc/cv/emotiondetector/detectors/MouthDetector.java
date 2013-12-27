package idc.cv.emotiondetector.detectors;

import java.io.UnsupportedEncodingException;

import idc.cv.emotiondetector.utillities.Optional;
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
        Optional<Rect> optionalEyesPair = EyesPairDetector.instance.detectEyePair(image);

        if (!optionalEyesPair.isPresent())
        {
            return Optional.absent();
        }

        MatOfRect suspectedMouths = FacePartDetector.instance.detect(image, FacePartCascade.MOUTH);

        int leftEyeLeftEdge = optionalEyesPair.get().x;
        int rightEyeRightEdge = optionalEyesPair.get().x + optionalEyesPair.get().width;

        Optional<Rect> mouthBestCandidate = Optional.absent();
        double threshold = Double.MAX_VALUE;

        for (Rect suspectedMouth : suspectedMouths.toArray())
        {
            if (notBeneathTheEyes(optionalEyesPair.get(), suspectedMouth)) continue;

            int mouthLeftEdge = suspectedMouth.x;
            int mouthRightEdge = suspectedMouth.x + suspectedMouth.width;
            double averageVerticalLineOfEyes = (rightEyeRightEdge - leftEyeLeftEdge) / 2;
            double averageVerticalLineOfMouth = (mouthRightEdge - mouthLeftEdge) / 2;

            double differenceBetweenMouthAndEyes = Math.abs(averageVerticalLineOfEyes - averageVerticalLineOfMouth);

            if (mouthIsReasonablyFarFromEyes(optionalEyesPair, suspectedMouth) && differenceBetweenMouthAndEyes < threshold)
            {
                mouthBestCandidate = Optional.of(suspectedMouth);
                threshold = differenceBetweenMouthAndEyes;
            }
        }

        if (mouthBestCandidate.isPresent())
        {
            System.out.println("Mouth is detected at: " + mouthBestCandidate.get());
        }
        else
        {
            System.out.println("Mouth not found at: " + image);
        }

        return mouthBestCandidate;
    }

    private boolean mouthIsReasonablyFarFromEyes(Optional<Rect> optionalEyesPair, Rect suspectedMouth)
    {
        Rect eyes = optionalEyesPair.get();
        int eyesBottomEdge = eyes.y + eyes.height;

        return suspectedMouth.y - eyesBottomEdge > 50;
    }

    private boolean notBeneathTheEyes(Rect detectedEyes, Rect suspectedMouth)
    {
        return !(suspectedMouth.y > detectedEyes.y+detectedEyes.height);
    }
}
