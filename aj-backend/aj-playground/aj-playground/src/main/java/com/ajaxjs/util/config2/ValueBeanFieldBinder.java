package com.ajaxjs.util.config2;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

class ValueBeanFieldBinder {

	/**
	 * Value placeholder / Value SpringEL Expression / ConfigurationProperties
	 * annotation prefix
	 */
	private String expr;

	/**
	 * Reference of the Dynamic Bean instance
	 */
	private WeakReference<Object> beanRef;

	/**
	 * Record the bound field, only for {@literal @}Value fields binding case
	 */
	private Field dynamicField;

	/**
	 * name of the Spring bean
	 */
	private String beanName;

	ValueBeanFieldBinder(String expr, Field dynamicField, Object bean, String beanName) {
		this.beanRef = new WeakReference<>(bean);
		this.expr = expr;
		this.dynamicField = dynamicField;
		this.beanName = beanName;
	}

	public String getExpr() {
		return expr;
	}

	public void setExpr(String expr) {
		this.expr = expr;
	}

	public WeakReference<Object> getBeanRef() {
		return beanRef;
	}

	public void setBeanRef(WeakReference<Object> beanRef) {
		this.beanRef = beanRef;
	}

	public Field getDynamicField() {
		return dynamicField;
	}

	public void setDynamicField(Field dynamicField) {
		this.dynamicField = dynamicField;
	}

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}
}