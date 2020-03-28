/**
 * Copyright Sp42 frank@ajaxjs.com
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
package com.ajaxjs.temp;

/**
 * 发送短信
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public interface SMS {
	/**
	 * 发送短信
	 * 
	 * @param message
	 *            短信内容
	 * @return 是否成功
	 */
	public boolean send(Message message);
}
