package idc.cv.emotiondetector.entities;

import javax.xml.bind.annotation.XmlAttribute;

public class SimplePoint
{

	private int	x;
	private int	y;

	public SimplePoint() {
	}

	public SimplePoint(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	@XmlAttribute(required = true)
	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	@XmlAttribute(required = true)
	public void setY(int y) {
		this.y = y;
	}

}
