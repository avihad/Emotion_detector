package idc.cv.emotiondetector;

import idc.cv.emotiondetector.detectors.PulseDetector;
import idc.cv.emotiondetector.utillities.Utilities;

import java.util.Map;
import java.util.SortedMap;

import org.opencv.core.Core;

public class PulseDetectorMain
{

	public static void main(String[] args) {

		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		try {

			SortedMap<Integer, double[][]> pulseSamples = PulseDetector.instance.detectPulse("/avihad_pulse_check.mp4");
			Utilities.writeObjectToFile(pulseSamples, "tempSamples");

			SortedMap<Integer, Integer> pulseByFrame = PulseDetector.instance.calcPulseFromSamples(pulseSamples, 10, 29);

			// SortedMap<Integer, double[][]> pulseSamples = (SortedMap<Integer,
			// double[][]>) Utilities.readObjectFromFile("tempSamples");
			PulseDetector.instance.printSamples(pulseSamples);

			// SortedMap<Integer, Integer> pulseByFrame =
			// PulseDetector.instance.calcPulseFromSamples(pulseSamples, 15,
			// 29);
			for (Map.Entry<Integer, Integer> entry : pulseByFrame.entrySet()) {
				System.out.println("Frame #" + entry.getKey() + " Pulse: " + entry.getValue());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
