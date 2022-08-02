import sympy as sp

x = sp.Rational(1, 3)
"""
注意：
* sp.Expr一旦遇到浮点数，立马也会跟着变成浮点数
* sp.Expr遇到int，它就会把int包装成Expr
"""
print(0. + x)
print(0 + x)
