package bp;

import static java.lang.Math.random;
import static java.lang.Math.sin;

import bp.activitionfunction.Arctan;

/*SinSimulator用来测试一下Network.java是否正确.
 *输入0~1之间的一个浮点数,输出sin(x).
 * */
public class SinSimulator {
	public static Sample[] getTrainSamples(int cnt) {
		Sample[] ans = new Sample[cnt];
		for (int i = 0; i < ans.length; i++) {
			double x = random();
			ans[i] = new Sample(new double[] { x }, new double[] { sin(x) });
		}
		return ans;
	}

	public static void main(String[] args) {
		Network network = new Network();
		// 层数一多,性能变差
		network.init(new int[] { 1, 3, 1 }, 1.2, new Arctan());
		// debug();
		Sample samples[] = getTrainSamples(300);
		for (int i = 0; i < 10000; i++)
			network.train(samples);
		samples = getTrainSamples(100);
		for (Sample i : samples) {
			network.assign(i.x);
			network.calculate();
			System.out.printf("sin(%f)=%f %f\n", i.x[0], i.y[0], network.value[network.layer.length - 1][0]);
		}
	}
}
