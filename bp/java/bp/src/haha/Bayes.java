package haha;

import bp.Sample;

import java.util.Arrays;

public class Bayes extends DataReader {
    double a[][][] = new double[26][16][16];
    double[] p = new double[26];

    char testOne(Sample sample) {
        int ansChar = -1;
        double ansP = -Double.MAX_VALUE;
        for (int c = 0; c < 26; c++) {
            double s = 0;
            for (int j = 0; j < 16; j++) {
                int value = (int) sample.x[j];
                s += Math.log(a[c][j][value]);
            }
            s -= 15 * Math.log(p[c]);
            if (s > ansP) {
                ansChar = c;
                ansP = s;
            }
        }
        return (char) (ansChar + 'A');
    }

    double test() {
        double right = 0;
        for (Sample sample : testSamples) {
            if (testOne(sample) == (int) sample.y[0]) {
                right++;
            }
        }
        return right / testSamples.length;
    }

    void train() {
        for (Sample sample : trainSamples) {
            int c = (int) (sample.y[0] - 'A');
            for (int i = 0; i < 16; i++) {
                a[c][i][(int) sample.x[i]]++;
            }
            p[c]++;
        }
    }

    public Bayes() {
        init();
        int trainCount = 16000;
        trainSamples = Arrays.copyOfRange(samples, 0, trainCount);
        testSamples = Arrays.copyOfRange(samples, trainCount, 20000);
        train();
        System.out.println(test());
    }

    public static void main(String[] args) {
        new Bayes();
    }
}
