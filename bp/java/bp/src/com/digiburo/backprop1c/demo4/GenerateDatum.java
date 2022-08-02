package com.digiburo.backprop1c.demo4;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Scanner;

import com.digiburo.backprop1c.network.Pattern;

public class GenerateDatum {
	ArrayList<Pattern> data = new ArrayList<>();
	final static String file = "demo4/letter-recognition.data";
	final static String outputFile = "demo4/letter.serial";

	void init() {
		try (Scanner cin = new Scanner(new File(file));) {
			cin.useDelimiter(",|\n");
			while (cin.hasNextLine()) {
				double[] x = new double[16];
				int c = cin.next().charAt(0) - 'A';
				double[] y = new double[] { c & 1, (c >> 1) & 1, (c >> 2) & 1, (c >> 3) & 1, (c >> 4) & 1 };
				for (int i = 0; i < 16; i++) {
					x[i] = cin.nextInt() / 16.0;
				}
				Pattern pattern = new Pattern(x, y);
				data.add(pattern);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public GenerateDatum() {
		try {
			init();
			ArrayList<Pattern> d = new ArrayList<>();
			for (int i = 0; i < 50; i++) {
				d.add(data.get(i));
			}
			ObjectOutputStream cout = new ObjectOutputStream(new FileOutputStream(outputFile));
			cout.writeObject(d);
			cout.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new GenerateDatum();
	}
}
