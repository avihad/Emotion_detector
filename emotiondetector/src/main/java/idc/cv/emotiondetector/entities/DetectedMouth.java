package idc.cv.emotiondetector.entities;

import javax.xml.bind.annotation.XmlAttribute;

import org.opencv.core.Rect;

public class DetectedMouth extends DetectedPart
{
	private Boolean	smilling;

	public DetectedMouth() {
		super();
	}

	public DetectedMouth(Rect detectedPart) {
		super(detectedPart);
	}

	public DetectedMouth(Rect detectedPart, Boolean smilling) {
		super(detectedPart);
		this.smilling = smilling;
	}

	public Boolean getSmilling() {
		return smilling;
	}

	@XmlAttribute
	public void setSmilling(Boolean smilling) {
		this.smilling = smilling;
	}

}
