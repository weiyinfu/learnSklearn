from sklearn.preprocessing import OneHotEncoder
import numpy as np

"""
onehot encoder可以将属性进行独热编码
"""
enc = OneHotEncoder()
attr_cnt = 4
attr_value_cnt = np.random.randint(2, 4, attr_cnt)
train = [[np.random.randint(0, attr_value_cnt[j]) for j in range(attr_cnt)] for i in range(10)]
test = [[np.random.randint(0, attr_value_cnt[j]) for j in range(attr_cnt)] for i in range(2)]
enc.fit(train)
print(enc.n_values, enc.n_values_)  # 各个属性的取值个数
t = enc.transform(test)
print(test)
print('=' * 10)
print(t.toarray())
