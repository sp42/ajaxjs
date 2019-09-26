/**
 * Copyright sp42 frank@ajaxjs.com
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
package com.ajaxjs.framework;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据字典，常量
 * 
 * @author Frank Cheung
 *
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public interface DataDict {
	public static final int PIC_NORMAL = 1;
	public static final int PIC_COVER = 2;
	public static final int PIC_in_GALLERY = 3;

	public static final Map<Integer, String> picMap = new HashMap() {
		private static final long serialVersionUID = -1L;
		{
			put(PIC_NORMAL, "普通图片");
			put(PIC_COVER, "头像/封面");
			put(PIC_in_GALLERY, "相册图片");
		}
	};
}
