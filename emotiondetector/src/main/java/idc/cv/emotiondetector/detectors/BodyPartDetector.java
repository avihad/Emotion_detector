package idc.cv.emotiondetector.detectors;

import idc.cv.emotiondetector.utillities.Optional;
import org.opencv.core.Mat;

public interface BodyPartDetector<R>
{
    Optional<R> detectIn(Mat image) throws Exception;
}
