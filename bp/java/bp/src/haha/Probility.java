package haha;

import bp.Network;
import bp.Sample;

import java.util.Arrays;
import java.util.Random;

public class Probility extends DataReader {
    //a[i,j,k]表示i字符的第j个特征取值为k的概率出现的次数.
    double a[][][] = new double[26][16][16];
    Network network = new Network();
    String file = "letter-recognition.data";
    Random random = new Random();


    void test() {
        double right = 0;
        for (Sample sample : testSamples) {
            int ansChar = -1;
            double ansP = 0;
            for (int c = 0; c < 26; c++) {
                double p = 1;
                for (int j = 0; j < 16; j++) {
                    int v = (int) sample.x[j];
                    p *= a[c][j][v];
                }
                if (p > ansP) {
                    ansChar = c;
                    ansP = p;
                }
            }
            if (ansChar == (int) sample.y[0]) {
                right++;
            }
        }
        System.out.println(right / testSamples.length + " " + right);
    }

    public Probility() {
        init();
        int trainCount = 16000;
        trainSamples = Arrays.copyOfRange(samples, 0, trainCount);
        testSamples = Arrays.copyOfRange(samples, trainCount, 20000);
        for (Sample sample : trainSamples) {
            int c = (int) sample.y[0];
            for (int i = 0; i < 16; i++) {
                a[c][i][(int) sample.x[i]]++;
            }
        }
        for (int i = 0; i < 16; i++) {// 位置
            for (int j = 0; j < 16; j++) {// 值
                double cnt = 0;
                for (int k = 0; k < 26; k++) {
                    cnt += a[k][i][j];
                }
                for (int k = 0; k < 26; k++) {
                    a[k][i][j] /= cnt;
                }
            }
        }
        test();
    }

    public static void main(String[] args) {
        new Probility();
    }
}
