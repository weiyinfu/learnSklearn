import heapq

import numpy as np


class Tree_Node:
    def __init__(self, left, right, data, split_dim):
        pass


def build_tree(data, dim, depth):
    """
    建立KD树

    Parameters
    ----------
    data:numpy.array
        需要建树的数据集
    dim:int
        数据集特征的维数
    depth:int
        当前树的深度
    Returns
    -------
    tree_node:tree_node namedtuple
            　树的跟节点
    """
    size = data.shape[0]
    if size == 0:
        return None
    # 确定本层划分参照的特征
    split_dim = depth % dim
    mid = size / 2
    # 按照参照的特征划分数据集
    r_indx = np.argpartition(data[:, split_dim], mid)
    data = data[r_indx, :]
    left = data[0: mid]
    right = data[mid + 1: size]
    mid_data = data[mid]
    # 分别递归建立左右子树
    left = build_tree(left, dim, depth + 1)
    right = build_tree(right, dim, depth + 1)
    # 返回树的根节点
    return Tree_Node(left=left,
                     right=right,
                     data=mid_data,
                     split_dim=split_dim)


def search_n(cur_node, data, queue, k):
    """
    查找Ｋ近邻，最后queue中的k各值就是k近邻

    Parameters
    ----------
    cur_node:tree_node namedtuple
            当前树的跟节点
    data:numpy.array
        数据
    queue:Queue.PriorityQueue
         记录当前k个近邻，距离大的先输出
    k:int
        查找的近邻个数
    """
    # 当前节点为空，直接返回上层节点
    if cur_node is None:
        return None
    if type(data) is not np.array:
        data = np.asarray(data)
    cur_data = cur_node.data
    # 得到左右子节点
    left = cur_node.left
    right = cur_node.right
    # 计算当前节点与数据点的距离
    distance = np.sum((data - cur_data) ** 2) ** .5
    cur_split_dim = cur_node.split_dim
    flag = False  # 标记在回溯时是否需要进入另一个子树查找
    #  根据参照的特征来判断是先进入左子树还是右子树
    if data[cur_split_dim] > cur_data[cur_split_dim]:
        tmp = right
        right = left
        left = tmp
    # 进入子树查找
    search_n(left, data, queue, k)
    #  下面是回溯过程
    #  当队列中没有k个近邻时，直接将当前节点入队，并进入另一个子树开始查找
    if len(queue) < k:

        neg_distance = -1 * distance
        heapq.heappush(queue, (neg_distance, cur_node))
        flag = True
    else:
        #  得到当前距离数据点第K远的节点
        top_neg_distance, top_node = heapq.heappop(queue)
        #  如果当前节点与数据点的距离更小，则更新队列（当前节点入队，原第k远的节点出队）
        if - 1 * top_neg_distance > distance:
            top_neg_distance, top_node = -1 * distance, cur_node
        heapq.heappush(queue, (top_neg_distance, top_node))
        #  判断另一个子树内是否可能存在跟数据点的距离比当前第K远的距离更小的节点
        top_neg_distance, top_node = heapq.heappop(queue)
        if abs(data[cur_split_dim] - cur_data[cur_split_dim]) < -1 * top_neg_distance:
            flag = True
        heapq.heappush(queue, (top_neg_distance, top_node))
    # 进入另一个子树搜索
    if flag:
        search_n(right, data, queue, k)
