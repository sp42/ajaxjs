package com.ajaxjs.user.rbac.model;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ajaxjs.user.rbac.RbacConstant.RESOURCES_GROUP;
import com.ajaxjs.util.map.JsonHelper;
import com.ajaxjs.util.spring.DiContextUtil;

/**
 * 权限资源
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class Resource {
	/**
	 * id
	 */
	private Integer id;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 说明
	 */
	private String desc;

	/**
	 * 分类
	 */
	private RESOURCES_GROUP group;

	/**
	 * 创建一个资源描述
	 * 
	 * @param name  名称
	 * @param group 分类
	 */
	public Resource(String name, RESOURCES_GROUP group) {
		this.name = name;
		this.group = group;

		setId();
	}

	public Resource(String name, RESOURCES_GROUP group, String desc) {
		this.name = name;
		this.group = group;
		this.desc = desc;

		setId();
	}

	/**
	 * 自增 id
	 */
	private void setId() {
		Field[] fields;
		Resources res = DiContextUtil.getBean(Resources.class);// 是否有注入的扩展

		if (res != null)
			fields = res.getClass().getFields();
		else
			fields = Resources.class.getFields();

		int i = 1;
		for (Field field : fields) {
			int modifiers = field.getModifiers();
			if (isResourceField(field, modifiers) && (getResource(field) != null)) {
				// 是常量
				i++;
			}
		}

		if (i > 64)
			throw new IllegalStateException("当前系统限制：资源数量不能超过 64");

		id = i; // 累加 id
	}

	/**
	 * 反射获取资源字段
	 * 
	 * @param field
	 * @return
	 */
	private static Resource getResource(Field field) {
		try {
			Object obj = field.get(null);
			if (obj != null && obj instanceof Resource)
				return (Resource) obj;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 是否常量字段，以及是否 Resource 类型的
	 * 
	 * @param field
	 * @param modifiers
	 * @return
	 */
	private static boolean isResourceField(Field field, int modifiers) {
		return Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers) && (field.getGenericType() == Resource.class);
	}

	/**
	 * 反射获取所有资源（默认当前资源）
	 * 
	 * @return 资源 JSON
	 */
	public static String getAllResources() {
		return getAllResources(Resources.class);
	}

	/**
	 * 反射获取所有资源
	 * 
	 * @param clz 资源常量类
	 * @return 资源 JSON
	 */
	public static String getAllResources(Class<? extends Resources> clz) {
		Field[] fields = clz.getFields();
		List<Map<String, Object>> list = new ArrayList<>();

		for (Field field : fields) {
			int modifiers = field.getModifiers();

			if (Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers)) {
				Resource res = getResource(field);
				if (res == null)
					continue;

				Map<String, Object> map = new HashMap<>();
				map.put("id", res.getId());
				map.put("name", res.getName());
				map.put("group", res.getGroup().getName());

				list.add(map);

			}
		}

		return JsonHelper.toJson(list);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public RESOURCES_GROUP getGroup() {
		return group;
	}

	public void setGroup(RESOURCES_GROUP group) {
		this.group = group;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
