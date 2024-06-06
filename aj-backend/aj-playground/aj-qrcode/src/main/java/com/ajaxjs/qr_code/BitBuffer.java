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

import java.util.BitSet;
import java.util.Objects;

/**
 * An appendable sequence of bits (0s and 1s). Mainly used by {@link QrSegment}.
 */
public final class BitBuffer implements Cloneable {
    private BitSet data;

    private int bitLength;  // Non-negative

    /**
     * Constructs an empty bit buffer (length 0).
     */
    public BitBuffer() {
        data = new BitSet();
        bitLength = 0;
    }

    /**
     * Returns the length of this sequence, which is a non-negative value.
     *
     * @return the length of this sequence
     */
    public int bitLength() {
        assert bitLength >= 0;
        return bitLength;
    }

    /**
     * 获取指定索引位置的位，返回0或1。
     *
     * @param index 想要获取位的索引
     * @return 指定索引位置的位值，0或1
     * @throws IndexOutOfBoundsException 如果索引小于0或大于等于 bitLength，抛出此异常
     */
    public int getBit(int index) {
        if (index < 0 || index >= bitLength) throw new IndexOutOfBoundsException();  // 检查索引是否有效，否则抛出异常
        return data.get(index) ? 1 : 0;  // 返回指定索引位置的位值，如果是1则返回1，否则返回0
    }

    /**
     * 将指定值的指定数量的低位比特追加到此缓冲区中。要求 0 ≤ len ≤ 31 且 0 ≤ val < 2<sup>len</sup>。
     *
     * @param val 要追加的值
     * @param len 要从此值中获取的低位比特数
     * @throws IllegalArgumentException 如果值或比特数超出范围
     * @throws IllegalStateException    如果追加数据会使 bitLength 超过 Integer.MAX_VALUE
     */
    public void appendBits(int val, int len) {
        // 检查 val 和 len 是否在有效范围内
        if (len < 0 || len > 31 || val >>> len != 0) throw new IllegalArgumentException("Value out of range");
        // 检查追加位后是否超过整型最大值
        if (Integer.MAX_VALUE - bitLength < len) throw new IllegalStateException("Maximum length reached");

        // 逐比特追加到数据缓冲区
        for (int i = len - 1; i >= 0; i--, bitLength++)
            data.set(bitLength, QrCode.getBit(val, i));
    }

    /**
     * 将指定位缓冲区的内容追加到此缓冲区中。
     *
     * @param bb 要追加数据的位缓冲区（不为 {@code null}）
     * @throws NullPointerException  如果位缓冲区为 {@code null}
     * @throws IllegalStateException 如果追加数据后，bitLength 会超过 Integer.MAX_VALUE
     */
    public void appendData(BitBuffer bb) {
        Objects.requireNonNull(bb); // 确保传入的位缓冲区不为 null

        if (Integer.MAX_VALUE - bitLength < bb.bitLength) // 检查追加数据后长度是否超过整型最大值
            throw new IllegalStateException("Maximum length reached");

        // 位位追加数据
        for (int i = 0; i < bb.bitLength; i++, bitLength++)
            data.set(bitLength, bb.data.get(i));
    }

    /**
     * 克隆当前的 BitBuffer 对象。
     * <p>此方法返回一个新的 BitBuffer 对象，该对象是当前对象的深拷贝。这意味着包括内部数据结构在内的所有属性都将被复制到新的对象中。</p>
     *
     * @return 一个新的BitBuffer对象，它是当前对象的深拷贝（不会返回{@code null}）
     * @throws AssertionError 如果克隆不支持，则抛出此异常
     */
    @Override
    public BitBuffer clone() {
        try {
            BitBuffer result = (BitBuffer) super.clone(); // 通过调用 super.clone( )创建一个当前对象的浅拷贝
            result.data = (BitSet) result.data.clone();  // 深拷贝 data 属性，以确保新的 BitBuffer 对象与原对象的数据独立

            return result;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e); // 如果当前对象不能被克隆，则抛出 AssertionError 异常，这在理论上不应该发生
        }
    }

}
