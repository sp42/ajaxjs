/**
 * Copyright Sp42 frank@ajaxjs.com Licensed under the Apache License, Version
 * 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package com.ajaxjs.login.weibo;

import com.ajaxjs.framework.ClientAccessFullInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 微博登录的配置
 *
 * @author sp42 frank@ajaxjs.com
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WeiboConfig extends ClientAccessFullInfo {
    /**
     * 用户登录通过之后调转回来的地址
     */
    private String loginUrl;
}
