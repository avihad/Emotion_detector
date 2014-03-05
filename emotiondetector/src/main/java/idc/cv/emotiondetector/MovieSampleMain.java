package idc.cv.emotiondetector;

import java.util.SortedMap;

import org.opencv.core.Core;

public class MovieSampleMain
{
	public static void main(String[] args) throws Exception {

		if (args.length != 2) {
			System.out.println("Usage: wrong parameter's try:  <video name> <frame rate>");
			System.exit(-1);
		}

		String videoFileName = args[0];
		Integer movieFrameRate = Integer.valueOf(args[1]);

		// Load the native library.
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		// Integer numOfFrames =
		// VideoReader.instance.storeAsFrames(Main.resourcePath +
		// videoFileName);
		SortedMap<Integer, Integer> detectPulse = PulseDetectorMain.detectPulse(Main.resourcePath + videoFileName, 20);

		// Utilities.createMovieFromPicsWithPulse(videoFileName + "_output.avi",
		// "frame", detectPulse, numOfFrames, movieFrameRate);
		System.out.println("Finish detecting emutions !");
	}

}
