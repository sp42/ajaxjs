package com.ajaxjs.util;

/**
 * 二叉查找树 T 节点元素类型
 */
public class BinarySearchTree<T extends Comparable<? super T>> {
	/*
	 * 节点类
	 */
	private static class BinaryNode<T> {
		T element;
		BinaryNode<T> left;
		BinaryNode<T> right;

		@SuppressWarnings("unused")
		BinaryNode(T element) {
			this(element, null, null);
		}

		BinaryNode(T element, BinaryNode<T> left, BinaryNode<T> right) {
			this.element = element;
			this.left = left;
			this.right = right;
		}
	}

	/*
	 * 根
	 */
	private BinaryNode<T> root;

	/*
	 * 插入
	 */
	public void insert(T ele) {
		root = insert(ele, root);
	}

	public void remove(T ele) {
		root = remove(ele, root);
	}

	private BinaryNode<T> insert(T ele, BinaryNode<T> t) {
		if (t == null)
			return new BinaryNode<T>(ele, null, null);

		int result = ele.compareTo(t.element);
		if (result < 0)
			t.left = insert(ele, t.left);
		else if (result > 0)
			t.right = insert(ele, t.right);

		return t;
	}

	private BinaryNode<T> remove(T ele, BinaryNode<T> t) {
		if (t == null)
			return t;

		int result = ele.compareTo(t.element);
		if (result < 0) {
			return remove(ele, t.left);
		} else if (result > 0) {
			return remove(ele, t.right);
		} else if (t.left != null && t.right != null) {
			t.element = findMin(t.right).element;
			t.right = remove(t.element, t.right);
		}
		return t;
	}

	private BinaryNode<T> findMin(BinaryNode<T> t) {
		if (t != null) {
			while (t.left != null)
				t = t.left;
		}

		return t;
	}

	/*
	 * 二叉查找树中是否包含此节点
	 */
	public boolean contains(T ele, BinaryNode<T> t) {
		if (t == null)
			return false;

		int result = ele.compareTo(t.element);

		if (result < 0)
			return contains(ele, t.left);
		else if (result > 0)
			return contains(ele, t.right);

		return true;
	}
}