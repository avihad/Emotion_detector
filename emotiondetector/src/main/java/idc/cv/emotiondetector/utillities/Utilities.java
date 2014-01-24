package idc.cv.emotiondetector.utillities;

import idc.cv.emotiondetector.Main;
import idc.cv.emotiondetector.entities.DetectedEyePair;
import idc.cv.emotiondetector.entities.DetectedFace;
import idc.cv.emotiondetector.entities.DetectedFrame;
import idc.cv.emotiondetector.entities.DetectedMouth;
import idc.cv.emotiondetector.entities.DetectedMovie;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.SortedMap;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;

import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.ICodec;

public class Utilities
{
	public static String readResource(String resourceName) {
		String resource = resourceName;

		// this will replace %20 with spaces
		if (resource.startsWith("/", 0)) {
			resource = resource.replaceFirst("/", "");
		}

		try {
			return URLDecoder.decode(resource, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static Mat readImage(String fileName) throws UnsupportedEncodingException {
		return Highgui.imread(Utilities.readResource(fileName));
	}

	public static void drawDetectedParts(MatOfRect detectedParts, Mat image, String outputFileName) {
		// Draw a bounding box around each face.
		for (Rect rect : detectedParts.toArray()) {
			drawRect(rect, image);
		}

		writeImageToFile(outputFileName, image);
	}

	public static void drawRectAndStore(Rect rect, Mat image, String outputFileName) {
		drawRect(rect, image);

		writeImageToFile(outputFileName, image);
	}

	public static void drawRect(Rect rect, Mat image) {
		Core.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 0, 0));
	}

	public static void drawPoint(Point point, Mat image) {
		Core.line(image, point, new Point(point.x, point.y + 1), new Scalar(0, 0, 0), 5);
	}

	public static void drawLine(Point first, Point second, Mat image) {
		Core.line(image, first, second, new Scalar(0, 0, 0));
	}

	public static void writeImageToFile(String fileName, Mat image) {
		System.out.println(String.format("Writing %s", fileName));
		Highgui.imwrite(fileName, image);
	}

	public static void drawCollectionLineOf(Mat image, List<Point> points) {
		for (Point point : points) {
			Utilities.drawPoint(point, image);
		}

		int i = 0;
		int j = 1;
		while (j < points.size()) {
			Utilities.drawLine(points.get(i++), points.get(j++), image);
		}
	}

	/**
	 * @throws IOException
	 * */
	public static void writeObjectToFile(SortedMap<Integer, double[][]> pulseSamples, String fileName) throws IOException {
		File file = new File(fileName);

		if (file.exists()) {
			file.delete();
		}

		file.createNewFile();

		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
			out.writeObject(pulseSamples);
		}
	}

	/**
	 * @throws Exception
	 * */
	public static Object readObjectFromFile(String fileName) throws Exception {

		Object obj = null;
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
			obj = ois.readObject();
		}

		return obj;
	}

	/**
	 * Write the entity DetectedMovie into xml file named fileName using JAXB
	 * 
	 * @param xmlElement
	 * @param fileName
	 * @throws JAXBException
	 * */
	public static void witeToXml(DetectedMovie xmlElement, String fileName) throws JAXBException {

		File file = new File(fileName);

		JAXBContext jc = JAXBContext.newInstance(DetectedMovie.class);

		Marshaller marshaller = jc.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		marshaller.marshal(xmlElement, file);

	}

	public static void xmlWriteExample() throws JAXBException {
		DetectedMovie movie = new DetectedMovie();

		Rect rect = new Rect(0, 0, 10, 10);
		DetectedFrame frame1 = new DetectedFrame();
		DetectedFrame frame2 = new DetectedFrame();

		frame1.setFrameNumber(1);
		frame1.setPulse(72);
		frame1.addDetectedPart(new DetectedMouth(rect, Boolean.TRUE));
		frame1.addDetectedPart(new DetectedEyePair(rect));

		frame2.setFrameNumber(2);
		frame2.addDetectedPart(new DetectedFace(rect));

		movie.setName("SampleMovie");
		movie.addFrame(frame1);
		movie.addFrame(frame2);

		Utilities.witeToXml(movie, "test.xml");

	}

	/**
	 * Creating a movie from a png picture sequence with custom frame rate
	 * 
	 * @param outputMovieName
	 * @param picSequenceName
	 * @param numberOfFrames
	 * @param frameRate
	 * @throws IOException
	 * */
	public static void createMovieFromPics(final String outputMovieName, String picSequenceName, int numberOfFrames, int frameRate)
			throws IOException {
		System.out.println("Writing picture sequence: " + picSequenceName + " to a movie named: " + outputMovieName);

		IMediaWriter writer = ToolFactory.makeWriter(Main.outputPath + outputMovieName);
		BufferedImage image = ImageIO.read(new File(Main.outputPath + picSequenceName + 1 + ".png"));
		try {
			writer.addVideoStream(0, 0, ICodec.ID.CODEC_ID_MPEG4, image.getWidth(), image.getHeight());

			long timePerFrame = 1000 / frameRate;
			for (int i = 1; i < numberOfFrames; i++) {
				image = ImageIO.read(new File(Main.outputPath + picSequenceName + (i + 1) + ".png"));
				writer.encodeVideo(0, image, timePerFrame * i, TimeUnit.MILLISECONDS);
			}
		} finally {
			writer.close();
			System.out.println("Finish writing the movie: " + outputMovieName);
		}

	}

	/**
	 * Creating a movie from a png picture sequence with custom frame rate
	 * 
	 * @param outputMovieName
	 * @param picSequenceName
	 * @param numberOfFrames
	 * @param frameRate
	 * @throws IOException
	 * */
	public static void createMovieFromPicsWithPulse(final String outputMovieName, String picSequenceName, SortedMap<Integer, Integer> detectPulse,
			int numberOfFrames, int frameRate) throws IOException {
		System.out.println("Writing picture sequence: " + picSequenceName + " to a movie named: " + outputMovieName);

		IMediaWriter writer = ToolFactory.makeWriter(Main.outputPath + outputMovieName);
		BufferedImage image = ImageIO.read(new File(Main.outputPath + picSequenceName + 1 + ".png"));
		Integer lastPulse = detectPulse.get(detectPulse.firstKey());
		try {
			writer.addVideoStream(0, 0, ICodec.ID.CODEC_ID_MPEG4, image.getWidth(), image.getHeight());

			long timePerFrame = 1000 / frameRate;
			for (int i = 1; i < numberOfFrames; i++) {
				Integer framePulse = detectPulse.get(i);
				if (framePulse == null) {
					framePulse = lastPulse;
				}
				lastPulse = framePulse;

				image = ImageIO.read(new File(Main.outputPath + picSequenceName + (i + 1) + ".png"));
				Graphics imageGraphics = image.getGraphics();
				imageGraphics.setFont(imageGraphics.getFont().deriveFont(30f));
				imageGraphics.setColor(Color.BLACK);
				imageGraphics.drawString("Pulse: " + framePulse, (int) (image.getWidth() * 0.1), (int) (image.getHeight() * 0.1));
				imageGraphics.dispose();
				writer.encodeVideo(0, image, timePerFrame * i, TimeUnit.MILLISECONDS);
			}
		} finally {
			writer.close();
			System.out.println("Finish writing the movie: " + outputMovieName);
		}

	}

	public static void main(String[] args) throws IOException {
		createMovieFromPics("outputMovie.avi", "cannyFrame", 250, 29);
	}
}
