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
package com.ajaxjs.jsonparser.syntax;

import com.ajaxjs.jsonparser.JsonParseException;
import com.ajaxjs.jsonparser.lexer.Lexer;
import com.ajaxjs.jsonparser.lexer.Token;
import com.ajaxjs.jsonparser.lexer.Tokens;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 语法解析的状态机 Thanks to <a href="http://blog.csdn.net/yimengqiannian/article/details/53701275">...</a>
 */
public class FMS implements States {
    /**
     * 词法分析器实例
     */
    private final Lexer lex;

    /**
     * 堆栈管理器
     */
    private final Operator opt;

    /**
     * 当前状态
     */
    private State status;

    /**
     * 创建一个状态机
     *
     * @param json JSON 字符串
     */
    public FMS(String json) {
        Objects.requireNonNull(json, "没有输入的 JSON 字符串。");

        lex = new Lexer(json);
        opt = new Operator(lex);
    }

    /**
     * 开始解析 JSON 字符串
     *
     * @return MAP|LIST
     */
    public Object parse() {
        status = BGN;
        State oldStatus = status; // 上一个状态

        Token tk;
        while ((tk = lex.next()) != Tokens.EOF) {
            if (tk == null)
                throw lex.exceptionFactory("发现不能识别的 token：" + lex.getCurChar());

            if (status == VAL || status == EOF || status == ERR)
                throw lex.exceptionFactory(String.format(strTpl, oldStatus.getDescription(), "结束", tk));

            oldStatus = status;
            status = states[oldStatus.getId()][tk.getType()];

            if (status == ERR) {
                String expectStr = ETS[oldStatus.getId()];
                throw lex.exceptionFactory(String.format(strTpl, oldStatus.getDescription(), expectStr, tk));
            }

            Method m = TKOL[tk.getType()];

            try {
                if (m != null) // 输入 Token 操作 有点像 js 的 call/apply
                    status = (State) m.invoke(opt, oldStatus, status, tk);

                m = status.getHandler();

                if (m != null) // 目标状态操作
                    status = (State) m.invoke(opt, oldStatus, status, tk);

            } catch (IllegalArgumentException e) {
                throw lex.exceptionFactory("【反射调用】传入非法参数", e);
            } catch (IllegalAccessException e) {
                throw lex.exceptionFactory("【反射调用】私有方法无法调用", e);
            } catch (InvocationTargetException e) {
                if (e.getTargetException() instanceof JsonParseException)
                    throw (JsonParseException) e.getTargetException();
                else
                    throw lex.exceptionFactory("运行时异常", e);
            }
        }

        return opt.getCurValue();
    }

    private final static String strTpl = "当前状态【 %s 】, 期待【 %s 】; 但却返回 %s";

    /**
     * Token 输入操作列表
     */
    /* INPUT —— STR NUM DESC SPLIT ARRS OBJS ARRE OBJE FALSE TRUE NIL BGN */
    private static final Method[] TKOL = {null, null, null, null, Operator.getMethod("arrs"),
            Operator.getMethod("objs"), null, null, null, null, null, null};

    // STR NUM DESC SPLIT ARRS OBJS ARRE OBJE FALSE TRUE NIL BGN
    final static String[] allText = {"字符串", "数字", Tokens.DESC.getTypeNameChinese(), Tokens.SPLIT.getTypeNameChinese(),
            Tokens.ARRS.getTypeNameChinese(), Tokens.OBJS.getTypeNameChinese(), Tokens.ARRE.getTypeNameChinese(),
            Tokens.OBJE.getTypeNameChinese(), Tokens.FALSE.getTypeNameChinese(), Tokens.TRUE.getTypeNameChinese(),
            Tokens.NIL.getTypeNameChinese(), Tokens.BGN.getTypeNameChinese(),};

    // BGN ARRBV ARRAV OBJBK OBJAK OBJBV OBJAV VAL EOF ERR
    final static String[] ETS = {getExpectStr(BGN.getId()), getExpectStr(ARRBV.getId()), getExpectStr(ARRAV.getId()),
            getExpectStr(OBJBK.getId()), getExpectStr(OBJAK.getId()), getExpectStr(OBJBV.getId()),
            getExpectStr(OBJAV.getId()), Tokens.EOF.getTypeNameChinese(), Tokens.EOF.getTypeNameChinese(),
            Tokens.EOF.getTypeNameChinese()};

    /**
     * 获取期望 Token 描述字符串
     *
     * @param stateId 状态 id
     * @return 期望 Token 描述字符串
     */
    static String getExpectStr(int stateId) {
        State[] stateArr = States.states[stateId];
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < stateArr.length; i++) {
            State s = stateArr[i];

            if (s != ERR)
                sb.append(allText[i]).append('|');
        }

        return sb.length() == 0 ? null : sb.deleteCharAt(sb.length() - 1).toString();
    }
}
