/**
 * Copyright 2015 Sp42 frank@ajaxjs.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ajaxjs.util.io;

import java.io.File;
import java.io.IOException;

/**
 * 因为泛型的缘故，不好直接使用 Stream，故特提供该类。另外提供容纳 byte 工具类的地方
 * 
 * @author Sp42 frank@ajaxjs.com
 */
public class StreamUtil extends StreamChain<StreamUtil> {

	/**
	 * 在字节数组里查找某个字节数组，找到返回&lt;=0，未找到返回-1
	 * 
	 * @param data 被搜索的内容
	 * @param search 要搜索内容
	 * @param start 搜索起始位置
	 * @return 目标位置，找不到返回-1
	 */
	public static int byteIndexOf(byte[] data, byte[] search, int start) {
		int len = search.length;

		for (int i = start; i < data.length; i++) {
			int temp = i, j = 0;
			while (data[temp] == search[j]) {
				temp++;
				j++;
				if (j == len)
					return i;
			}
		}

		return -1;
	}

	/**
	 * 在字节数组里查找某个字节数组，找到返回&lt;=0，未找到返回-1
	 * 
	 * @param data 被搜索的内容
	 * @param search 要搜索内容
	 * @return 目标位置，找不到返回-1
	 */
	public static int byteIndexOf(byte[] data, byte[] search) {
		return byteIndexOf(data, search, 0);
	}

	/**
	 * 合并两个字节数组
	 * 
	 * @param a 数组a
	 * @param b 数组b
	 * @return 新合并的数组
	 */
	public static byte[] concat(byte[] a, byte[] b) {
		byte[] c = new byte[a.length + b.length];
		System.arraycopy(a, 0, c, 0, a.length);
		System.arraycopy(b, 0, c, a.length, b.length);

		return c;
	}

	/**
	 * 获得指定文件的 byte 数组
	 * 
	 * @param file 文件对象
	 * @return 文件字节数组
	 */
	public static byte[] fileAsByte(File file) {
		FileUtil fu = new FileUtil();
		byte[] b = null;

		try {
			b = fu.setFile(file).read().inputStream2Byte().getData();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			fu.close();
		}

		return b;
	}
}