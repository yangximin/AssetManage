package com.yang.assetmanage.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.Key;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 加密解密工具类
 *
 * @author yxm
 */
public class EncryptUtil {

    private static final int BUFFER_SIZE = 1024;

    /**
     * 将明文，按照MD5方式转成暗文
     *
     * @param password ：表示明文
     * @return : 表示对应的暗文
     */
    public static String encodeByMd5(String password) throws Exception {
        if (password == null || password.trim().length() == 0) {
            return null;
        }
        // 创建MessageDigest类
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        // 将明文转暗文
        byte[] byteArray = md5.digest(password.getBytes());
        // 暗文
        String passwordMD5 = byteArrayToHexString(byteArray);
        return passwordMD5.toLowerCase();
    }

    /**
     * 将byte[]中的每一个byte类型的值，转成16进制数
     */
    private static String byteArrayToHexString(byte[] byteArray) {
        StringBuffer sb = new StringBuffer();
        for (byte b : byteArray) {
            sb.append(byteToHexString(b));
        }
        return sb.toString();
    }

    /**
     * 将byte,转成16进制数
     */
    private static String byteToHexString(byte b) {
        // 将byte值覆给int型值
        int n = b;
        // 如果n是负数的话
        if (n < 0) {
            n = n + 256;
        }
        // n除以16的商，作为高位
        int height = n / 16;// 14
        int low = n % 16;// 1
        // 查表得到对应的16进制数
        return hex[height] + hex[low];
    }

    private static String[] hex = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};

    /**
     * GZIP 加密
     *
     * @param str
     * @return
     */
    public static byte[] encryptGZIP(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }

        try {
            // gzip压缩
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            GZIPOutputStream gzip = new GZIPOutputStream(baos);
            gzip.write(str.getBytes("UTF-8"));

            gzip.close();

            byte[] encode = baos.toByteArray();

            baos.flush();
            baos.close();

            // base64 加密
            return encode;
            // return new String(encode, "UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getUuid() {
        return UUID.randomUUID().toString();
    }

    /**
     * GZIP 解密
     *
     * @param str
     * @return
     */
    public static String decryptGZIP(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }

        try {

            byte[] decode = str.getBytes("UTF-8");

            // gzip 解压缩
            ByteArrayInputStream bais = new ByteArrayInputStream(decode);
            GZIPInputStream gzip = new GZIPInputStream(bais);

            byte[] buf = new byte[BUFFER_SIZE];
            int len = 0;

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            while ((len = gzip.read(buf, 0, BUFFER_SIZE)) != -1) {
                baos.write(buf, 0, len);
            }
            gzip.close();
            baos.flush();

            decode = baos.toByteArray();

            baos.close();

            return new String(decode, "UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 十六进制字符串 转换为 byte[]
     *
     * @param hexString the hex string
     * @return byte[]
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
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
    private static byte charToByte(char c) {
        return (byte) "0123456789abcdef".indexOf(c);
    }

    /**
     * byte[] 转换为 十六进制字符串
     *
     * @param src
     * @return
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
     * 将二进制转换成16进制
     *
     * @param buf
     * @return
     */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }


    public static String decryptByAES128(String sSrc, String sKey) throws Exception {
        try {
            // 判断Key是否正确
            if (sKey == null) {
                System.out.print("Key为空null");
                return null;
            }
            byte[] raw = sKey.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
            //IvParameterSpec iv = new IvParameterSpec(new byte[16]);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] encrypted1 = Base64.decode(sSrc, Base64.NO_WRAP);// 先用base64解密
            try {
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original, "utf-8");
                return originalString;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 为字符串添加时间戳
     *
     * @param token 加密token
     * @return
     */
    public static String enTokenTimes(Context context, String token) {
        if (TextUtils.isEmpty(token)) {
            return "";
        }
        String timesToken = token;
        String times = System.currentTimeMillis() + "";

        if (!TextUtils.isEmpty(times)) {
            timesToken = timesToken + ":" + times;
        }
        return timesToken;
    }

    /**
     * 获取含时间撮中的Token
     *
     * @param token token值
     * @return
     */
    public static String deTokenTimes(String token) {
        if (TextUtils.isEmpty(token)) {
            return "";
        }
        String[] tokens = token.split(":");
        return tokens[0];
    }

    /**
     * DES算法，加密
     *
     * @param data 待加密字符串
     * @param key  加密私钥，长度不能够小于8位
     * @return 加密后的字节数组，一般结合Base64编码使用
     * 异常
     */
    public static String encode(String key, String data) throws Exception {
        return encode(key, data.getBytes());
    }

    /**
     * DES算法，加密
     *
     * @param data 待加密字符串
     * @param key  加密私钥，长度不能够小于8位
     * @return 加密后的字节数组，一般结合Base64编码使用
     * 异常
     */
    public static String encode(String key, byte[] data) throws Exception {
        try {
            DESKeySpec dks = new DESKeySpec(key.getBytes());

            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            // key的长度不能够小于8位字节
            Key secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec("7a2156h3".getBytes());
            AlgorithmParameterSpec paramSpec = iv;
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, paramSpec);

            byte[] bytes = cipher.doFinal(data);

            return Base64.encodeToString(bytes, Base64.NO_WRAP);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    /**
     * DES算法，解密
     *
     * @param data 待解密字符串
     * @param key  解密私钥，长度不能够小于8位
     * @return 解密后的字节数组
     * @throws Exception 异常
     */
    public static byte[] decode(String key, byte[] data) throws Exception {
        try {
            SecureRandom sr = new SecureRandom();
            DESKeySpec dks = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            // key的长度不能够小于8位字节
            Key secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec("7a2156h3".getBytes());
            AlgorithmParameterSpec paramSpec = iv;
            cipher.init(Cipher.DECRYPT_MODE, secretKey, paramSpec);
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    /**
     * 获取系统时间
     *
     * @return
     */
    public static String getSystemTiem() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd HHmmssSS");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        return formatter.format(curDate) + "00";
    }

    /**
     * 银行卡掩码
     *
     * @param bankNum
     * @return
     */
    public static String getStartAndEndBankNum(String bankNum) {

        String startBankNum = bankNum.substring(0, 4);
        String endBankNum = bankNum.substring(bankNum.length() - 4, bankNum.length());
        StringBuffer sb = new StringBuffer();
        sb.append(startBankNum).append("**********").append(endBankNum);
        return sb.toString();
    }


    /**
     *
     */

    public static String getStartAndEndMobile(String mobile) {

        if (TextUtils.isEmpty(mobile)) {
            return "";
        }
        String startMobile = mobile.substring(0, 3);
        String endMobile = mobile.substring(mobile.length() - 4, mobile.length());
        StringBuffer sb = new StringBuffer();
        sb.append(startMobile).append("****").append(endMobile);
        return sb.toString();
    }

    public static String URLEncoder(String text) {
        if (TextUtils.isEmpty(text)) {
            return text;
        }
        try {
            text = URLEncoder.encode(text, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return text;
    }

}
