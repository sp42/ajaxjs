package com.ajaxjs.lru;

public class LRUCache_2<K, V> {
	int cap;

	int size = 0;

	private Node<K, V> head;
	private Node<K, V> tail;

	/**
	 * 设定容量
	 * 
	 * @param cap 设定容量
	 * @param db
	 */
	public LRUCache_2(int cap) {
		this.cap = cap;
	}

	public V get(K key) {
		Node<K, V> previous = null; // ahead 是之前的那个节点

		Node<K, V> temp = head;
		while (temp != null) {
			if (temp.key.equals(key)) {
				if (temp.next != null) // 找到了匹配的缓存
					moveToLast(previous, temp);

				return temp.value;
			}

			previous = temp;
			temp = temp.next;
		}

		return null;
	}

	private void moveToLast(Node<K, V> previous, Node<K, V> target) {
		if (target == tail)
			return;

		if (head == target) {
			head = target.next;// 头部不当头了，后面的上
		} else {
			previous.next = target.next; // target 被抽出来了，后面的接上
		}

		tail.next = target;// 续上新的， 放在最后插入
		tail = target;
		target.next = null;
	}

	public V put(K key, V value) {
		if (head == null) { // 空，直接插入
			tail = head = new Node<>(key, value);
			size++;
			return value;
		}

		Node<K, V> temp = head;
		while (temp != null) {
			if (temp.key.equals(key)) { // 已经存在
				if (temp.value.equals(value)) { // 没有不同
					return value;
				} else {
					V oldValue = temp.value; // 进行了值的替换
					temp.value = value;

					return oldValue;
				}
			}

			temp = temp.next;
		}

		// 找不到，是新成员
		temp = new Node<>(key, value); // 加入到尾部
		tail.next = temp;
		tail = temp;
		size++;

		checkCap();

		return value;
	}

	private void checkCap() {
		if (size > cap) {
			head = head.next;// 进行淘汰,摘除开头
			size--;
		}
	}

	public Node<K, V> peek() {
		return head;
	}

	public static class Node<K, V> {
		K key;
		V value;
		Node<K, V> next;

		Node(K key, V value) {
			this.key = key;
			this.value = value;
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		while (head != null) {
			sb.append(head.key + " " + head.value);
			head = head.next;
		}

		return sb.toString();
	}
}
