package idc.cv.emotiondetector;

import idc.cv.emotiondetector.utillities.Utilities;
import org.opencv.objdetect.CascadeClassifier;

public enum FacePartCascade
{
    EYE("/haarcascade_eye.xml"),
    MOUTH("/haarcascade_smile.xml"),
    FACE("/haarcascade_frontalface_alt2.xml");

    private final CascadeClassifier cascadeClassifier;

    private FacePartCascade(String cascadePath)
    {
        this.cascadeClassifier = new CascadeClassifier(Utilities.readResource(cascadePath));
    }

    public CascadeClassifier getCascadeClassifier()
    {
        return cascadeClassifier;
    }

}
