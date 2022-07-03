package com.lutw.common.core.utils;

import java.math.BigInteger;
import java.nio.ByteBuffer;

/**
 * 通信格式转换
 * <p>
 * Java和一些windows编程语言如c、c++、delphi所写的网络程序进行通讯时，需要进行相应的转换
 * 高、低字节之间的转换
 * windows的字节序为低字节开头
 * linux,unix的字节序为高字节开头
 * java则无论平台变化，都是高字节开头
 * @Author ltw
 * @Date 2020-09-07 09:17:00
 * @Version V1.0
 */
public class FormatTransfer {
    /**
     * 将int转为低字节在前，高字节在后的byte数组
     *
     * @param n int
     * @return byte[]
     */
    public static byte[] toLH(int n) {
        byte[] b = new byte[4];
        b[0] = (byte) (n & 0xff);
        b[1] = (byte) (n >> 8 & 0xff);
        b[2] = (byte) (n >> 16 & 0xff);
        b[3] = (byte) (n >> 24 & 0xff);
        return b;
    }

    /**
     * 将int转为高字节在前，低字节在后的byte数组
     *
     * @param n int
     * @return byte[]
     */
    public static byte[] toHH(int n) {
        byte[] b = new byte[4];
        b[3] = (byte) (n & 0xff);
        b[2] = (byte) (n >> 8 & 0xff);
        b[1] = (byte) (n >> 16 & 0xff);
        b[0] = (byte) (n >> 24 & 0xff);
        return b;
    }

    /**
     * 将short转为低字节在前，高字节在后的byte数组
     *
     * @param n short
     * @return byte[]
     */
    public static byte[] toLH(short n) {
        byte[] b = new byte[2];
        b[0] = (byte) (n & 0xff);
        b[1] = (byte) (n >> 8 & 0xff);
        return b;
    }

    /**
     * 将short转为高字节在前，低字节在后的byte数组
     *
     * @param n short
     * @return byte[]
     */
    public static byte[] toHH(short n) {
        byte[] b = new byte[2];
        b[1] = (byte) (n & 0xff);
        b[0] = (byte) (n >> 8 & 0xff);
        return b;
    }
    /**
     * 将float转为低字节在前，高字节在后的byte数组
     */
    public static byte[] toLH(float f) {
        return toLH(Float.floatToRawIntBits(f));
    }

    /**
     * 将float转为高字节在前，低字节在后的byte数组
     */
    public static byte[] toHH(float f) {
        return toHH(Float.floatToRawIntBits(f));
    }

    /**
     * Double转字节数组
     * 网络字节序 小端 低字节在前
     * @param d
     * @return
     */
    public static byte[] double2Bytes(double d) {
        long value = Double.doubleToRawLongBits(d);
        byte[] byteRet = new byte[8];
        for (int i = 0; i < 8; i++) {
            byteRet[i] = (byte) ((value >> 8 * i) & 0xff);
        }
        return byteRet;
    }

    /**
     * 字节数组转double
     * 适用于网络字节序 小端 低字节在前
     * @param bytes
     * @return
     */
    public static double bytes2Double(byte[] bytes) {
        long value = 0;
        for (int i = 0; i < 8; i++) {
            value |= ((long) (bytes[i] & 0xff)) << (8 * i);
        }
        return Double.longBitsToDouble(value);
    }

    /**
     * double转字节数组  长度  8
     * 适用于网络字节序 大端 高字节在前
     * @param d
     * @return
     */
    public static byte[] bytesToDouble(double d){
        ByteBuffer bb = ByteBuffer.wrap(new byte[8]);
        bb.asDoubleBuffer().put(d);
        return bb.array();
    }

    /**
     * 将String转为byte数组
     */
    public static byte[] stringToBytes(String s, int length) {
        while (s.getBytes().length < length) {
            s += " ";
        }
        return s.getBytes();
    }

    /**
     * 将byte转换为一个长度为8的byte数组，数组每个值代表bit
     * 如：输入0x35//0011 0101，输出[0,0,1,1,0,1,0,1]
     */
    public static byte[] getBooleanArray(byte b) {
        byte[] array = new byte[8];
        for (int i = 7; i >= 0; i--) {
            array[i] = (byte) (b & 1);
            b = (byte) (b >> 1);
        }
        return array;
    }

    /**
     * 把byte转为字符串的bit
     * 如：输入0x35//00110101，输出00110101
     */
    public static String byteToBit(byte b) {
        return ""
                + (byte) ((b >> 7) & 0x1) + (byte) ((b >> 6) & 0x1)
                + (byte) ((b >> 5) & 0x1) + (byte) ((b >> 4) & 0x1)
                + (byte) ((b >> 3) & 0x1) + (byte) ((b >> 2) & 0x1)
                + (byte) ((b >> 1) & 0x1) + (byte) ((b >> 0) & 0x1);
    }

