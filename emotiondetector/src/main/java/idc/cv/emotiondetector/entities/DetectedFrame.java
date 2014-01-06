package idc.cv.emotiondetector.entities;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "frame")
public class DetectedFrame
{
	private Integer				frameNumber;
	private Integer				pulse;
	private List<DetectedPart>	detectedParts;

	public DetectedFrame() {
		detectedParts = new ArrayList<>();
	}

	public Integer getFrameNumber() {
		return frameNumber;
	}

	@XmlAttribute(required = true)
	public void setFrameNumber(Integer frameNumber) {
		this.frameNumber = frameNumber;
	}

	@XmlAttribute
	public Integer getPulse() {
		return pulse;
	}

	public void setPulse(Integer pulse) {
		this.pulse = pulse;
	}

	public boolean addDetectedPart(DetectedPart part) {
		return detectedParts.add(part);
	}

	public List<DetectedPart> getDetectedParts() {
		return detectedParts;
	}

	@XmlElements({ @XmlElement(name = "mouth", type = DetectedMouth.class), @XmlElement(name = "face", type = DetectedFace.class),
			@XmlElement(name = "eyePair", type = DetectedEyePair.class) })
	public void setDetectedParts(List<DetectedPart> detectedParts) {
		this.detectedParts = detectedParts;
	}

}
