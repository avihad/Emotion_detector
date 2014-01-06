package idc.cv.emotiondetector.entities;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import org.opencv.core.Rect;

public abstract class DetectedPart
{

	private List<SimplePoint>	points;

	public DetectedPart() {
	}

	public DetectedPart(Rect detectedPart) {
		int x = detectedPart.x;
		int y = detectedPart.y;
		int width = detectedPart.width;
		int height = detectedPart.height;
		points = new ArrayList<>();

		points.add(new SimplePoint(x, y));
		points.add(new SimplePoint(x + width, y));
		points.add(new SimplePoint(x, y + height));
		points.add(new SimplePoint(x + width, y + height));

	}

	public List<SimplePoint> getPoints() {
		return points;
	}

	@XmlElement(name = "point")
	public void setPoints(List<SimplePoint> points) {
		this.points = points;
	}

	public boolean addPoint(SimplePoint point) {
		return points.add(point);
	}

}
