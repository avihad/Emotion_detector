package idc.cv.emotiondetector;

import idc.cv.emotiondetector.utillities.Utilities;
import idc.cv.emotiondetector.utillities.VideoReader;

import java.util.SortedMap;

import org.opencv.core.Core;

public class MovieSampleMain
{
	public static void main(String[] args) throws Exception {

		if (args.length != 3) {
			System.out.println("Usage: wrong parameter's try:  <video name> <frame rate> <number of frames>");
			System.exit(-1);
		}

		String videoFileName = args[0];
		Integer movieFrameRate = Integer.valueOf(args[1]);
		Integer numOfFrames = Integer.valueOf(args[2]);

		// Load the native library.
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		VideoReader.instance.storeAsFrames(Main.resourcePath + videoFileName);
		SortedMap<Integer, Integer> detectPulse = PulseDetectorMain.detectPulse(Main.resourcePath + videoFileName, 20);

		Utilities.createMovieFromPicsWithPulse("output.avi", "frame", detectPulse, numOfFrames, movieFrameRate);
		System.out.println("Finish detecting emutions !");
	}

}
