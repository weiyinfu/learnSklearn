import json
import numpy as np
from sklearn.datasets import load_files
from sklearn.feature_extraction.text import CountVectorizer, TfidfTransformer
from sklearn.model_selection import train_test_split
from sklearn.naive_bayes import MultinomialNB
from chinese_tokenizer.tokenizer import Tokenizer
from sklearn.pipeline import Pipeline
from sklearn.externals import joblib

jie_ba_tokenizer = Tokenizer().jie_ba_tokenizer


def foo():
    # 加载数据集
    training_data = load_files('./data', encoding='utf-8')
    # x_train txt内容 y_train 是类别（正 负 中 ）
    x_train, x_test, y_train, y_test = train_test_split(training_data.data, training_data.target)
    print('开始建模.....')
    with open('training_data.target', 'w', encoding='utf-8') as f:
        f.write(json.dumps(training_data.target_names))
    # 矢量化 实例化 tokenizer参数是用来对文本进行分词的函数（就是上面我们结巴分词）
    count_vect = CountVectorizer(tokenizer=jie_ba_tokenizer)
    # 特征提取 实例化
    tfidf_transformer = TfidfTransformer()
    # 切词过后 矢量化
    X_train_counts = count_vect.fit_transform(x_train)
    # 特征抽取
    X_train_tfidf = tfidf_transformer.fit_transform(X_train_counts)
    print('正在训练分类器.....')
    # 多项式贝叶斯分类器训练
    clf = MultinomialNB().fit(X_train_tfidf, y_train)
    # 保存分类器（好在其它程序中使用）
    # joblib.dump(clf, 'model.pkl')
    # 保存矢量化（坑在这儿！！需要使用和训练器相同的 矢量器 不然会报错！！！！！！ 提示 ValueError dimension mismatch··）
    # pickle.dump(count_vect, open('count_vect', 'wb'))
    joblib.dump(count_vect, 'count_vect.pkl')
    print("分类器的相关信息：")
    print(clf)
    # clf = joblib.load('model.pkl')
    # count_vect = joblib.load('count_vect')
    testing_data = load_files('./predict_test', encoding='utf-8')
    # target_names = json.loads(open('training_data.target', 'r', encoding='utf-8').read())
    #     # 字符串处理
    # count_vect = CountVectorizer(tokenizer=jieba_tokenizer)
    tfidf_transformer1 = TfidfTransformer()
    X_new_counts = count_vect.transform(testing_data.data)
    X_new_tfidf = tfidf_transformer1.fit_transform(X_new_counts)
    # 进行预测
    predicted = clf.predict(X_new_tfidf)

    print("识别率：", np.mean(predicted == testing_data.target))

    # for title, category in zip(testing_data.filenames, predicted):
    #     # print('%r => %s' % (title, target_names[category]))
    #     print('%r => %s' % (title, training_data.target_names[category]))


def pipeline_test():
    training_data = load_files("./data", encoding='utf-8')
    x_train, _, y_train, _ = train_test_split(training_data.data, training_data.target)
    text_clf_pipeline = Pipeline([
        ('vect', CountVectorizer(tokenizer=jie_ba_tokenizer)),
        ('tfidf', TfidfTransformer()),
        ('clf', MultinomialNB())
    ])
    print("开始训练：...........")
    text_clf = text_clf_pipeline.fit(x_train, y_train)
    testing_data = load_files("./predict_test", encoding='utf-8')
    predicted = text_clf.predict(testing_data.data)
    accuracy = np.mean(predicted == testing_data.target)
    with open('pipeline_training_data.target', 'w', encoding='utf-8') as f:
        f.write(json.dumps(training_data.target_names))
    print("贝叶斯准确率为：", accuracy)
    joblib.dump(text_clf, 'pipeline_model.pkl')


if __name__ == '__main__':
    pipeline_test()