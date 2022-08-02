package haha;

import bp.Sample;

import java.util.Arrays;

public class Parzen extends DataReader {
    double norm_max(Sample x, Sample y) {
        double s = 0;
        for (int i = 0; i < x.x.length; i++) {
            s = Math.max(Math.abs(x.x[i] - y.x[i]), s);
        }
        return s;
    }

    double norm2(Sample x, Sample y) {
        double s = 0;
        for (int i = 0; i < x.x.length; i++) {
            s += Math.pow(x.x[i] - y.x[i], 2);
        }
        return s;
    }

    double kernel_uniform(double x, double h, int dim) {
        if (x / h <= 0.5) return 1.0 / Math.pow(h, dim);
        else return 0;
    }

    double kernel_gauss(double x, double h) {
        return Math.exp(-x * x / (2 * h * h)) / h;
    }

    double go() {
        int rightCnt = 0;
        double h = 3;
        for (Sample i : testSamples) {
            double p[] = new double[26];
            for (Sample j : trainSamples) {
                int c = (int) (j.y[0] - 'A');
//                p[c] += kernel_uniform(norm_max(i, j), h, 16);
                p[c] += kernel_gauss(norm2(i, j), h);
            }
            double ans = 'A';
            double ansP = p[0];
            for (int j = 1; j < 26; j++) {
                if (ansP < p[j]) {
                    ansP = p[j];
                    ans = j + 'A';
                }
            }
            if (ans == i.y[0]) {
                rightCnt++;
            }
        }
        return (double) rightCnt / testSamples.length;
    }

    Parzen() {
        init();
        int trainCount = 16000;
        trainSamples = Arrays.copyOfRange(samples, 0, trainCount);
        testSamples = Arrays.copyOfRange(samples, trainCount, 20000);
        double right = go();
        System.out.println(right);
    }

    public static void main(String[] args) {
        new Parzen();
    }
}
