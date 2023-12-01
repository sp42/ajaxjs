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

/**
 * 状态常量表
 */
public interface States {
    /**
     * 目标状态转换操作列表
     * BGN ARRBV ARRAV OBJBK OBJAK OBJBV OBJAV VAL EOF ERR
     */

    /**
     * 开始态
     */
    State BGN = new State(0, "解析开始");

    /**
     * 数组值前态
     */
    State ARRBV = new State(1, "数组待值");

    /**
     * 数组值后态
     */
    State ARRAV = new State(2, "数组得值", Operator.getMethod("arrav"));

    /**
     * 对象键前态
     */
    State OBJBK = new State(3, "对象待键");

    /**
     * 对象键后态
     */
    State OBJAK = new State(4, "对象得键", Operator.getMethod("objak"));

    /**
     * 对象值前态
     */
    State OBJBV = new State(5, "对象待值");

    /**
     * 对象值后态
     */
    State OBJAV = new State(6, "对象得值", Operator.getMethod("objav"));

    /**
     * 结果态
     */
    State VAL = new State(7, "得最终值", Operator.getMethod("val"));

    /**
     * 结束态
     */
    State EOF = new State(8, "解析结束");

    /**
     * 错误态
     */
    State ERR = new State(9, "异常错误");

    /**
     * 状态矩阵
     */
    State[][] states = {
            /*INPUT——    STR NUM DESC SPLIT ARRS OBJS ARRE OBJE FALSE TRUE NIL BGN*/
            /* BGN */  {VAL, VAL, ERR, ERR, ARRBV, OBJBK, ERR, ERR, VAL, VAL, VAL, BGN},
            /* ARRBV */{ARRAV, ARRAV, ERR, ERR, ARRBV, OBJBK, VAL, ERR, ARRAV, ARRAV, ARRAV, ERR},
            /* ARRAV */{ERR, ERR, ERR, ARRBV, ERR, ERR, VAL, ERR, ERR, ERR, ERR, ERR},
            /* OBJBK */{OBJAK, OBJAK, ERR, ERR, ERR, ERR, ERR, VAL, ERR, ERR, ERR, ERR},
            /* OBJAK */{ERR, ERR, OBJBV, ERR, ERR, ERR, ERR, ERR, ERR, ERR, ERR, ERR},
            /* OBJBV */{OBJAV, OBJAV, ERR, ERR, ARRBV, OBJBK, ERR, ERR, OBJAV, OBJAV, OBJAV, ERR},
            /* OBJAV */{ERR, ERR, ERR, OBJBK, ERR, ERR, ERR, VAL, ERR, ERR, ERR, ERR},
            /*VAL*/{},//没有后续状态,遇见此状态时弹出状态栈中的状态计算当前状态,占位，方便后期添加
            /*EOF*/{},//没有后续状态，占位，方便后期添加
            /*ERR*/{}//没有后续状态，占位，方便后期添加
    };
}
