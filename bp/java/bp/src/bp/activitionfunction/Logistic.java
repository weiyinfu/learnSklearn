package bp.activitionfunction;

import static java.lang.Math.exp;

public class Logistic implements ActivitionFunction {

	@Override
	public double f(double x) {
		return 1 / 1 + exp(-x);
	}

	@Override
	public double ff(double y) {
		return y * (1 - y);
	}

}
