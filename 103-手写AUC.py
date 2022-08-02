"""
AUC只能用于二分类，只看阳性样本
TP：真阳，我说为阳性，且正确
TN：真阴，我说为阴性，且正确
FP：假阳，我说为阳性，且错误
FN：假阴，我说为阴性，且错误

从图上来看，横轴表示FPR=FP/(TN+FP)，假阳性在负样本中的比例
纵轴表示TPR=TP/(TP+FN)，真阳性在正样本中的比例
AUC一定是大于0.5的
"""
def roc(y_true, y_mine):
    a = [(y_mine[i], y_true[i]) for i in range(len(y_true))]
    a = sorted(a, key=lambda x: -x[0])
    thresh = sorted(list(set([0, 1] + [i[0] for i in a])), key=lambda x: -x)
    cnt = [y_true.count(0), y_true.count(1), 0, 0]
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


y_mine = [0.1, 0.3, 0.6, 0.7, 1.0]
y_true = [0, 1, 1, 0, 1]
x, y, t = roc(y_true, y_mine)
print(auc(x, y))
