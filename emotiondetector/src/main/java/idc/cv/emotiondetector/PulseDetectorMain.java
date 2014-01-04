package idc.cv.emotiondetector;

import idc.cv.emotiondetector.detectors.PulseDetector;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.SortedMap;

import org.opencv.core.Core;

public class PulseDetectorMain
{

	public static void main(String[] args) {

		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		try {
			SortedMap<Integer, double[][]> pulseSamples = PulseDetector.instance.detectPulse("/avihad2.mp4");
			writeObjectToFile(pulseSamples, "tempSamples");

			SortedMap<Integer, Integer> pulseByFrame = PulseDetector.instance.calcPulseFromSamples(pulseSamples, 10, 29);

			for (Map.Entry<Integer, Integer> entry : pulseByFrame.entrySet()) {
				System.out.println("Frame #" + entry.getKey() + " Pulse: " + entry.getValue());
			}

			pulseByFrame = PulseDetector.instance.calcPulseFromSamples(pulseSamples, 20, 29);

			for (Map.Entry<Integer, Integer> entry : pulseByFrame.entrySet()) {
				System.out.println("Frame #" + entry.getKey() + " Pulse: " + entry.getValue());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * @throws IOException
	 * */
	private static void writeObjectToFile(SortedMap<Integer, double[][]> pulseSamples, String fileName) throws IOException {
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
	private static Object readObjectFromFile(String fileName) throws Exception {

		Object obj = null;
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
			obj = ois.readObject();
		}

		return obj;
	}
}
