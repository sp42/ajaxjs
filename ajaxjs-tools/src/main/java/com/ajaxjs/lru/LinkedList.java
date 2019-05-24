package com.ajaxjs.lru;

public class LinkedList<T> {
	public Node<T> first;
	public int size;

	/**
	 * 增加一个节点到链表头部
	 */
	public void addFirst(T data) {
		Node<T> node = new Node<>(data);
		if (first == null) {
			first = node;
		} else {
			node.next = first; // 下一个是当前头部的
			first = node;
		}

		size++;
	}

	/**
	 * 增加一个节点到链表尾部
	 */
	public void addLast(T data) {
		if (first == null) {
			addFirst(data);
		} else {
			Node<T> temp = first;
			while (temp.next != null) {
				temp = temp.next; // 遍历得到最后一个元素
			}
			
			temp.next = new Node<>(data);
			size++;
		}
	}
	
	/**
	 * 查找指定元素
	 * 
	 * @param data
	 * @return
	 */
	public T find(T data) {
		if (size == 0)
			return null;

		Node<T> temp = first;
		while (!temp.data.equals(data)) {
			if (temp.next == null) {// 到达最后，找不到匹配的元素，返回 null
				return null;
			} else {
				temp = temp.next;
			}
		}

		return temp.data;
	}

	/**
	 * 删除尾部元素
	 * 
	 * @return
	 */
	public void removeLast() {
		if (size == 0)
			return;

		Node<T> previous = first; // 要删除元素的话，得先得到该元素的前面一个

		Node<T> temp = first;
		while (temp.next != null) {
			previous = temp;
			temp = temp.next;
		}

		previous.next = null;
		size--;
	}

	/**
	 * 删除指定元素
	 */
	public boolean remove(T data) {
		if (size == 0)
			return false;

		Node<T> previous = first;

		Node<T> temp = first; // 目标元素
		while (!temp.data.equals(data)) {
			if (temp.next == null) { // 到达最后，找不到匹配的元素，返回吧
				return false;
			} else {
				previous = temp;
				temp = temp.next;
			}
		}

		if (temp == first)
			first = temp.next;
		else
			previous.next = temp.next; // 删除目标元素，后面的继续跟着上

		size--;
		
		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Node<T> temp = first;

		while (temp.next != null) {
			sb.append(temp.data + "->");
			temp = temp.next;
		}

		sb.append(temp.data);
		return sb.toString();
	}

	static class Node<T> {
		Node<T> next;
		T data;

		public Node(T data) {
			this.data = data;
		}
	}
}
