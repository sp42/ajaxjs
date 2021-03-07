/**
 * 版权所有 2017 Sp42 frank@ajaxjs.com 根据 2.0 版本 Apache 许可证("许可证")授权；
 * 根据本许可证，用户可以不使用此文件。 用户可从下列网址获得许可证副本：
 * http://www.apache.org/licenses/LICENSE-2.0
 * 除非因适用法律需要或书面同意，根据许可证分发的软件是基于"按原样"基础提供，
 * 无任何明示的或暗示的保证或条件。详见根据许可证许可下，特定语言的管辖权限和限制。
 */
package com.ajaxjs.framework;

/**
 * 获取配置。base 层不提供实现，于是给出一个接口耦合
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public interface GetConfig {
	/**
	 * 读取配置并转换其为字符串类型。仅对扁平化后的配置有效，所以参数必须是扁平化的 aaa.bbb.ccc 格式。
	 * 
	 * @param key 配置键值
	 * @return 配置内容
	 */
	public String getString(String key);
}
