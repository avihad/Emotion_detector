package idc.cv.emotiondetector.detectors;

import idc.cv.emotiondetector.utillities.Optional;
import idc.cv.emotiondetector.utillities.Utilities;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;

public enum MouthDetectorImproved
{
    instance;

    /**
     * Detecting mouth in image using @FacePartDetector to find candidates and
     * choosing the best one according to the candidate alignment in refer to
     * the eyes
     *
     * @throws java.io.UnsupportedEncodingException
     */
    public Optional<Rect> detectMouth(Mat image) throws Exception
    {
        Rect face = FacePartDetector.instance.detect(image, FacePartCascade.FACE).toArray()[0];

        Rect bottomFace = new Rect(face.x, face.y + ((2 * face.height) / 3), face.width, face.height / 3);

        Mat bottomFaceOnly = new Mat(image, bottomFace);

        MatOfRect mouths = FacePartDetector.instance.detect(bottomFaceOnly, FacePartCascade.MOUTH);

        double maxWidth = 0;
        Optional<Rect> bestChoice = Optional.absent();

        for (Rect suspectedMouth : mouths.toArray())
        {
            if (Math.abs(suspectedMouth.x + (suspectedMouth.width/2) - (bottomFaceOnly.size().width/2)) < 50 && suspectedMouth.width > maxWidth)
            {
                maxWidth = suspectedMouth.width;
                bestChoice = Optional.of(suspectedMouth);
            }
        }

        if (bestChoice.isPresent())
        {
            Rect mouth = bestChoice.get();
            bestChoice = Optional.of(new Rect(bottomFace.x + mouth.x, bottomFace.y + mouth.y, mouth.width, mouth.height));
        }

        Utilities.drawRectAndStore(bestChoice.get(), image, "mouths.png");
        return bestChoice;
    }
}
