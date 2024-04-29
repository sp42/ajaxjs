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

package com.ajaxjs.qr_code_fast;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.ajaxjs.qr_code.DataTooLongException;
import com.ajaxjs.qr_code.Mode;
import com.ajaxjs.qr_code.QrSegmentAdvanced;
import com.ajaxjs.qr_code.fast.BitBuffer;


/**
 * Splits text into optimal segments and encodes kanji segments.
 * Provides static functions only; not instantiable.
 *
 * @see QrSegment
 * @see QrCode
 */
public final class QrSegmentAdvancedFast {
    /**
     * Returns a list of zero or more segments to represent the specified Unicode text string.
     * The resulting list optimally minimizes the total encoded bit length, subjected to the constraints
     * in the specified {error correction level, minimum version number, maximum version number}.
     * <p>This function can utilize all four text encoding modes: numeric, alphanumeric, byte (UTF-8),
     * and kanji. This can be considered as a sophisticated but slower replacement for {@link
     * QrSegment#makeSegments(String)}. This requires more input parameters because it searches a
     * range of versions, like {@link QrCode#encodeSegments(List, QrCode.Ecc, int, int, int, boolean)}.</p>
     *
     * @param text       the text to be encoded (not {@code null}), which can be any Unicode string
     * @param ecl        the error correction level to use (not {@code null})
     * @param minVersion the minimum allowed version of the QR Code (at least 1)
     * @param maxVersion the maximum allowed version of the QR Code (at most 40)
     * @return a new mutable list (not {@code null}) of segments (not {@code null})
     * containing the text, minimizing the bit length with respect to the constraints
     * @throws NullPointerException     if the text or error correction level is {@code null}
     * @throws IllegalArgumentException if 1 &#x2264; minVersion &#x2264; maxVersion &#x2264; 40 is violated
     * @throws DataTooLongException     if the text fails to fit in the maxVersion QR Code at the ECL
     */
    public static List<QrSegment> makeSegmentsOptimally(String text, QrCode.Ecc ecl, int minVersion, int maxVersion) {
        // Check arguments
        Objects.requireNonNull(text);
        Objects.requireNonNull(ecl);
        if (!(QrCode.MIN_VERSION <= minVersion && minVersion <= maxVersion && maxVersion <= QrCode.MAX_VERSION))
            throw new IllegalArgumentException("Invalid value");

        // Iterate through version numbers, and make tentative segments
        List<QrSegment> segs = null;
        int[] codePoints = toCodePoints(text);
        for (int version = minVersion; ; version++) {
            if (version == minVersion || version == 10 || version == 27) segs = makeSegmentsOptimally(codePoints, version);
            assert segs != null;

            // Check if the segments fit
            int dataCapacityBits = QrCode.getNumDataCodewords(version, ecl) * 8;  // Number of data bits available
            int dataUsedBits = QrSegment.getTotalBits(segs, version);
            if (dataUsedBits != -1 && dataUsedBits <= dataCapacityBits) return segs;  // This version number is found to be suitable

            if (version >= maxVersion) {  // All versions in the range could not fit the given text
                String msg = "Segment too long";
                if (dataUsedBits != -1) msg = String.format("Data length = %d bits, Max capacity = %d bits", dataUsedBits, dataCapacityBits);
                throw new DataTooLongException(msg);
            }
        }
    }


    // Returns a new list of segments that is optimal for the given text at the given version number.
    private static List<QrSegment> makeSegmentsOptimally(int[] codePoints, int version) {
        if (codePoints.length == 0) return new ArrayList<>();
        Mode[] charModes = computeCharacterModes(codePoints, version);

        return splitIntoSegments(codePoints, charModes);
    }


