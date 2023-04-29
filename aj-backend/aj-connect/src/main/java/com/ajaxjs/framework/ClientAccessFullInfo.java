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
package com.ajaxjs.framework;

import lombok.Data;

/**
 * 客户端访问的基本两个字段: App Id、App 密钥
 *
 * @author Frank Cheung
 */
@Data
public abstract class ClientAccessFullInfo {
    /**
     * App Id
     */
    private String accessKeyId;

    /**
     * App 密钥
     */
    private String accessSecret;
}
