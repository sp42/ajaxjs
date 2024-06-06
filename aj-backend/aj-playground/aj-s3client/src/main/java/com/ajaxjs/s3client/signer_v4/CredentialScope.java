/*
  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
  the License. You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
  an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
  specific language governing permissions and limitations under the License.

  Copyright 2016 the original author or authors.
 */
package com.ajaxjs.s3client.signer_v4;

import lombok.Data;

/**
 *
 */
@Data
public class CredentialScope {
    public static final String TERMINATION_STRING = "aws4_request";

    private final String dateWithoutTimestamp;

    private final String service;

    private final String region;

    public CredentialScope(String date, String service, String region) {
        this.dateWithoutTimestamp = date.substring(0, 8); // 提取日期中的日期部分，去掉时间戳;
        this.service = service;
        this.region = region;
    }

    /**
     * 生成并返回一个由日期、区域、服务和终止字符串组成的路径
     *
     * @return 返回的字符串格式为"日期/区域/服务/终止字符串"
     */
    public String get() {
        return dateWithoutTimestamp + "/" + region + "/" + service + "/" + TERMINATION_STRING;
    }
}
