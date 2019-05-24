package com.ajaxjs.lru;

public class LRUCache_1<T> extends LinkedList<T> {
	int max = 5;

	/**
	 * 设置最大缓存数量
	 *
	 * @param max
	 */
	public LRUCache_1(int max) {
		this.max = max;
	}

	/**
	 * 从链表中查询数据， 如果有数据，则将该数据插入链表头部并删除原数据
	 *
	 * @param data
	 * @return
	 */
	public T get(T data) {
		T result = find(data);

		if (result != null) {
			remove(data);
			addFirst(data);
			return result;
		}

		return null;
	}

	/**
	 * 数据在链表中是否存在？ 存在，将其删除，然后插入链表头部 
	 * 不存在，缓存是否满了？ 满了，删除链表尾结点，将数据插入链表头部 
	 * 未满，直接将数据插入链表头部
	 * 时间复杂度O(n)
	 * 
	 * @param data
	 */
	public void put(T data) {
		boolean isExist = remove(data);
		if (isExist) {
			addFirst(data);
		} else {
			if (size < max) {
				addFirst(data);
			} else {
				removeLast();
				addFirst(data);
			}
		}
	}
}
