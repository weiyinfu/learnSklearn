package bp;

import bp.activitionfunction.Arctan;

public class XOR {
	public static Sample[] getTrainSamples(int cnt) {
		Sample[] ans = new Sample[4];
		ans[0] = new Sample(new double[] { 0, 0 }, new double[] { 0 });
		ans[1] = new Sample(new double[] { 0, 1 }, new double[] { 1 });
		ans[2] = new Sample(new double[] { 1, 0 }, new double[] { 1 });
		ans[3] = new Sample(new double[] { 1, 1 }, new double[] { 0 });
		return ans;
	}

	public static void main(String[] args) {
		Network network = new Network();
		// 层数一多,性能变差
		network.init(new int[] { 2, 2, 1 }, 1, new Arctan());
		Sample samples[] = getTrainSamples(4);
		for (int i = 0; i < 100; i++)
			for (int j = 0; j < samples.length; j++)
				network.train(samples[j]);
		samples = getTrainSamples(100);
		for (Sample i : samples) {
			network.assign(i.x);
			network.calculate();
			System.out.printf("%f^%f=%f %f\n", i.x[0], i.x[1], i.y[0], network.value[network.layer.length - 1][0]);
		}
	}
}
