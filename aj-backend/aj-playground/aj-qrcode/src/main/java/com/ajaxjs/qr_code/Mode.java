package com.ajaxjs.qr_code;

/**
 * Describes how a segment's data bits are interpreted.
 */
public enum Mode {
    NUMERIC(0x1, 10, 12, 14), ALPHANUMERIC(0x2, 9, 11, 13), BYTE(0x4, 8, 16, 16), KANJI(0x8, 8, 10, 12), ECI(0x7, 0, 0, 0);

    // The mode indicator bits, which is the uint4 value (range 0 to 15).
    public final int modeBits;

    // Number of character count bits for three different version ranges.
    private final int[] numBitsCharCount;

    Mode(int mode, int... ccbits) {
        modeBits = mode;
        numBitsCharCount = ccbits;
    }

    /**
     * 返回给定版本号下，QR码中某一模式下的字符计数字段的位宽度。结果范围在[0, 16]之间。
     * Returns the bit width of the character count field for a segment in this mode in a QR Code at the given version number. The result is in the range [0, 16].
     *
     * @param ver QR码的版本号，必须在最小版本号和最大版本号之间。
     * @return 字符计数字段的位宽度。
     */
    public int numCharCountBits(int ver) {
        assert QrCode.MIN_VERSION <= ver && ver <= QrCode.MAX_VERSION; // 断言：确保版本号在QR码支持的最小版本号和最大版本号之间
        return numBitsCharCount[(ver + 7) / 17]; // 根据版本号计算并返回字符计数字段的位宽度
    }
}