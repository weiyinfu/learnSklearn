package haha;

import bp.Sample;

import java.io.File;
import java.util.Scanner;

public class DataReader {
    Sample[] samples = new Sample[20000];
    String file = "letter-recognition.data";
    Sample[] trainSamples, testSamples;

    void init() {
        try (Scanner cin = new Scanner(new File(file))) {
            int j = 0;
            cin.useDelimiter(",|\n");
            while (cin.hasNextLine()) {
                double[] x = new double[16];
                int c = cin.next().charAt(0);
                double[] y = new double[] { c };
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

}
