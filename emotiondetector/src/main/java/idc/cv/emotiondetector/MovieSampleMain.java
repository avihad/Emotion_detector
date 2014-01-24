package idc.cv.emotiondetector;

import idc.cv.emotiondetector.utillities.Utilities;
import idc.cv.emotiondetector.utillities.VideoReader;

import java.util.SortedMap;

import org.opencv.core.Core;

public class MovieSampleMain
{
	public static void main(String[] args) throws Exception {
		// Load the native library.
		String videoFileName = "1.avi";
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		VideoReader.instance.storeAsFrames(Main.resourcePath + videoFileName);
		SortedMap<Integer, Integer> detectPulse = PulseDetectorMain.detectPulse(Main.resourcePath + videoFileName, 20);

		Utilities.createMovieFromPicsWithPulse("output.avi", "frame", detectPulse, 257, 29);
	}

}
