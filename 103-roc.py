import sklearn.metrics as metrics
import numpy as np


def roc(y_true, y_mine):
    a = [(y_mine[i], y_true[i]) for i in range(len(y_true))]
    a = sorted(a, key=lambda x: -x[0])
    thresh = sorted(list(set([0, 1] + [i[0] for i in a])), key=lambda x: -x)
    cnt = [np.count_nonzero(y_true == 0), np.count_nonzero(y_true == 1), 0, 0]
    x = []
    y = []
    j = 0
    for i in thresh:
        while j < len(a) and a[j][0] >= i:
            if a[j][1]:
                cnt[0b11] += 1
                cnt[0b01] -= 1
            else:
                cnt[0b10] += 1
                cnt[0b00] -= 1
            j += 1
        x.append(cnt[0b10] / (cnt[0b00] + cnt[0b10]))
        y.append(cnt[0b11] / (cnt[0b11] + cnt[0b01]))
    return x, y, thresh


def auc(x, y):
    a = sorted([(x[i], y[i]) for i in range(len(x))], key=lambda x: x[0])
    area = 0
    for i in range(1, len(a)):
        area += (a[i - 1][1] + a[i][1]) * (a[i][0] - a[i - 1][0]) / 2
    return area


def auc2(y_mine, y_true):
    """
    auc的物理意义在于：随机正样本比随机负样本得分高的概率
    基于这种直观的思路可以更快速的计算auc
    本函数只适用于计算得分各不相同的样本，如果存在并列情况，此函数结果错误
    :param y_mine:
    :param y_true:
    :return:
    """
    a = sorted(zip(y_mine, y_true), key=lambda x: -x[0])
    T = np.count_nonzero(y_true)
    F = len(y_true) - T
    R = sum(i + 1 for i in range(len(a)) if a[i][1] == 1)
    return 1 + (T + 1) / (2 * F) - R / (T * F)


y_mine = np.random.rand(10)
# y_mine = np.array([1, 1, 1, 0.5, 0.5, 0.5, 0, 0, 0, 0])
y_true = np.random.randint(0, 2, 10)
x, y, t = roc(y_true, y_mine)
print(auc(x, y))
print(metrics.auc(x, y))
print(auc2(y_mine, y_true))
