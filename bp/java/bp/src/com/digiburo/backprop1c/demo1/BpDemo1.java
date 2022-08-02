package com.digiburo.backprop1c.demo1;

import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;

import com.digiburo.backprop1c.network.BackProp;

public class BpDemo1 extends BackProp {

	/**
	 * Constructor for new backpropagation network.
	 *
	 * @param inputPopulation
	 *            input node count
	 * @param middlePopulation
	 *            middle node count
	 * @param outputPopulation
	 *            output node count
	 * @param learningRate
	 * @param momentum
	 */
	public BpDemo1(int inputPopulation, int middlePopulation, int outputPopulation, double learningRate,
			double momentum) {
		super(inputPopulation, middlePopulation, outputPopulation, learningRate, momentum);
	}

	/**
	 * Constructor for existing backpropagation network.
	 *
	 * @param file
	 *            serialized Network memento
	 */
	public BpDemo1(File file) throws IOException, FileNotFoundException, ClassNotFoundException {
		super(file);
	}

	/**
	 * Classify a point as either above or below the line.
	 * 
	 * @param xx
	 *            x coordinate
	 * @param yy
	 *            y coordinate
	 * @return int -1 = below line, 1 = above line, 0 = ambiguous
	 */
	public int classifier(double xx, double yy) {
		double[] input = new double[2];
		input[0] = xx;
		input[1] = yy;

		double[] output = runNetwork(input);

		if (output[0] > 0.85) {
			return (1);
		} else if (output[0] < 0.15) {
			return (-1);
		}

		return (0);
	}
}
