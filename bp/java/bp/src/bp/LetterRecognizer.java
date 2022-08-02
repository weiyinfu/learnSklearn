package bp;

import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

import bp.activitionfunction.Arctan;

/**
 * 用一个神经网络来识别26个字母,因为输出值在0~1之间,所以把样本的值域从1~26映射到0~1之间,即A表示为1/26,B为2/26....效果也不好
 */
public class LetterRecognizer {
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
				double[] y = new double[] { c / 30.0 + 0.1 };
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

	char parse(double x) {
		return (char) (Math.round((x - 0.1) * 30) + 'A');
	}

	public LetterRecognizer() {
		init();
		network.init(new int[] { 16, 32, 20, 7, 1 }, 1.3, new Arctan());
		Sample[] data = Arrays.copyOfRange(samples, 0, 13);
		for (int i = 0; i < 20000; i++)
			network.train(data);
		for (Sample i : data) {
			network.assign(i.x);
			network.calculate();
			double mine = network.value[network.layer.length - 1][0];
			System.out.printf("mine=%c real=%c %d\n", parse(mine), parse(i.y[0]), parse(mine) - parse(i.y[0]));
		}
	}

	public static void main(String[] args) {
		new LetterRecognizer();
	}
}
