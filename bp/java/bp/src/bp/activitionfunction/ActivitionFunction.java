package bp.activitionfunction;

public interface ActivitionFunction {
	double f(double x);

	// y=f(x),求f(x)的导数,注意此函数的输入为y,求的确实f'(x).
	double ff(double y);
}
