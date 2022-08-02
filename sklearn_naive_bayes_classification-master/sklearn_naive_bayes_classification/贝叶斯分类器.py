# -*- coding: utf-8 -*-
# @Time    : 2017/8/23 18:02
# @Author  : 哎哟卧槽
# @Site    : 
# @File    : 贝叶斯分类器.py
# @Software: PyCharm

import json
from sklearn.datasets import load_files
from sklearn.feature_extraction.text import TfidfTransformer
from sklearn.externals import joblib



def foo():
    clf = joblib.load('model.pkl')
    count_vect = joblib.load('count_vect.pkl')
    testing_data = load_files('./predict_test', encoding='utf-8')
    target_names = json.loads(open('training_data.target', 'r', encoding='utf-8').read())
    #     # 字符串处理

    tfidf_transformer = TfidfTransformer()
    # 将文本集合转为矩阵
    X_new_counts = count_vect.transform(testing_data.data)
    X_new_tfidf = tfidf_transformer.fit_transform(X_new_counts)
    # 进行预测
    predicted = clf.predict(X_new_tfidf)
    for title, category in zip(testing_data.filenames, predicted):
        print('%r => %s' % (title, target_names[category]))


def foo2():
    testing_data = load_files('./predict_test', encoding='utf-8')
    clf = joblib.load('./pipeline_model.pkl')
    target_names = json.loads(open('pipeline_training_data.target', 'r', encoding='utf-8').read())
    clf.predict(testing_data.data)
    predicted = clf.predict(testing_data.data)
    for title, category in zip(testing_data.filenames, predicted):
        print('%r => %s' % (title, target_names[category]))


if __name__ == '__main__':
    foo2()