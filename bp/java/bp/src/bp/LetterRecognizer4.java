package bp;

import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

import bp.activitionfunction.Arctan;

public class LetterRecognizer4 {
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
				double[] y = new double[] { c & 1, (c >> 1) & 1, (c >> 2) & 1, (c >> 3) & 1, (c >> 4) & 1 };
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

	double getRate(Sample[] testData) {
		int rightCnt = 0;
		for (Sample i : testData) {
			network.assign(i.x);
			network.calculate();
			double[] mine = network.value[network.layer.length - 1];
			char mineChar = parse(mine);
			char ansChar = parse(i.y);
			System.out.printf("mine=%c real=%c\n", mineChar, ansChar);
			if (mineChar == ansChar)
				rightCnt++;
		}
		return rightCnt * 1.0 / testData.length;

	}

	public LetterRecognizer4() {
		init();
		network.init(new int[] { 16, 25, 5 }, 0.5, new Arctan());
		Sample[] data = Arrays.copyOfRange(samples, 0, 50);
		for (int i = 0; i < 100000; i++) {
			// System.out.println("is training " + i + " round");
			network.train(data);
		}
		// data = Arrays.copyOfRange(samples, 16000, samples.length - 1);
		System.out.println(getRate(data));
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

	public static void main(String[] args) {
		new LetterRecognizer4();
	}

}
