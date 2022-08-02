from sklearn import datasets,tree,metrics
import pydot
"""

"""
eye=tree.DecisionTreeClassifier(criterion="entropy")
iris=datasets.load_iris()
x,y=iris.data,iris.target
eye.fit(x,y)
yy=eye.predict(x)
print(metrics.accuracy_score(y,yy))
dot=tree.export_graphviz(eye,out_file=None)#outfile=None则返回数据
pydot.graph_from_dot_data(dot)[0].write("haha.jpg",format="jpg")
print(eye.tree_.node_count)
print(eye.tree_.max_depth)
print(dir(eye.tree_))
print(eye.tree_.n_node_samples)