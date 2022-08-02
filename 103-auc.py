"""
回忆AUC的计算方式
"""
import numpy as np
from sklearn.metrics import roc_auc_score


def generate():
    n = 100
    x = np.random.randint(0, 100, n)
    y = np.random.randint(0, 2, len(x))
    return x, y


def get_cas():
    x = [1, 1, 1, 2, 2, 3]
    y = [0, 1, 0, 1, 1, 0]
    x, y = [9, 96, 71, 57, 88], [1, 0, 0, 1, 0]
    return np.array(x), np.array(y)


def roc(y_true, y_mine, eps=1e-7):
    # 需要考虑eps
    y, x = y_true.copy(), y_mine.copy()
    ind = np.argsort(x)
    x, y = x[ind], y[ind]
    T = np.count_nonzero(y)
    F = len(y) - T
    up = 0
    f = F
    ff = F
    # 三指针算法，需要考虑等于的情况
    k = len(y) - 1
    j = len(y) - 1
    i = len(y) - 1
    while i >= 0:
        while k >= 0 and x[k] - x[i] > eps:
            if y[k] == 0:
                f -= 1
            k -= 1
        while j >= 0 and x[i] - x[j] < eps:
            if y[j] == 0:
                ff -= 1
            j -= 1
        if y[i]:
            up += ff + (f - ff) / 2
            # print(i, x[i], f, ff)
        i -= 1
    return up / (T * F)


def test():
    x, y = generate()
    res = roc_auc_score(y, x)
    mine = roc(y, x)
    print('标准答案', res)
    print('mine', mine)
    if abs(mine - res) > 1e-5:
        print(x.tolist(), y.tolist())
        print(abs(mine - res))
        raise Exception("different")


def test_many():
    for i in range(1000):
        test()


test_many()
