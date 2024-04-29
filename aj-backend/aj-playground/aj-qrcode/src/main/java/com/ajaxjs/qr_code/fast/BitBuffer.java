/*
 * Fast QR Code generator library
 *
 * Copyright (c) Project Nayuki. (MIT License)
 * https://www.nayuki.io/page/fast-qr-code-generator-library
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

package com.ajaxjs.qr_code.fast;

import java.util.Arrays;
import java.util.Objects;

// An appendable sequence of bits (0s and 1s), mainly used by QrSegment.
public final class BitBuffer {
    public int[] data;  // In each 32-bit word, bits are filled from top down.

    public int bitLength;  // Always non-negative.

    // Creates an empty bit buffer.
    public BitBuffer() {
        data = new int[64];
        bitLength = 0;
    }

    // Returns the bit at the given index, yielding 0 or 1.
    public int getBit(int index) {
        if (index < 0 || index >= bitLength)
            throw new IndexOutOfBoundsException();

        return (data[index >>> 5] >>> ~index) & 1;
    }


    // Returns a new array representing this buffer's bits packed into
    // bytes in big endian. The current bit length must be a multiple of 8.
    public byte[] getBytes() {
        if (bitLength % 8 != 0)
            throw new IllegalStateException("Data is not a whole number of bytes");
        byte[] result = new byte[bitLength / 8];
        for (int i = 0; i < result.length; i++)
            result[i] = (byte) (data[i >>> 2] >>> (~i << 3));

        return result;
    }

    /**
     * 将给定值的指定数量的低阶位追加到此缓冲区中。要求 0 <= len <= 31 并且 0 <= val < 2^len。
     * Appends the given number of low-order bits of the given value to this buffer. Requires 0 <= len <= 31 and 0 <= val < 2^len.
     *
     * @param val 要追加的值。
     * @param len 要追加的位数。
     * @throws IllegalArgumentException 如果值超出范围。
     * @throws IllegalStateException    如果达到最大长度。
     */
    public void appendBits(int val, int len) {
        // 检查长度合法性及值范围
        if (len < 0 || len > 31 || val >>> len != 0)
            throw new IllegalArgumentException("Value out of range");
        if (len > Integer.MAX_VALUE - bitLength)
            throw new IllegalStateException("Maximum length reached");

        // 如果当前位长度加上即将追加的位数超过现有数据数组的容量，则扩容
        if (bitLength + len + 1 > data.length << 5)
            data = Arrays.copyOf(data, data.length * 2);
        // 确保追加位操作不会超出数据数组的范围
        assert bitLength + len <= data.length << 5;

        // 计算当前可用的剩余位数
        int remain = 32 - (bitLength & 0x1F);
        // 确保剩余位数合法
        assert 1 <= remain && remain <= 32;

        // 如果剩余位数小于需要追加的位数，则先追加剩余位数，并调整值和位数
        if (remain < len) {
            data[bitLength >>> 5] |= val >>> (len - remain);
            bitLength += remain;
            // 确保位长度对齐
            assert (bitLength & 0x1F) == 0;
            len -= remain;
            val &= (1 << len) - 1; // 调整val，只保留要追加的低阶位
            remain = 32;
        }

        // 追加剩余的位数
        data[bitLength >>> 5] |= val << (remain - len);
        bitLength += len; // 更新位长度
    }

    /**
     * 将给定 word 数组表示的位序列追加到此缓冲区中。要求 0 <= len <= 32 * val.length。
     * Appends to this buffer the sequence of bits represented by the given word array and given a bit of length. Requires 0 <= len <= 32 * val.length.
     *
     * @param val 表示要追加的位序列的整数数组。
     * @param len 要追加的位数。如果为 0，则不执行任何操作。
     * @throws IllegalArgumentException 如果 len 小于 0 或大于 val 数组长度乘以 32，或者如果最后的 word 的低位不是 0，或者如果超过了最大长度限制，则抛出此异常。
     * @throws IllegalStateException    如果尝试追加的位长度超过了当前数据结构所能容纳的最大长度。
     */
    public void appendBits(int[] val, int len) {
        Objects.requireNonNull(val); // 确保 val 不为 null
        if (len == 0)
            return; // 如果要追加的位长度为 0，则直接返回

        // 检查 len 的合法性
        if (len < 0 || len > val.length * 32L)
            throw new IllegalArgumentException("Value out of range");

        // 计算需要追加的整数个数和剩余的位数
        int wholeWords = len / 32;
        int tailBits = len % 32;

        // 检查最后一个整数的低位是否清零
        if (tailBits > 0 && val[wholeWords] << tailBits != 0)
            throw new IllegalArgumentException("Last word must have low bits clear");
        // 检查是否超过了最大长度
        if (len > Integer.MAX_VALUE - bitLength)
            throw new IllegalStateException("Maximum length reached");

        // 确保数据数组有足够的空间容纳新的位
        while (bitLength + len > data.length * 32)
            data = Arrays.copyOf(data, data.length * 2);

        int shift = bitLength % 32; // 计算当前数据数组中下一个可用字节的位偏移

        // 如果位偏移为 0，即当前数据数组的起始位置是一个整数的起始位置，则直接复制
        if (shift == 0) {
            System.arraycopy(val, 0, data, bitLength / 32, (len + 31) / 32);
            bitLength += len;
        } else {
            // 如果位偏移不为 0，则需要逐个整数地追加
            for (int i = 0; i < wholeWords; i++) {
                int word = val[i];
                // 将 word 中的位追加到当前数据数组的末尾
                data[bitLength >>> 5] |= word >>> shift;
                bitLength += 32;
                // 如果追加了一个整数后，下一个整数的起始位置不是整数的起始位置，则需要处理跨整数的位追加
                data[bitLength >>> 5] = word << (32 - shift);
            }
            // 处理剩余的位
            if (tailBits > 0)
                appendBits(val[wholeWords] >>> (32 - tailBits), tailBits);
        }
    }
}
