/*
 * QR Code generator library (Java)
 *
 * Copyright (c) Project Nayuki. (MIT License)
 * https://www.nayuki.io/page/qr-code-generator-library
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 * - The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 * - The Software is provided "as is", without warranty of any kind, express or
 *   implied, including but not limited to the warranties of merchantability,
 *   fitness for a particular purpose and noninfringement. In no event shall the
 *   authors or copyright holders be liable for any claim, damages or other
 *   liability, whether in an action of contract, tort or otherwise, arising from,
 *   out of or in connection with the Software or the use or other dealings in the
 *   Software.
 */

package com.ajaxjs.qr_code;

/**
 * 当提供的数据不适合任何QR码版本时抛出此异常。处理此异常的方法包括：
 * <ul>
 *   <li>如果错误纠正级别高于{@code Ecc.LOW}，则降低错误纠正级别。</li>
 *   <li>如果调用了带有6个参数的高级{@code encodeSegments()}函数或{@code makeSegmentsOptimally()}函数，
 *       并且maxVersion参数小于{@link QrCode#MAX_VERSION}，则增加该参数值。（此建议不适用于其他工厂函数，
 *       因为它们会搜索所有版本到{@code QrCode.MAX_VERSION}。）</li>
 *   <li>为了减少所需位数，将文本数据拆分为更好或最优的段。（参见 {@link QrSegmentAdvanced#makeSegmentsOptimally(CharSequence, QrCode.Ecc, int, int)
 *       QrSegmentAdvanced.makeSegmentsOptimally()}。）</li>
 *   <li>缩短文本或二进制数据。</li>
 *   <li>更改文本以适应特定段模式的字符集（例如字母数字）。</li>
 *   <li>将错误向上传播给调用者/用户。</li>
 * </ul>
 *
 * @see QrCode#encodeText(CharSequence, QrCode.Ecc)
 * @see QrCode#encodeBinary(byte[], QrCode.Ecc)
 * @see QrCode#encodeSegments(java.util.List, QrCode.Ecc)
 * @see QrCode#encodeSegments(java.util.List, QrCode.Ecc, int, int, int, boolean)
 * @see QrSegmentAdvanced#makeSegmentsOptimally(CharSequence, QrCode.Ecc, int, int)
 */
public class DataTooLongException extends IllegalArgumentException {
    public DataTooLongException() {
    }

    public DataTooLongException(String msg) {
        super(msg);
    }

}
