package com.ajaxjs.lru;

import java.util.HashMap;

public class LRUCache<K, V> {
	static class Node<K, V> {
		K key;
		V value;
		Node<K, V> pre;
		Node<K, V> next;

		public Node(K key, V value) {
			this.key = key;
			this.value = value;
		}
	}

	private HashMap<K, Node<K, V>> map;
	private int capicity, count;
	private Node<K, V> head, tail;

	/**
	 * 
	 * @param capacity 缓存容量
	 */
	public LRUCache(int capacity) {
		this.capicity = capacity;
		map = new HashMap<>();
		head = new Node<>(null, null);
		tail = new Node<>(null, null);

		head.next = tail;
		head.pre = null;
		tail.pre = head;
		tail.next = null;
		count = 0;
	}

	/**
	 * 加到头部
	 * 
	 * @param node
	 */
	public void addToHead(Node<K, V> node) {
		node.next = head.next;
		node.next.pre = node;
		node.pre = head;
		head.next = node;
	}

	/**
	 * 删除节点
	 * 
	 * @param node
	 */
	public void deleteNode(Node<K, V> node) {
		node.pre.next = node.next;
		node.next.pre = node.pre;
	}

	public V get(int key) {
		if (map.get(key) != null) {
			Node<K, V> node = map.get(key);
			V result = node.value;
			deleteNode(node);
			addToHead(node);
			
			return result;
		}

		return null; // 找不到
	}

	public void set(K key, V value) {
		System.out.println("Going to set the (key, value) : (" + key + ", " + value + ")");
		if (map.get(key) != null) { // 已存在
			Node<K, V> node = map.get(key);
			node.value = value;
			deleteNode(node);
			addToHead(node);
		} else {// 插入新的
			Node<K, V> node = new Node<>(key, value);
			map.put(key, node);
			
			if (count < capicity) {
				count++;
				addToHead(node);
			} else { // 满了
				map.remove(tail.pre.key);
				deleteNode(tail.pre);
				addToHead(node);
			}
		}
	}
}
