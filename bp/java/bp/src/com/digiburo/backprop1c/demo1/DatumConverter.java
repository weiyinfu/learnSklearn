package com.digiburo.backprop1c.demo1;

import java.io.File;

import java.io.FileWriter;
import java.io.BufferedWriter;

import com.digiburo.backprop1c.network.Pattern;
import com.digiburo.backprop1c.network.PatternList;

public class DatumConverter {
  private static final String NEG_FILENAME = "below.txt";
  private static final String POS_FILENAME = "above.txt";
  private static final String TRAIN_FILENAME = "demo1.trn";

  /**
   *
   */
  private PatternList pl;

  /**
   *
   */
  public void patternReader(String trainFileName, String aboveFileName, String belowFileName) throws Exception {
    pl = new PatternList();
    pl.reader(new File(trainFileName));

    int limit = pl.size();

    // write above answers

    BufferedWriter bw = new BufferedWriter(new FileWriter(aboveFileName));

    for (int ii = 0; ii < limit; ii++) {
      Pattern pp = pl.get(ii);
      double[] output = pp.getOutput();
      if (output[0] > 0.5) {
        writeData(bw, pp);
      }
    }

    bw.close();

    // write below answers

    bw = new BufferedWriter(new FileWriter(belowFileName));

    for (int ii = 0; ii < limit; ii++) {
      Pattern pp = pl.get(ii);
      double[] output = pp.getOutput();
      if (output[0] < 0.5) {
        writeData(bw, pp);
      }
    }

    bw.close();
  }

  /**
   *
   */
  public void writeData(BufferedWriter bw, Pattern pp) throws Exception {
    double[] input = pp.getInput();
    double[] output = pp.getOutput();

    if (input == null) {
      System.out.println("input null");
    }

    if (output == null) {
      System.out.println("output null");
    }

    for (int ii = 0; ii < input.length; ii++) {
      bw.write(Double.toString(input[ii]) + " ");
    }

    for (int ii = 0; ii < output.length; ii++) {
      bw.write(Double.toString(output[ii]) + " ");
    }

    bw.newLine();
  }

  /**
   * Driver
   */
  public static void main(String args[]) throws Exception {
    System.out.println("begin");

    String trainFileName = TRAIN_FILENAME;
    String aboveFileName = POS_FILENAME;
    String belowFileName = NEG_FILENAME;

    if (args.length == 3) {
      trainFileName = args[0];
      aboveFileName = args[1];
      belowFileName = args[2];
    }

    DatumConverter dc1 = new DatumConverter();
    dc1.patternReader(trainFileName, aboveFileName, belowFileName);

    System.out.println("end");
  }
}
