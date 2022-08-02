package bp;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * 本程序用来将letter-recognition.data预处理,将A,B,C...各个样本分离开来.输出到haha.txt,
 * 可以用这个程序统计一下letter-recognition.data中各个字母的数量.
 *
 */
class Node extends ArrayList<Sample> {

}

public class Pre {
	static String file = "letter-recognition.data";
	static Node[] nodes = new Node[26];

	public static void main(String[] args) {
		for (int i = 0; i < nodes.length; i++)
			nodes[i] = new Node();
		try (Scanner cin = new Scanner(new File(file)); PrintWriter cout = new PrintWriter("haha.txt")) {
			cin.useDelimiter(",|\n");
			while (cin.hasNextLine()) {
				double[] x = new double[16];
				int c = cin.next().charAt(0);
				for (int i = 0; i < 16; i++) {
					x[i] = cin.nextInt();
				}
				nodes[c - 'A'].add(new Sample(x, new double[] { c }));
			}
			for (Node i : nodes) {
				for (Sample k : i) {
					cout.print((char) (k.y[0]));
					for (int m = 0; m < 16; m++) {
						cout.print(" " + (int) k.x[m]);
					}
					cout.println();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
