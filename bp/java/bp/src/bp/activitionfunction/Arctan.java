package bp.activitionfunction;

import static java.lang.Math.PI;
import static java.lang.Math.atan;
import static java.lang.Math.tan;

public class Arctan implements ActivitionFunction {

	@Override
	public double f(double x) {
		return atan(x) / PI + 0.5;
	}

	@Override
	public double ff(double y) {
		double xx = tan((y - 0.5) * PI);
		return 1 / PI / (xx * xx + 1);
	}

}
