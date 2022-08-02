import sklearn.metrics as metrics

res = metrics.auc([0.1, 0.3, 0.6, 0.7, 1.0], [0, 1, 1, 0, 1])
print(res)
x, y, threshholds = metrics.roc_curve([0, 1, 1, 0, 1], [0.1, 0.3, 0.6, 0.7, 1.0])
print(metrics.auc(x, y))
print(x, y, threshholds)
