/**
 * Copyright Sp42 frank@ajaxjs.com
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ajaxjs.jsonparser.lexer;

import com.ajaxjs.jsonparser.JsonParseException;

/**
 * 字符串 Token
 *
 * @author sp42 frank@ajaxjs.com
 */
public class StringToken extends Token {
    /**
     * 创建字符串 Token
     *
     * @param value 这是 JSON 字符串上的那个原始值。
     */
    public StringToken(String value) {
        super(0, "STR", "字符串", null, value);
    }

    /**
     * 转义
     *
     * @param str 输入的字符
     * @return 转义后的结果
     */
    public static String unescape(String str) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);

            if (c == '\\') {
                c = str.charAt(++i);// 游标前进一个字符

                switch (c) {
                    case '"':
                        sb.append('"');
                        break;
                    case '\\':
                        sb.append('\\');
                        break;
                    case '/':
                        sb.append('/');
                        break;
                    case 'b':
                        sb.append('\b');
                        break;
                    case 'f':
                        sb.append('\f');
                        break;
                    case 'n':
                        sb.append('\n');
                        break;
                    case 'r':
                        sb.append('\r');
                        break;
                    case 't':
                        sb.append('\t');
                        break;
                    case 'u':
                        String hex = str.substring(i + 1, i + 5);
                        sb.append((char) Integer.parseInt(hex, 16));
                        i += 4;
                        break;
                    default:
                        throw new JsonParseException("“\\”后面期待“\"\\/bfnrtu”中的字符，结果得到“" + c + "”");
                }
            } else
                sb.append(c);
        }

        return sb.toString();
    }
}