    /**
     * 二进制字符串转byte
     */
    public static byte decodeBinaryString(String byteStr) {
        int re, len;
        if (null == byteStr) {
            return 0;
        }
        len = byteStr.length();
        if (len != 4 && len != 8) {
            return 0;
        }
        if (len == 8) {// 8 bit处理
            if (byteStr.charAt(0) == '0') {// 正数
                re = Integer.parseInt(byteStr, 2);
            } else {// 负数
                re = Integer.parseInt(byteStr, 2) - 256;
            }
        } else {// 4 bit处理
            re = Integer.parseInt(byteStr, 2);
        }
        return (byte) re;
    }

    /**
     * 将字符串转换为byte数组
     *
     * @param s String
     * @return byte[]
     */
    public static byte[] stringToBytes(String s) {
        return s.getBytes();
    }

    /**
     * 将低字节数组转换为int
     *
     * @param b byte[]
     * @return int
     */
    public static int lBytesToInt(byte[] b) {
        int s = 0;
        for (int i = 0; i < 3; i++) {
            if (b[3 - i] >= 0) {
                s = s + b[3 - i];
            } else {
                s = s + 256 + b[3 - i];
            }
            s = s * 256;
        }
        if (b[0] >= 0) {
            s = s + b[0];
        } else {
            s = s + 256 + b[0];
        }
        return s;
    }

    /**
     * 高字节数组到short的转换
     *
     * @param b byte[]
     * @return short
     */
    public static short hBytesToShort(byte[] b) {
        int s = 0;
        if (b[0] >= 0) {
            s = s + b[0];
        } else {
            s = s + 256 + b[0];
        }
        s = s * 256;
        if (b[1] >= 0) {
            s = s + b[1];
        } else {
            s = s + 256 + b[1];
        }
        short result = (short) s;
        return result;
    }

    /**
     * 低字节数组到short的转换
     *
     * @param b byte[]
     * @return short
     */
    public static short lBytesToShort(byte[] b) {
        int s = 0;
        if (b[1] >= 0) {
            s = s + b[1];
        } else {
            s = s + 256 + b[1];
        }
        s = s * 256;
        if (b[0] >= 0) {
            s = s + b[0];
        } else {
            s = s + 256 + b[0];
        }
        short result = (short) s;
        return result;
    }

    /**
     * 高字节数组转换为float
     *
     * @param b byte[]
     * @return float
     */
    public static float hBytesToFloat(byte[] b) {
        int i = 0;
        Float F = new Float(0.0);
        i = ((((b[0] & 0xff) << 8 | (b[1] & 0xff)) << 8) | (b[2] & 0xff)) << 8 | (b[3] & 0xff);
        return F.intBitsToFloat(i);
    }

    /**
     * 低字节数组转换为float
     *
     * @param b byte[]
     * @return float
     */
    public static float lBytesToFloat(byte[] b) {
        int i = 0;
        Float F = new Float(0.0);
        i = ((((b[3] & 0xff) << 8 | (b[2] & 0xff)) << 8) | (b[1] & 0xff)) << 8 | (b[0] & 0xff);
        return F.intBitsToFloat(i);
    }

    /**
     * 打印byte数组
     */
    public static void printBytes(byte[] bb) {
        int length = bb.length;
        for (int i = 0; i < length; i++) {
            System.out.print(bb + " ");
        }
    }

    /**
     * 将short类型的值转换为字节序颠倒过来对应的short值
     *
     * @param s short
     * @return short
     */
    public static short reverseShort(short s) {
        short result = FormatTransfer.hBytesToShort(FormatTransfer.toLH(s));
        return result;
    }

    /**
     * 将float类型的值转换为字节序颠倒过来对应的float值
     *
     * @param f float
     * @return float
     */
    public static float reverseFloat(float f) {
        float result = FormatTransfer.hBytesToFloat(FormatTransfer.toLH(f));
        return result;
    }

