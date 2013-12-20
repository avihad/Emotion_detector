package idc.cv.emotiondetector;

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
        Optional<Pair<Rect, Rect>> optionalDetectedEyes = EyeDetector.instance.detectEyes(image);

        if (!optionalDetectedEyes.isPresent())
        {
            return Optional.absent();
        }

        MatOfRect suspectedMouths = FacePartDetector.instance.detect(image, FacePartCascade.MOUTH);

        int leftEyeLeftEdge = optionalDetectedEyes.get().first.x;
        int rightEyeRightEdge = optionalDetectedEyes.get().second.x + optionalDetectedEyes.get().second.width;

        Optional<Rect> mouthBestCandidate = Optional.absent();
        double threshold = Double.MAX_VALUE;

        for (Rect suspectedMouth : suspectedMouths.toArray())
        {
            if (aboveTheEyes(optionalDetectedEyes.get(), suspectedMouth)) continue;

            int mouthLeftEdge = suspectedMouth.x;
            int mouthRightEdge = suspectedMouth.x + suspectedMouth.width;
            double averageVerticalLineOfEyes = (rightEyeRightEdge - leftEyeLeftEdge) / 2;
            double averageVerticalLineOfMouth = (mouthRightEdge - mouthLeftEdge) / 2;

            double differenceBetweenMouthAndEyes = Math.abs(averageVerticalLineOfEyes - averageVerticalLineOfMouth);

            if (differenceBetweenMouthAndEyes < threshold)
            {
                mouthBestCandidate = Optional.of(suspectedMouth);
                threshold = differenceBetweenMouthAndEyes;
            }
        }

        return mouthBestCandidate;
    }

    private boolean aboveTheEyes(Pair<Rect, Rect> detectedEyes, Rect suspectedMouth)
    {
        return suspectedMouth.y <= detectedEyes.first.y;
    }
}
