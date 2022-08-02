package com.digiburo.backprop1c.demo4;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import com.digiburo.backprop1c.network.BackProp;
import com.digiburo.backprop1c.network.Pattern;

public class Test {
	public static final String NETWORK_FILENAME = "demo4.serial";
	public static final String DATA_FILE = "demo4/letter.serial";
	ArrayList<Pattern> data;
	private BackProp bp;

	/**
	 * Create network
	 * 
	 * @throws Exception
	 */
	public Test(File network) throws Exception {
		bp = new BackProp(network);
		ObjectInputStream cInputStream = new ObjectInputStream(new FileInputStream(DATA_FILE));
		data = (ArrayList<Pattern>) cInputStream.readObject();
	}

	/**
	 * Generate a 2D matrix from -1 to 1 at 0.1 intervals. Submit these points
	 * to the network for classification.
	 * 
	 * @return
	 */
	public double performTesting() throws Exception {
		BackProp network = bp;
		int rightCnt = 0;
		for (Pattern i : data) {
			double[] mine = network.runNetwork(i.getInput());
			char mineChar = parse(mine);
			char ansChar = parse(i.getOutput());
			System.out.printf("mine=%c real=%c\n", mineChar, ansChar);
			if (mineChar == ansChar)
				rightCnt++;
		}
		return rightCnt * 1.0 / data.size();

	}

	char parse(double[] x) {
		int ans = 0;
		for (int i = 0; i < x.length; i++) {
			int num = (int) Math.round(x[i]);
			if (num == 1) {
				ans |= (1 << i);
			} else if (num == 0) {

			} else {
				System.err.println("输出竟然不是0-1");
				System.exit(0);
			}
		}
		return (char) (ans + 'A');
	}

	/**
	 * Driver
	 */
	public static void main(String args[]) throws Exception {
		System.out.println("begin");

		Test tr = new Test(new File(NETWORK_FILENAME));

		System.out.println(tr.performTesting());

		System.out.println("end");
	}
}
