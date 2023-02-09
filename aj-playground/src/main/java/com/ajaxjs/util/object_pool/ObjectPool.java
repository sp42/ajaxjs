package com.ajaxjs.util.object_pool;

import java.util.Enumeration;
import java.util.Vector;

public abstract class ObjectPool<T> {
	public static int numObjects = 10; // 对象池的大小

	public static int maxObjects = 50; // 对象池最大的大小

	protected Vector<PooledObject<T>> objects; // 存放对象池中对象的向量(PooledObject 类型)

	/**
	 * 创建一个对象池
	 */
	public synchronized void createPool() { // 确保对象池没有创建。如果创建了，保存对象的向量 objects 不会为空
		if (objects != null)
			return; // 如果己经创建，则返回

		// 创建保存对象的向量 , 初始时有 0 个元素
		objects = new Vector<PooledObject<T>>();

		for (int i = 0; i < numObjects; i++)
			objects.addElement(create());
	}

	/**
	 * 创建对象的模板方法
	 * 
	 * @return 被包装的对象实例
	 */
	public abstract PooledObject<T> create();

	/**
	 * 获取对象
	 * 
	 * @return
	 */
	public synchronized T getObject() { // 确保对象池己被创建
		if (objects == null)
			return null; // 对象池还没创建，则返回 null

		T t = getFreeObject(); // 获得一个可用的对象

		// 如果目前没有可以使用的对象，即所有的对象都在使用中
		while (t == null) {
			wait(250);
			t = getFreeObject(); // 重新再试，直到获得可用的对象，如果
			// getFreeObject() 返回的为 null，则表明创建一批对象后也不可获得可用对象
		}

		return t;// 返回获得的可用的对象
	}

	/**
	 * 本函数从对象池对象 objects 中返回一个可用的的对象，如果当前没有可用的对象，则创建几个对象，并放入对象池中。
	 * 如果创建后，所有的对象都在使用中，则返回 null
	 * 
	 * @return
	 */
	private T getFreeObject() { // 从对象池中获得一个可用的对象
		T obj = findFreeObject();

		if (obj == null) {
			createObjects(10); // 如果目前对象池中没有可用的对象，创建一些对象
			// 重新从池中查找是否有可用对象
			obj = findFreeObject(); // 如果创建对象后仍获得不到可用的对象，则返回 null

			if (obj == null)
				return null;
		}

		return obj;
	}

	/**
	 * 按照指定数目创建对象
	 * 
	 * @param increment
	 */
	public void createObjects(int increment) {
		for (int i = 0; i < increment; i++) {
			if (objects.size() > maxObjects)
				return;

			objects.addElement(create());
		}
	}

	/**
	 * 查找对象池中所有的对象，查找一个可用的对象
	 * 
	 * @return 对象。如果没有可用的对象，返回 null
	 */
	private T findFreeObject() {
		T obj = null;
		PooledObject<T> pObj = null; // 获得对象池向量中所有的对象
		Enumeration<PooledObject<T>> enumerate = objects.elements(); // 遍历所有的对象，看是否有可用的对象

		while (enumerate.hasMoreElements()) {
			pObj = (PooledObject<T>) enumerate.nextElement(); // 如果此对象不忙，则获得它的对象并把它设为忙

			if (!pObj.isBusy()) {
				obj = pObj.getObject();
				pObj.setBusy(true);
			}
		}

		return obj;// 返回找到到的可用对象
	}

	/**
	 * 此函数返回一个对象到对象池中，并把此对象置为空闲。 所有使用对象池获得的对象均应在不使用此对象时返回它。
	 * 
	 * @param obj
	 */
	public void returnObject(T obj) { // 确保对象池存在，如果对象没有创建（不存在），直接返回
		if (objects == null)
			return;

		PooledObject<T> pObj = null;
		Enumeration<PooledObject<T>> enumerate = objects.elements(); // 遍历对象池中的所有对象，找到这个要返回的对象对象

		while (enumerate.hasMoreElements()) {
			pObj = (PooledObject<T>) enumerate.nextElement(); // 先找到对象池中的要返回的对象对象

			if (obj == pObj.getObject()) { // 找到了设置此对象为空闲状态
				pObj.setBusy(false);
				break;
			}
		}
	}

	/**
	 * 关闭对象池中所有的对象，并清空对象池。
	 */
	public synchronized void closeObjectPool() { // 确保对象池存在，如果不存在，返回
		if (objects == null)
			return;

		PooledObject<T> pObj = null;
		Enumeration<PooledObject<T>> enumerate = objects.elements();

		while (enumerate.hasMoreElements()) {
			pObj = (PooledObject<T>) enumerate.nextElement(); // 如果忙，等 0.5 秒

			if (pObj.isBusy())
				wait(500); // 等

			objects.removeElement(pObj);// 从对象池向量中删除它
		}

		objects = null;// 置对象池为空
	}

	/**
	 * 使程序等待给定的毫秒数
	 * 
	 * @param mSeconds
	 */
	private static void wait(int mSeconds) {
		try {
			Thread.sleep(mSeconds);
		} catch (InterruptedException e) {
		}
	}
}