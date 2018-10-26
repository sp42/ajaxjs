package com.ajaxjs.util.io;

import com.ajaxjs.Component;

public interface IoFile extends Component {
	/**
	 * 打开文件，返回其文本内容
	 * 
	 * @param path 文件磁盘路径
	 * @return 文件内容
	 */
	public String open(String path);

	/**
	 * 删除文件或目录
	 * 
	 * @param target 源文件
	 * @return 是否操作成功
	 */
	public boolean delete(String target);

	/**
	 * 复制文件
	 * 
	 * @param target 源文件
	 * @param dest 目的文件/目录，如果最后一个为目录，则不改名，如果最后一个为文件名，则改名
	 * @return 是否操作成功
	 */
	public boolean copy(String target, String dest);

	/**
	 * 移动文件
	 * 
	 * @param target 源文件
	 * @param dest 目的文件/目录，如果最后一个为目录，则不改名，如果最后一个为文件名，则改名
	 * @return 是否操作成功
	 */
	public boolean move(String target, String dest);
}
