package idc.cv.emotiondetector.entities;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "movie")
public class DetectedMovie
{
	private String				name;
	private List<DetectedFrame>	frames;
    private Integer             numOfFramesWithASmile;

	public DetectedMovie() {
		frames = new ArrayList<>();
	}

	public String getName() {
		return name;
	}

	@XmlAttribute(required = true)
	public void setName(String name) {
		this.name = name;
	}

	@XmlElement(name = "frame")
	public List<DetectedFrame> getFrames() {
		return frames;
	}

	public void setFrames(List<DetectedFrame> frames) {
		this.frames = frames;
	}

	public boolean addFrame(DetectedFrame frame) {
		return frames.add(frame);
	}

    public Integer getNumOfFramesWithASmile()
    {
        return numOfFramesWithASmile;
    }

    @XmlAttribute(required = true)
    public void setNumOfFramesWithASmile(Integer numOfFramesWithASmile)
    {
        this.numOfFramesWithASmile = numOfFramesWithASmile;
    }
}
