package com.ajaxjs.utils;

import org.springframework.util.StringUtils;

/**
 * int , float, double, long, String 与 byte 数组互转
 * int：4字节  float：4字节 , double：4字节   long：8字
 * <a href="https://blog.csdn.net/qq_40083897/article/details/117363607">...</a>
 * <a href="https://blog.csdn.net/qq_40083897/article/details/125367578">...</a>
 */
public class BytesUtils {
    /**
     * bytesToFloat
     *
     * @param content byte数组
     * @param index   从多少下标开始取
     * @return
     */
    public static float bytesToFloat(byte[] content, int index) {
        int l = content[index];
        l &= 0xff;
        l |= ((long) content[index + 1] << 8);
        l &= 0xffff;
        l |= ((long) content[index + 2] << 16);
        l &= 0xffffff;
        l |= ((long) content[index + 3] << 24);

        return Float.intBitsToFloat(l);
    }

    public static byte[] floatToBytes(float f) {
        int fbit = Float.floatToIntBits(f);// 把 float 转换为 byte[]

        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++)
            b[i] = (byte) (fbit >> (24 - i * 8));

        int len = b.length;// 翻转数组
        byte[] dest = new byte[len];  // 建立一个与源数组元素类型相同的数组
        System.arraycopy(b, 0, dest, 0, len); // 为了防止修改源数组，将源数组拷贝一份副本
        byte temp;

        // 将顺位第i个与倒数第i个交换
        for (int i = 0; i < len / 2; ++i) {
            temp = dest[i];
            dest[i] = dest[len - i - 1];
            dest[len - i - 1] = temp;
        }

        return dest;
    }

    /**
     * bytesToInt
     *
     * @param content byte数组
     * @param index   从多少下标开始取
     * @return
     */
    public static int bytesToInt(byte[] content, int index) {
        return (0xff000000 & (content[index + 3] << 24)) |
                (0x00ff0000 & (content[index + 2] << 16)) |
                (0x0000ff00 & (content[index + 1] << 8)) |
                (0x000000ff & content[index]);
    }


    public static byte[] intToBytes(int value) {
        byte[] b = new byte[4];
        b[3] = (byte) ((value & 0xff000000) >> 24);
        b[2] = (byte) ((value & 0x00ff0000) >> 16);
        b[1] = (byte) ((value & 0x0000ff00) >> 8);
        b[0] = (byte) (value & 0x000000ff);

        return b;
    }

    // float转换为byte[4]数组
    public static byte[] getByteArray(float f) {
        int intuits = Float.floatToIntBits(f);//将 float 里面的二进制串解释为 int 整数
        return getByteArray(intuits);
    }

    /**
     * bytesToDouble
     *
     * @param content byte数组
     * @param index   从多少下标开始取
     * @return
     */
    public static double bytesToDouble(byte[] content, int index) {
        long value = 0;
        for (int i = 0; i < 8; i++)
            value |= ((long) (content[index + i] & 0xff)) << (8 * i);

        return Double.longBitsToDouble(value);
    }

    public static byte[] doubleToBytes(double d) {
        long value = Double.doubleToRawLongBits(d);
        byte[] byteRet = new byte[8];

        for (int i = 0; i < 8; i++)
            byteRet[i] = (byte) ((value >> 8 * i) & 0xff);

        return byteRet;
    }

    /**
     * bytesToLong
     *
     * @param content byte数组
     * @param index   从多少下标开始取
     * @return
     */
    public static long bytesToLong(byte[] content, int index) {
        long num = 0;

        for (int ix = 0; ix < 8; ++ix) {
            num <<= 8;
            num |= (content[ix + index] & 0xff);
        }

        return num;
    }

    public static byte[] longToBytes(long num) {
        byte[] byteNum = new byte[8];

        for (int ix = 0; ix < 8; ++ix) {
            int offset = 64 - (ix + 1) * 8;
            byteNum[ix] = (byte) ((num >> offset) & 0xff);
        }

        return byteNum;
    }

    /**
     * bytesToString
     *
     * @param content byte数组
     * @param offset  从多少下标开始取
     * @param length  长度
     * @return
     */
    public static String bytesToString(byte[] content, int offset, int length) {
        return new String(content, offset, length);
    }

    public static byte[] stringToByte(String hexString) {
        if (StringUtils.hasText(hexString)) {
            hexString = hexString.replaceAll(" ", "");
            int len = hexString.length();
            int index = 0;
            byte[] bytes = new byte[len / 2];

            while (index < len) {
                String sub = hexString.substring(index, index + 2);
                bytes[index / 2] = (byte) Integer.parseInt(sub, 16);
                index += 2;
            }

            return bytes;
        }

        return null;
    }

    public static String bytesToString1(byte[] content, int offset, int length) {
        int effLength = checkEffectiveLength(content, offset, length);
        //System.out.println("effLength==="+effLength);
        return new String(content, offset, effLength);
    }

    public static int checkEffectiveLength(byte[] content, int offset, int length) {
        for (int i = 0; i < length; i++) {
            int v = content[offset + i] & 0xFF;
            //System.out.println(v);
            if (v == 0)
                return i;// 代表结束标记
        }

        return 0;
    }

    /**
     * 数组转换成十六进制字符串
     *
     * @param content
     * @return HexString
     */
    public static String bytesToHexString(byte[] content) {
        StringBuilder sb = new StringBuilder(content.length);
        String sTemp;

        for (byte b : content) {
            sTemp = Integer.toHexString(0xFF & b);
            if (sTemp.length() < 2)
                sb.append(0);

            sb.append(sTemp.toUpperCase());
        }

        return sb.toString();
    }
}

