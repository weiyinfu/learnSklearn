package com.digiburo.backprop1c.demo4;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.digiburo.backprop1c.network.BackProp;
import com.digiburo.backprop1c.network.Pattern;
import com.digiburo.backprop1c.network.PatternList;

public class BpDemo4 {
	public static final double one = 0.9999999999;
	public static final double zero = 0.0000000001;

	private static final String TRAIN_FILENAME = "demo4/letter.serial";
	private static final String NETWORK_FILENAME = "demo4.serial";

	private BackProp bp;
	private PatternList pl;

	/**
	 * Create network
	 */
	public BpDemo4() {
		bp = new BackProp(16, 25, 5, 0.45, 0.9);
	}

	/**
	 * Load training datum
	 *
	 * @param datum
	 *            training file
	 */
	public int loadTraining(File datum) throws IOException, FileNotFoundException, ClassNotFoundException {
		pl = new PatternList();
		pl.reader(datum);
		return (pl.size());
	}

	/**
	 * Train the network on these patterns
	 */
	public void performTraining() {
		bp.trainNetwork(pl, pl.size(), -1, 0.15, true);
	}

	/**
	 * Save this network for later use.
	 *
	 * @param datum
	 *            file to save as
	 */
	public void saveTraining(File datum) throws IOException, FileNotFoundException {
		bp.saveNetwork(datum);
	} 

	/**
	 *
	 */
	public static void main(String args[]) throws Exception {
		System.out.println("begin:" + args.length);

		String trainFileName = TRAIN_FILENAME;
		String networkFileName = NETWORK_FILENAME;

		if (args.length == 2) {
			trainFileName = args[0];
			networkFileName = args[1];
		}

		BpDemo4 tr = new BpDemo4();
		int population = tr.loadTraining(new File(trainFileName));
		System.out.println("PatternList loaded w/" + population + " patterns");
		tr.performTraining();
		tr.saveTraining(new File(networkFileName));
		System.out.println("end");
	}
}