    /**
     * Convert byte[] to hex string.这里我们可以将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。
     *
     * @param src byte[] data
     * @return hex string
     */
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * Convert hex string to byte[]
     *
     * @param hexString the hex string
     * @return byte[]
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    /**
     * Convert char to byte
     *
     * @param c char
     * @return byte
     */
    public static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }


    public static String bytes2hex02(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        String tmp = null;
        for (byte b : bytes) {
            // 将每个字节与0xFF进行与运算，然后转化为10进制，然后借助于Integer再转化为16进制
            tmp = Integer.toHexString(0xFF & b);
            if (tmp.length() == 1)// 每个字节8为，转为16进制标志，2个16进制位
            {
                tmp = "0" + tmp;
            }
            sb.append(tmp);
        }

        return sb.toString();

    }

    public static String bytes2hex03(byte[] bytes) {
        final String HEX = "0123456789abcdef";
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            // 取出这个字节的高4位，然后与0x0f与运算，得到一个0-15之间的数据，通过HEX.charAt(0-15)即为16进制数
            sb.append(HEX.charAt((b >> 4) & 0x0f));
            // 取出这个字节的低位，与0x0f与运算，得到一个0-15之间的数据，通过HEX.charAt(0-15)即为16进制数
            sb.append(HEX.charAt(b & 0x0f));
        }

        return sb.toString();
    }

    public static int byteToInt2(byte[] bRefArr) {
        int iOutcome = 0;
        byte bLoop;

        for (int i = 0; i < 4; i++) {
            bLoop = bRefArr[i];
            iOutcome += (bLoop & 0xFF) << (8 * i);

        }

        return iOutcome;
    }

    public static float getFloat(byte[] b) {
        int accum = 0;
        accum = accum | (b[0] & 0xff) << 0;
        accum = accum | (b[1] & 0xff) << 8;
        accum = accum | (b[2] & 0xff) << 16;
        accum = accum | (b[3] & 0xff) << 24;
        System.out.println(accum);
        return Float.intBitsToFloat(accum);
    }

    /**
     * 通过byte数组取到short
     *
     * @param b
     * @return
     */
    public static short getShort(byte[] b) {
        return (short) (((b[1] << 8) | b[0] & 0xff));
    }

    /**
     * 将一个单字节的byte转换成32位的int
     *
     * @param b byte
     * @return convert result
     */
    public static int unsignedByteToInt(byte b) {
        return (int) b & 0xFF;
    }

    /**
     * 将长度为2的byte数组转换为16位int
     *
     * @param res byte[]
     * @return int
     */
    public static int byte2int(byte[] res) {
        // res = InversionByte(res);
        // 一个byte数据左移24位变成0x??000000，再右移8位变成0x00??0000
        int targets = (res[0] & 0xff) | ((res[1] << 8) & 0xff00); // | 表示安位或
        return targets;
    }

    public static byte[] short2ByteNew(short x) {
        byte high = (byte) (0x00FF & (x >> 8));//定义第一个byte
        byte low = (byte) (0x00FF & x);//定义第二个byte
        byte[] bytes = new byte[2];
        bytes[0] = high;
        bytes[1] = low;
        return bytes;
    }


    public static String shortToHexString(short x) {
        byte[] src = short2ByteNew(x);
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public static byte[] charToByteNew(char c) {
        byte[] b = new byte[2];
        b[0] = (byte) ((c & 0xFF00) >> 8);
        b[1] = (byte) (c & 0xFF);
        return b;
    }

    public static String charToHexString(char c) {
        byte[] src = charToByteNew(c);
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public static char byteToChar(byte[] b) {
        char c = (char) (((b[0] & 0xFF) << 8) | (b[1] & 0xFF));
        return c;
    }

    private static String hexStr = "0123456789ABCDEF";

    /**
     * @param bytes
     * @return 将二进制转换为十六进制字符输出
     */
    public static String binaryToHexString(byte[] bytes) {

        String result = "";
        String hex = "";
        for (int i = 0; i < bytes.length; i++) {
            //字节高4位
            hex = String.valueOf(hexStr.charAt((bytes[i] & 0xF0) >> 4));
            //字节低4位
            hex += String.valueOf(hexStr.charAt(bytes[i] & 0x0F));
            result += hex + " ";
        }
        return result;
    }

    //16进制转10进制
    public static int decodeHEX(String hexs) {
        BigInteger bigint = new BigInteger(hexs, 16);
        int numb = bigint.intValue();
        return numb;
    }

    //16进制转10进制
    public static long decodeHEXToLong(String hexs) {
        BigInteger bigint = new BigInteger(hexs, 16);
        long numb = bigint.longValue();
        return numb;
    }

    /**
     * int到byte[] 由高位到低位
     *
     * @param i 需要转换为byte数组的整行值。
     * @return byte数组
     */
    public static byte[] intToByteArray(int i) {
        byte[] result = new byte[4];
        result[0] = (byte) ((i >> 24) & 0xFF);
        result[1] = (byte) ((i >> 16) & 0xFF);
        result[2] = (byte) ((i >> 8) & 0xFF);
        result[3] = (byte) (i & 0xFF);
        return result;
    }

    /**
     * byte[]转int
     *
     * @param bytes 需要转换成int的数组
     * @return int值
     */
    public static int byteArrayToInt(byte[] bytes) {
        int value = 0;
        for (int i = 0; i < 4; i++) {
            int shift = (3 - i) * 8;
            value += (bytes[i] & 0xFF) << shift;
        }
        return value;
    }

    /**
     * 将int转化16进制
     *
     * @param n
     * @return 16进制数
     */
    public static String intToHex(int n) {
        StringBuffer s = new StringBuffer();
        String a;
        char[] b = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        while (n != 0) {
            s = s.append(b[n % 16]);
            n = n / 16;
        }
        a = s.reverse().toString();
        return a;
    }

    /**
     * 二制度字符串转字节数组，如 101000000100100101110000 -> A0 09 70
     * 由左到右
     * @param bitString 输入字符串。
     * @return 转换好的字节数组。
     */
    public static byte[] bitString2bytes(String bitString) {
        StringBuilder in = new StringBuilder(bitString);
        // 注：这里in.length() 不可在for循环内调用，因为长度在变化
        int remainder = in.length() % 8;
        if (remainder > 0)
            for (int i = 0; i < 8 - remainder; i++)
                in.append("0");
        byte[] bts = new byte[in.length() / 8];
        for (int i = 0; i < bts.length; i++){
            bts[i] = (byte) Integer.parseInt(in.substring(i * 8, i * 8 + 8), 2);
        }
        return bts;
    }

    /**
     * 将 byte数组中的元素倒序排列
     */
    public static byte [] bytesReverseOrder( byte [] b) {
        int  length = b.length;
        byte [] result =  new   byte [length];
        for ( int  i= 0 ; i<length; i++) {
            result[length-i-1 ] = b[i];
        }
        return  result;
    }

    public static byte[] doubleToByte(double num) {
        byte[] b = new byte[8];
        long l = Double.doubleToLongBits(num);
        for (int i = 0; i < 8; i++) {
            b[i] = new Long(l).byteValue();
            l = l >> 8;
        }
        return b;
    }

    /**
     * int转换为小端byte[]（高位放在高地址中）
     * @param iValue
     * @return
     */
    public static byte[] Int2Bytes_LE(int iValue){
        byte[] rst = new byte[4];
        // 先写int的最后一个字节
        rst[0] = (byte)(iValue & 0xFF);
        // int 倒数第二个字节
        rst[1] = (byte)((iValue & 0xFF00) >> 8 );
        // int 倒数第三个字节
        rst[2] = (byte)((iValue & 0xFF0000) >> 16 );
        // int 第一个字节
        rst[3] = (byte)((iValue & 0xFF000000) >> 24 );
        return rst;
    }

    /**
     * 十六进制转二进制  高字节在前
     * @param hexString 十六进制字符串
     * @return 二进制字符串
     * @DATA 2020-12-01
     * @Author ltw
     */
    public static String hexString2binaryString(String hexString) {
        if (hexString == null || hexString.length() % 2 != 0)
            return null;
        String bString = "", tmp;
        for (int i = 0; i < hexString.length(); i++) {
            tmp = "0000" + Integer.toBinaryString(Integer.parseInt(hexString.substring(i, i + 1), 16));
            bString += tmp.substring(tmp.length() - 4);
        }
        return bString;
    }

    /**
     * 二进制转16进制字符串
     * @param bString 二进制字符串
     * @return  十六进制字符串
     * @DATA 2020-12-01
     * @Author ltw
     */
    public static String binaryString2hexString(String bString) {
        if (bString == null || bString.equals("") || bString.length() % 8 != 0)
            return null;
        StringBuffer tmp=new StringBuffer();
        int iTmp = 0;
        for (int i = 0; i < bString.length(); i += 4) {
            iTmp = 0;
            for (int j = 0; j < 4; j++) {
                iTmp += Integer.parseInt(bString.substring(i + j, i + j + 1)) << (4 - j - 1);
            }
            tmp.append(Integer.toHexString(iTmp));
        }
        return tmp.toString();
    }

    /**
     * 二进制字符串转十进制
     * @param binary  二进制字符串
     * @return 十进制数值
     * @DATA 2020-12-01
     * @Author ltw
     */
    public static int binaryToInt(String binary) {
        int max = binary.length();
        int result = 0;
        for (int i = max; i > 0; i--) {
            char c = binary.charAt(i - 1);
            int algorism = c - '0';
            result += Math.pow(2, max - i) * algorism;
        }
        return result;
    }
    public static byte[] int2byte(int res) {
        byte[] targets = new byte[4];

        targets[0] = (byte) (res & 0xff);// 最低位
        targets[1] = (byte) ((res >> 8) & 0xff);// 次低位
        targets[2] = (byte) ((res >> 16) & 0xff);// 次高位
        targets[3] = (byte) (res >>> 24);// 最高位,无符号右移。
        return targets;
    }

    /**
     * hexStr 转 Double
     * @param hexStr
     * @return
     */
    public static Double hexStrToDouble(String hexStr) {

        long longBits = Long.valueOf(hexStr, 16).longValue();
        return Double.longBitsToDouble(longBits);
    }

}