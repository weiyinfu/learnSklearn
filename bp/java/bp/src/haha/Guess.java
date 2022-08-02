package haha;

import bp.Sample;

import java.util.Random;

public class Guess extends DataReader{
	Random random = new Random();

	public Guess() {
		init();
		for (int i = 0; i < 100; i++) {
			int right = 0;
			for (Sample j : samples) {
				if (random.nextInt(26) == j.y[0]) {
					right++;
				}
			}
			System.out.println(right);
		}
	}

	public static void main(String[] args) {
		new Guess();
	}
}
