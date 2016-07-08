package org.easyarch.jdfs.common.codec;/**
 * Description : 
 * Created by YangZH on 16-7-3
 *  下午5:36
 */

import org.apache.commons.codec.binary.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Description :
 * Created by YangZH on 16-7-3
 * 下午5:36
 */

public class Codec {

    public static long hash(String key) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md5.update(key.getBytes());
        byte[] bKey = md5.digest();
        long res = ((long) (bKey[3] & 0xFF) << 24) | ((long) (bKey[2] & 0xFF) << 16) | ((long) (bKey[1] & 0xFF) << 8)
                | (long) (bKey[0] & 0xFF);
        return res & 0xffffffffL;
    }

    public static String sha1(byte[] data){
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            return bytes2Hex(md.digest(data));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String base64(byte[] data){
        return Base64.encodeBase64URLSafeString(data);
    }

    private static String bytes2Hex(byte[]bts) {
        String des="";
        String tmp=null;
        for (int i=0;i<bts.length;i++) {
            tmp=(Integer.toHexString(bts[i] & 0xFF));
            if (tmp.length()==1) {
                des+="0";
            }
            des+=tmp;
        }
        return des;
    }
}
