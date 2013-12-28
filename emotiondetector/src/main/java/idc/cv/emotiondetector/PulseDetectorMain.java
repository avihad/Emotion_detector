package idc.cv.emotiondetector;

import idc.cv.emotiondetector.detectors.PulseDetector;

import java.util.Map;

import org.opencv.core.Core;

public class PulseDetectorMain
{

	public static void main(String[] args) {

		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		try {

			Map<Integer, double[][]> pulseSamples = PulseDetector.detectPulse("avihad.mp4");
			System.out.println(pulseSamples);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
