package com.digiburo.backprop1c.demo3;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;

import com.digiburo.backprop1c.network.PatternList;

/**
 * Convert txt files to serialized patterns
 *
 * @author G.S. Cole (gsc@digiburo.com)
 * @version $Id: GenerateDatum.java,v 1.1 2002/02/03 04:24:05 gsc Exp $
 */

/*
 * Development Environment: Linux 2.2.14-5.0 (Red Hat 6.2) Java Developers Kit
 * 1.3.1
 *
 * Legalise: Copyright (C) 2002 Digital Burro, INC.
 *
 * Maintenance History: $Log: GenerateDatum.java,v $ Revision 1.1 2002/02/03
 * 04:24:05 gsc Initial Check In
 */

public class GenerateDatum {
	private static final double one = 0.9999999999;
	private static final double zero = 0.0000000001;

	private static final String TRAIN_FILENAME = "tmp/demo3.trn";

	private static final String FILE_PATH = "src/demo3/src/main/resources/";

	private static final String[] RAW_FILENAMES = { "zero_a.txt", "one_a.txt", "two_a.txt", "three_a.txt", "four_a.txt",
			"five_a.txt", "six_a.txt", "seven_a.txt", "eight_a.txt", "nine_a.txt" };

	/**
	 * Read and return the ASCII pattern files as Pattern objects.
	 */
	public void createTrainingSet(String fileName) throws Exception {
		PatternList pl = new PatternList();

		double input[] = null;
		double output[] = new double[10];

		for (int ii = 0; ii < RAW_FILENAMES.length; ii++) {
			output[ii] = zero;
		}

		for (int ii = 0; ii < RAW_FILENAMES.length; ii++) {
			System.out.println(RAW_FILENAMES[ii]);
			input = patternReader(FILE_PATH + RAW_FILENAMES[ii]);
			if (ii == 0) {
				output[ii] = one;
			} else {
				output[ii] = one;
				output[ii - 1] = zero;
			}

			pl.add(input, output);
		}

		pl.writer(new File(TRAIN_FILENAME));
	}

	/**
	 * Convert the ASCII file do an array of double suitable for input neurons
	 *
	 * @param fileName
	 *            of file to read
	 * @return file contents as array of double
	 */
	private double[] patternReader(String fileName) {
		String rawPattern = null;

		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));

			String inbuffer;
			while ((inbuffer = br.readLine()) != null) {
				if (rawPattern == null) {
					rawPattern = inbuffer;
				} else {
					rawPattern += inbuffer;
				}
			}

			br.close();
		} catch (Exception ee) {
			ee.printStackTrace();
		}

		if (rawPattern.length() != 25) {
			throw new IllegalArgumentException("bad pattern length");
		}

		byte[] temp = rawPattern.getBytes();

		double[] result = new double[25];
		for (int ii = 0; ii < 25; ii++) {
			result[ii] = (temp[ii] == 0x20) ? zero : one;
		}

		return (result);
	}

	/**
	 * Driver
	 */
	public static void main(String args[]) throws Exception {
		System.out.println("begin:" + args.length);

		String fileName = TRAIN_FILENAME;

		if (args.length > 0) {
			fileName = args[0];
		}

		System.out.println("training output:" + fileName);

		GenerateDatum gd = new GenerateDatum();
		gd.createTrainingSet(fileName);

		System.out.println("end");
	}
}