    // Returns a new array representing the optimal mode per code point based on the given text and version.
    private static Mode[] computeCharacterModes(int[] codePoints, int version) {
        if (codePoints.length == 0) throw new IllegalArgumentException();
        final Mode[] modeTypes = {Mode.BYTE, Mode.ALPHANUMERIC, Mode.NUMERIC, Mode.KANJI};  // Do not modify
        final int numModes = modeTypes.length;

        // Segment header sizes, measured in 1/6 bits
        final int[] headCosts = new int[numModes];
        for (int i = 0; i < numModes; i++)
            headCosts[i] = (4 + modeTypes[i].numCharCountBits(version)) * 6;

        // charModes[i][j] represents the mode to encode the code point at
        // index i such that the final segment ends in modeTypes[j] and the
        // total number of bits is minimized over all possible choices
        Mode[][] charModes = new Mode[codePoints.length][numModes];

        // At the beginning of each iteration of the loop below,
        // prevCosts[j] is the exact minimum number of 1/6 bits needed to
        // encode the entire string prefix of length i, and end in modeTypes[j]
        int[] prevCosts = headCosts.clone();

        // Calculate costs using dynamic programming
        for (int i = 0; i < codePoints.length; i++) {
            int c = codePoints[i];
            int[] curCosts = new int[numModes];
            {  // Always extend a byte mode segment
                curCosts[0] = prevCosts[0] + QrSegmentAdvanced.countUtf8Bytes(c) * 8 * 6;
                charModes[i][0] = modeTypes[0];
            }
            // Extend a segment if possible
            if (QrSegment.ALPHANUMERIC_MAP[c] != -1) {  // Is alphanumeric
                curCosts[1] = prevCosts[1] + 33;  // 5.5 bits per alphanumeric char
                charModes[i][1] = modeTypes[1];
            }
            if ('0' <= c && c <= '9') {  // Is numeric
                curCosts[2] = prevCosts[2] + 20;  // 3.33 bits per digit
                charModes[i][2] = modeTypes[2];
            }
            if (QrSegmentAdvanced.isKanji(c)) {
                curCosts[3] = prevCosts[3] + 78;  // 13 bits per Shift JIS char
                charModes[i][3] = modeTypes[3];
            }

            // Start new segment at the end to switch modes
            for (int j = 0; j < numModes; j++) {  // To mode
                for (int k = 0; k < numModes; k++) {  // From mode
                    int newCost = (curCosts[k] + 5) / 6 * 6 + headCosts[j];
                    if (charModes[i][k] != null && (charModes[i][j] == null || newCost < curCosts[j])) {
                        curCosts[j] = newCost;
                        charModes[i][j] = modeTypes[k];
                    }
                }
            }

            prevCosts = curCosts;
        }

        // Find optimal ending mode
        Mode curMode = null;
        for (int i = 0, minCost = 0; i < numModes; i++) {
            if (curMode == null || prevCosts[i] < minCost) {
                minCost = prevCosts[i];
                curMode = modeTypes[i];
            }
        }

        // Get optimal mode for each code point by tracing backwards
        Mode[] result = new Mode[charModes.length];
        for (int i = result.length - 1; i >= 0; i--) {
            for (int j = 0; j < numModes; j++) {
                if (modeTypes[j] == curMode) {
                    curMode = charModes[i][j];
                    result[i] = curMode;
                    break;
                }
            }
        }
        return result;
    }

    // Returns a new list of segments based on the given text and modes, such that
    // consecutive code points in the same mode are put into the same segment.
    private static List<QrSegment> splitIntoSegments(int[] codePoints, Mode[] charModes) {
        if (codePoints.length == 0) throw new IllegalArgumentException();
        List<QrSegment> result = new ArrayList<>();

        // Accumulate run of modes
        Mode curMode = charModes[0];
        int start = 0;

        for (int i = 1; ; i++) {
            if (i < codePoints.length && charModes[i] == curMode) continue;
            String s = new String(codePoints, start, i - start);
            if (curMode == Mode.BYTE) result.add(QrSegment.makeBytes(s.getBytes(StandardCharsets.UTF_8)));
            else if (curMode == Mode.NUMERIC) result.add(QrSegment.makeNumeric(s));
            else if (curMode == Mode.ALPHANUMERIC) result.add(QrSegment.makeAlphanumeric(s));
            else if (curMode == Mode.KANJI) result.add(makeKanji(s));
            else throw new AssertionError();
            if (i >= codePoints.length) return result;
            curMode = charModes[i];
            start = i;
        }
    }


    // Returns a new array of Unicode code points (effectively
    // UTF-32 / UCS-4) representing the given UTF-16 string.
    private static int[] toCodePoints(String s) {
        int[] result = s.codePoints().toArray();
        for (int c : result) {
            if (Character.isSurrogate((char) c)) throw new IllegalArgumentException("Invalid UTF-16 string");
        }
        return result;
    }

    /*---- Kanji mode segment encoder ----*/

    /**
     * Returns a segment representing the specified text string encoded in kanji mode.
     * Broadly speaking, the set of encodable characters are {kanji used in Japan,
     * hiragana, katakana, East Asian punctuation, full-width ASCII, Greek, Cyrillic}.
     * Examples of non-encodable characters include {ordinary ASCII, half-width katakana,
     * more extensive Chinese hanzi}.
     *
     * @param text the text (not {@code null}), with only certain characters allowed
     * @return a segment (not {@code null}) containing the text
     * @throws NullPointerException     if the string is {@code null}
     * @throws IllegalArgumentException if the string contains non-encodable characters
     */
    public static QrSegment makeKanji(String text) {
        Objects.requireNonNull(text);
        BitBuffer bb = new BitBuffer();
        text.chars().forEachOrdered(c -> {
            int val = QrSegmentAdvanced.UNICODE_TO_QR_KANJI[c];
            if (val == -1) throw new IllegalArgumentException("String contains non-kanji-mode characters");
            bb.appendBits(val, 13);
        });

        return new QrSegment(Mode.KANJI, text.length(), bb.data, bb.bitLength);
    }


    private QrSegmentAdvancedFast() {
    }  // Not instantiable
}
