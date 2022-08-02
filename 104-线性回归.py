import matplotlib.pyplot as plt
import numpy as np
import numpy.linalg as alg

sz = 10
x = np.random.rand(sz) * 100
k_ = 3.2
b_ = 1.7
y_ = k_ * x + b_
y = y_ + np.random.rand(sz) * 20 - 10
k, b = alg.solve([[np.sum(x * x), np.sum(x)], [np.sum(x), sz]], [np.sum(x * y), np.sum(y)])
y_mine = k * x + b
plt.scatter(x, y)
plt.plot(x, y_)
plt.plot(x, y_mine, color='r')
plt.show()

#使用sklearn中的线性回归
from sklearn import linear_model

reg = linear_model.LinearRegression()
reg.fit([[i] for i in x], y)
print(reg, reg.coef_, reg.intercept_)
print(k,b)