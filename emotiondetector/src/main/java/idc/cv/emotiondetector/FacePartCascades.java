package idc.cv.emotiondetector;

import org.opencv.objdetect.CascadeClassifier;

public enum FacePartCascades
{
    EYE("/haarcascade_eye.xml"),
    MOUTH("/haarcascade_smile.xml"),
    FACE("/haarcascade_frontalface_alt2.xml");

    private final CascadeClassifier cascadeClassifier;

    private FacePartCascades(String cascadePath)
    {
        this.cascadeClassifier = new CascadeClassifier(Utility.readResource(cascadePath));
    }

    public CascadeClassifier getCascadeClassifier()
    {
        return cascadeClassifier;
    }

}
