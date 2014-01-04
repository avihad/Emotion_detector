package idc.cv.emotiondetector.detectors;

import idc.cv.emotiondetector.utillities.Optional;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;

public enum FaceDetector implements BodyPartDetector<Rect>
{
    instance;

    public Optional<Rect> detectIn(Mat image) throws Exception
    {
        Optional<Rect> optionalFace = Optional.absent();

        MatOfRect suspectedFaces = FacePartDetector.instance.detect(image, FacePartCascade.FACE);

        if (suspectedFaces.toArray().length > 0)
        {
            optionalFace = Optional.of(suspectedFaces.toArray()[0]);
        }

        return optionalFace;
    }
}
