package bp;

import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

import bp.activitionfunction.Arctan;

/**
 * 这个识别器是最好的,关键在于对样本的处理,输入为16个4bit,相当于64个bit;输出为26种答案,可以用5bit来表示.所以设置成输入层神经元为64个
 * ,输出层神经元为5个.
 */
public class LetterRecognizer3 {
	Sample[] samples = new Sample[20000];
	Network network = new Network();
	String file = "letter-recognition.data";

	void init() {

		try (Scanner cin = new Scanner(new File(file));) {
			int j = 0;
			cin.useDelimiter(",|\n");
			while (cin.hasNextLine()) {
				double[] x = new double[64];
				int c = cin.next().charAt(0) - 'A';
				double[] y = new double[] { c & 1, (c >> 1) & 1, (c >> 2) & 1, (c >> 3) & 1, (c >> 4) & 1 };
				for (int i = 0; i < 16; i++) {
					int num = cin.nextInt();
					x[i * 4] = num & 1;
					x[i * 4 + 1] = (num >> 1) & 1;
					x[i * 4 + 2] = (num >> 2) & 1;
					x[i * 4 + 3] = (num >> 3) & 1;
				}
				samples[j] = new Sample(x, y);
				j++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public LetterRecognizer3() {
		init();
		network.init(new int[] { 64, 5 }, 1, new Arctan());
		Sample[] data = Arrays.copyOfRange(samples, 0, 10000);
		for (int i = 0; i < 1000; i++) {
			System.out.println("is training " + i + " round");
			network.train(data);
		}
		data = Arrays.copyOfRange(samples, 16000, samples.length - 1);
		int rightCnt = 0;
		for (Sample i : data) {
			network.assign(i.x);
			network.calculate();
			double[] mine = network.value[network.layer.length - 1];
			char mineChar = parse(mine);
			char ansChar = parse(i.y);
			//System.out.printf("mine=%c real=%c\n", mineChar, ansChar);
			if (mineChar == ansChar)
				rightCnt++;
		}
		System.out.printf("\n****************\n正答率为%d/%d=%f\n", rightCnt, data.length, rightCnt * 1.0 / data.length);
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
		new LetterRecognizer3();
	}
}
