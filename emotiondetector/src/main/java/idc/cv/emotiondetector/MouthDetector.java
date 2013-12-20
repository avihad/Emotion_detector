package idc.cv.emotiondetector;

import java.io.UnsupportedEncodingException;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;

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

        MatOfRect suspectedMouths = FacePartDetector.instance.detect(image, FacePartCascades.MOUTH.getCascadeClassifier());

        int rightEyeLeftEdge = detectedEyes.fst.x;
        int rightEyeRightEdge = detectedEyes.fst.x + detectedEyes.fst.width;

        int leftEyeLeftEdge = detectedEyes.snd.x;
        int leftEyeRightEdge = detectedEyes.snd.x + detectedEyes.snd.width;

        Rect mouthBestCandidate = null;

        for (Rect suspectedMouth : suspectedMouths.toArray())
        {
            int suspectedLeftEdge = suspectedMouth.x;
            int suspectedRightEdge = suspectedMouth.x + suspectedMouth.width;
            // TODO: examine and refine this condition
            if (suspectedLeftEdge < rightEyeLeftEdge && suspectedLeftEdge > leftEyeLeftEdge && suspectedRightEdge < rightEyeRightEdge
                    && suspectedRightEdge > leftEyeRightEdge)
            {
                mouthBestCandidate = suspectedMouth;
            }

        }
        return mouthBestCandidate;
    }
}
