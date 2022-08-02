package bp;

import java.io.File;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

import bp.activitionfunction.Arctan;
/*每个字母训练一个BP神经网络,效果很差.
 * */
public class LetterRecognizer2 {
	String file = "letter-recognition.data";
	Sample[] samples = new Sample[20000];
	Network[] networks = new Network[26];

	void init() {
		for (int i = 0; i < networks.length; i++) {
			networks[i] = new Network();
			networks[i].init(new int[] { 16, 20, 32, 1 }, 130, new Arctan());
		}
		try (Scanner cin = new Scanner(new File(file));) {
			int j = 0;
			cin.useDelimiter(",|\n");
			while (cin.hasNextLine()) {
				double[] x = new double[16];
				int c = cin.next().charAt(0) - 'A';
				double[] y = new double[] { c };
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

	void test() {
		Sample[] testData = Arrays.copyOfRange(samples, 0, 5);
		for (Sample sample : testData) {
			System.out.printf("%c ", (char) (sample.y[0] + 'A'));
			for (int i = 0; i < networks.length; i++) {
				networks[i].assign(sample.x);
				networks[i].calculate();
				int lastLayer = networks[i].layer.length - 1;
				double ans = networks[i].value[lastLayer][0];
				System.out.printf("p(%c)=%f ", (char) (i + 'A'), ans);
			}
			System.out.println();
		}
	}

	void train() {
		Sample[] trainData = Arrays.copyOfRange(samples, 0, 10000);
		AtomicInteger cnt = new AtomicInteger(26);
		for (int i = 0; i < networks.length; i++) {
			final int id = i;
			new Thread() {
				public void run() {
					int positive = 0, negtive = 0;
					for (int j = 0; j < 10; j++) {
						System.out.println("第" + j + "遍");
						for (Sample sample : trainData) {
							networks[id].assign(sample.x);
							networks[id].calculate();
							if (sample.y[0] == id) {
								networks[id].bp(new double[] { 1 });
								positive++;
							} else if (negtive<positive) {
								networks[id].bp(new double[] { 0 });
								negtive++;
							}
						}
					}

					System.out.println((char) (id + 'A') + " trained over");
					int x = cnt.decrementAndGet();
					if (x == 0) {
						System.out.println("训练全部结束");
						test();
					}
				};
			}.start();
		}
	}

	LetterRecognizer2() {
		init();
		train();
	}

	public static void main(String[] args) {
		new LetterRecognizer2();
	}
}
