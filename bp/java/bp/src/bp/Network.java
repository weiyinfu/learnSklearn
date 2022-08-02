package bp;

import static java.lang.Math.random;

import java.util.ArrayList;

import bp.activitionfunction.ActivitionFunction;

public class Network {
	double weight[][][];// 权值网络,第一维表示第几层,第二维表示上层第几个神经元,第三维表示当前层第几个神经元
	double dWeight[][][];
	double theta[][];// 阈值,第一维表示第几层,第二维表示该层第几个神经元
	public double value[][];// 输出值,第一维表示第几层,第二维表示该层第几个神经元
	double dValue[][];
	public int layer[];// 每层有几个神经元
	double eta = 3;// 学习率
	double momentum = 0.9;
	ActivitionFunction activitionFunction;

	public void init(int[] layer, double eta, ActivitionFunction function) {
		this.layer = layer;
		this.eta = eta;
		this.activitionFunction = function;
		// 闲置输入层的权值
		weight = new double[layer.length][][];
		dWeight = new double[layer.length][][];
		for (int i = 1; i < layer.length; i++) {
			weight[i] = new double[layer[i - 1]][layer[i]];
			dWeight[i] = new double[layer[i - 1]][layer[i]];
			randomFill(weight[i]);
		}
		// 闲置一层theta,输入层的theta默认为0,换取代码可读性
		theta = new double[layer.length][];
		for (int i = 1; i < layer.length; i++) {
			theta[i] = new double[layer[i]];
			randomFill(theta[i]);
		}
		value = new double[layer.length][];
		dValue = new double[layer.length][];
		for (int i = 0; i < layer.length; i++) {
			value[i] = new double[layer[i]];
			dValue[i] = new double[layer[i]];
		}
	}

	void randomFill(double[][] a) {
		for (double[] i : a) {
			randomFill(i);
		}
	}

	void randomFill(double[] a) {
		for (int i = 0; i < a.length; i++) {
			a[i] = random();
		}
	}

	public void calculate() {
		for (int i = 1; i < layer.length; i++) {
			for (int j = 0; j < layer[i]; j++) {
				double s = 0;
				for (int k = 0; k < layer[i - 1]; k++) {
					s += weight[i][k][j] * value[i - 1][k];
				}
				value[i][j] = activitionFunction.f(s - theta[i][j]);
			}
		}
	}

	public void assign(double[] x) {
		assert (layer[0] == x.length);
		for (int i = 0; i < x.length; i++) {
			value[0][i] = x[i];
		}
	}

	boolean same(double[] x, double[] y) {
		assert (x.length == y.length);
		for (int i = 0; i < x.length; i++) {
			if (Math.abs(x[i] - y[i]) > 0.2)
				return false;
		}
		return true;
	}

	public void train(Sample[] samples, int times) {
		for (Sample i : samples) {
			for (int j = 0; j < times; j++) {
				assign(i.x);
				calculate();
				bp(i.y);
			}
		}
	}

	public void train(Sample[] samples) {
		for (Sample i : samples) {
			assign(i.x);
			calculate();
			bp(i.y);
		}
	}

	public void train(Sample sample) {
		while (true) {
			assign(sample.x);
			calculate();
			if (same(sample.y, value[layer.length - 1])) {
				break;
			} else {
				bp(sample.y);
			}
		}
	}

	public void train(ArrayList<Sample> samples) {
		for (Sample i : samples) {
			assign(i.x);
			calculate();
			bp(i.y);
		}
	}

	void bp(double[] y) {
		assert (y.length == layer[layer.length - 1]);
		int nowLayer = layer.length - 1;
		for (int i = 0; i < layer[nowLayer]; i++) {
			dValue[nowLayer][i] = y[i] - value[nowLayer][i];
		}
		while (nowLayer > 0) {
			for (int i = 0; i < layer[nowLayer - 1]; i++) {
				dValue[nowLayer - 1][i] = 0;
			}
			for (int i = 0; i < layer[nowLayer]; i++) {
				double k = eta * dValue[nowLayer][i] * activitionFunction.ff(value[nowLayer][i]);
				theta[nowLayer][i] += -k;
				for (int j = 0; j < layer[nowLayer - 1]; j++) {
					dValue[nowLayer - 1][j] += k * weight[nowLayer][j][i];
					dWeight[nowLayer][j][i] = k * value[nowLayer - 1][j] + momentum * dWeight[nowLayer][j][i];
					weight[nowLayer][j][i] += dWeight[nowLayer][j][i];
				}
			}
			nowLayer--;
		}
	}

	void print() {
		System.out.println("values========");
		for (int i = 0; i < layer.length; i++) {
			System.out.println("第" + i + "层");
			for (int j = 0; j < value[i].length; j++) {
				System.out.print(value[i][j] + ",");
			}
			System.out.println();
		}
		System.out.println("weight===========");
		for (int i = 1; i < layer.length; i++) {
			System.out.println("第" + i + "层");
			for (int j = 0; j < layer[i]; j++) {
				for (int k = 0; k < layer[i - 1]; k++) {
					System.out.printf("w(%d,%d)=%f,", k, j, weight[i][k][j]);
				}
			}
			System.out.println();
		}
		System.out.println("theta===========");
		for (int i = 1; i < layer.length; i++) {
			System.out.print("layer" + i + " ");
			for (int j = 0; j < layer[i]; j++) {
				System.out.print(theta[i][j] + " ");
			}
			System.out.println();
		}
	}

	public static void main(String[] args) {
		new Network();
	}
}
