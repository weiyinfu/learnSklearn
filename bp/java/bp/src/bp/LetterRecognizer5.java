package bp;

import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

import bp.activitionfunction.Arctan;

public class LetterRecognizer5 {
	Sample[] samples = new Sample[20000];
	Network network = new Network();
	String file = "letter-recognition.data";

	void init() {
		try (Scanner cin = new Scanner(new File(file));) {
			int j = 0;
			cin.useDelimiter(",|\n");
			while (cin.hasNextLine()) {
				double[] x = new double[16];
				int c = cin.next().charAt(0) - 'A';
				double[] y = new double[26];
				y[c] = 1;
				for (int i = 0; i < 16; i++) {
					x[i] = cin.nextInt();
				}
				samples[j] = new Sample(x, y);
				j++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public LetterRecognizer5() {
		init();
		network.init(new int[] { 16, 30, 26 }, 1, new Arctan());
		Sample[] data = Arrays.copyOfRange(samples, 0, 6);
		for (int i = 0; i < 20000; i++) {
			// System.out.println("is training " + i + " round");
			network.train(data);
		}
		//data = Arrays.copyOfRange(samples, 16000, samples.length - 1);
		int rightCnt = 0;
		for (Sample i : data) {
			network.assign(i.x);
			network.calculate();
			double[] mine = network.value[network.layer.length - 1];
			char mineChar = (char) (parse(mine) + 'A');
			char ansChar = (char) (parse(i.y) + 'A');
			System.out.printf("mine=%c real=%c\n", mineChar, ansChar);
			if (mineChar == ansChar)
				rightCnt++;
		}
		System.out.printf("\n****************\n正答率为%d/%d=%f\n", rightCnt, data.length, rightCnt * 1.0 / data.length);
	}

	int parse(double[] x) {
		int ans = 0;
		for (int i = 1; i < x.length; i++) {
			if (x[i] > x[ans]) {
				ans = i;
			}
		}
		return ans;
	}

	public static void main(String[] args) {
		new LetterRecognizer5();
	}
}
