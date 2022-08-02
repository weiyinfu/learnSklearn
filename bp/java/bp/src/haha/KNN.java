package haha;

import bp.Sample;

import java.util.Arrays;

public class KNN  extends DataReader{
	int neiborCount = 12;
	double d[] = new double[neiborCount + 1];
	char c[] = new char[neiborCount + 1];


	void reset() {
		d[0] = 0;
		for (int i = 1; i <= neiborCount; i++) {
			d[i] = Double.MAX_VALUE;
		}
	}

	// 欧几里得距离
	double ojld(Sample x, Sample y) {
		double s = 0;
		for (int i = 0; i < x.x.length; i++) {
			s += (x.x[i] - y.x[i]) * (x.x[i] - y.x[i]);
		}
		return Math.sqrt(s);
	}

	// 汉密尔顿距离
	double hamierton(Sample x, Sample y) {
		double s = 0;
		for (int i = 0; i < x.x.length; i++) {
			s += Math.abs(x.x[i] - y.x[i]);
		}
		return s;
	}

	// 余弦距离
	double cos(Sample x, Sample y) {
		double s = 0;
		double d1 = 0, d2 = 0;
		for (int i = 0; i < x.x.length; i++) {
			d1 += x.x[i] * x.x[i];
			d2 += y.x[i] * y.x[i];
			s += x.x[i] * y.x[i];
		}
		return Math.abs(s) / (Math.sqrt(d1 * d2));
	}

	double dis(Sample x, Sample y) {
		return ojld(x, y);
	}

	// 分析KNN结果,半数以上
	boolean analyzeSimple(char trueAns) {
		int cnt = 0;
		for (int i = 1; i <= neiborCount; i++) {
			if (c[i] == trueAns) {
				cnt++;
			}
		}
		return cnt > neiborCount / 2;
	}

	// 精确式分析投票结果
	boolean analyzeAccurate(char trueAns) {
		int[] ans = new int[26];
		double sumD[] = new double[26];
		for (int i = 1; i <= neiborCount; i++) {
			ans[c[i] - 'A']++;
			sumD[c[i] - 'A'] += d[i];
		}
		int ma = 0;
		for (int i = 0; i < ans.length; i++) {
			if (ans[i] > ans[ma]) {
				ma = i;
			} else if (ans[i] == ans[ma] && sumD[i] < sumD[ma]) {
				ma = i;
			}
		}
		return (char) (ma + 'A') == trueAns;
	}

	boolean analyze(char trueAns) {
		return analyzeAccurate(trueAns);
	}

	// 获取正确率
	double getRate() {
		int rightCnt = 0;
		for (Sample i : testSamples) {
			reset();
			for (Sample j : trainSamples) {
				double di = dis(i, j);
				int k = neiborCount;
				while (d[k] > di) {
					d[k] = d[k - 1];
					c[k] = c[k - 1];
					k--;
				}
				if (k < neiborCount) {
					d[k + 1] = di;
					c[k + 1] = (char) j.y[0];
				}
			}
			if (analyze((char) i.y[0])) {
				rightCnt++;
			}
		}
		return (double) rightCnt / testSamples.length;
	}

	public KNN() {
		init();
		trainSamples = Arrays.copyOfRange(samples, 0, 16000);
		testSamples = Arrays.copyOfRange(samples, 16000, 20000);
		for (neiborCount = 1; neiborCount < 20; neiborCount++) {
			d = new double[neiborCount + 1];
			c = new char[neiborCount + 1];
			System.out.printf("%d %f\n", neiborCount, getRate());
		}
	}

	public static void main(String[] args) {
		new KNN();
	}
}
