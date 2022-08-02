import numpy as np
from sklearn.datasets import load_iris
from sklearn.ensemble.forest import RandomForestClassifier
from sklearn.model_selection import train_test_split

data = load_iris()
x = data.data
y = data.target

clf = RandomForestClassifier(n_estimators=200)
train_x, test_x, train_y, test_y = train_test_split(x, y, train_size=0.7)
print(train_x.shape, train_y.shape, test_x.shape, test_y.shape)
clf.fit(train_x, train_y)
mine = clf.predict(test_x)
right_count = np.count_nonzero(mine == test_y)
print(right_count, right_count / len(test_y) * 100)
