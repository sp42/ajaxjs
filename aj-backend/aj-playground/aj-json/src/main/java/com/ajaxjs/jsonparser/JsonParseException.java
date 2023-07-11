/**
 * Copyright Sp42 frank@ajaxjs.com
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ajaxjs.jsonparser;

/**
 * JSON 解析器专用异常类
 */
public class JsonParseException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * 创建一个 JSON 解析异常对象
     *
     * @param charNum charNum
     * @param lineNum 错误所在的行数
     * @param colNum  错误所在的列数
     * @param message 异常信息
     */
    public JsonParseException(int charNum, int lineNum, int colNum, String message) {
        this.charNum = charNum;
        this.colNum = colNum;
        this.lineNum = lineNum;
        this.desc = message;
    }

    /**
     * 创建一个 JSON 解析异常对象
     *
     * @param charNum charNum
     * @param lineNum 错误所在的行数
     * @param colNum  错误所在的列数
     * @param message 异常信息
     * @param cause   异常对象
     */
    public JsonParseException(int charNum, int lineNum, int colNum, String message, Throwable cause) {
        this(charNum, lineNum, colNum, message);
        this.cause = cause;
    }

    /**
     * 创建一个 JSON 解析异常对象
     *
     * @param message 异常信息
     */
    public JsonParseException(String message) {
        super(message);
    }

    private int charNum;

    private int lineNum;

    private int colNum;

    private String desc;

    private Throwable cause;

    @Override
    public String getMessage() {
        return "JsonParseException[char: " + charNum + ", line: " + lineNum + ", column: " + colNum + "]" + desc + (cause == null ? "" : cause.toString());
    }

    @Override
    public String toString() {
        return getMessage();
    }
}
