package bp;

import static java.lang.Math.sin;

import java.util.Random;

import bp.activitionfunction.Arctan;

public class HardRemember {
	static Random random = new Random(0);

	public static Sample[] getTrainSamples(int cnt) {
		Sample[] ans = new Sample[cnt];
		for (int i = 0; i < ans.length; i++) {
			double x = random.nextDouble();
			ans[i] = new Sample(new double[] { x }, new double[] { sin(x) });
		}
		return ans;
	}

	static double howGood(Network network, Sample[] samples) {
		double s = 0;
		for (Sample i : samples) {
			network.assign(i.x);
			network.calculate();
			s += Math.abs(i.y[0] - network.value[network.layer.length - 1][0]);
		}
		return s;
	}

	public static void main(String[] args) {
		Network network = new Network();
		// 层数一多,性能变差
		network.init(new int[] { 1, 13, 1 }, 1, new Arctan());
		// debug();
		Sample samples[] = getTrainSamples(10);
		for (int i = 0; i < 1000; i++) {
			network.train(samples);
			System.out.println(i+" : "+howGood(network, samples));
		}
	}
}
