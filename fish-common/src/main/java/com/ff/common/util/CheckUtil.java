package com.ff.common.util;

import java.security.MessageDigest;
import java.util.Arrays;


public class CheckUtil {
//开发平台自己填写的
private static final String token = "yichengyuan";
public static boolean checkSignature(String signature,String timestamp,String nonce){
		
		String[] arr = new String[] { token, timestamp, nonce };
		
		// 排序
		Arrays.sort(arr);
		// 生成字符串
		StringBuilder content = new StringBuilder();
		for (int i = 0; i < arr.length; i++) {
			content.append(arr[i]);
		}
		
		// sha1加密
		String temp = getSHA1String(content.toString());
		
		return temp.equals(signature); // 与微信传递过来的签名进行比较
	}

private static String getSHA1String(String str){
	if(str==null||str.length()==0){
        return null;
    }
    char hexDigits[] = {'0','1','2','3','4','5','6','7','8','9',
            'a','b','c','d','e','f'};
    try {
        MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
        mdTemp.update(str.getBytes("UTF-8"));

        byte[] md = mdTemp.digest();
        int j = md.length;
        char buf[] = new char[j*2];
        int k = 0;
        for (int i = 0; i < j; i++) {
            byte byte0 = md[i];
            buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
            buf[k++] = hexDigits[byte0 & 0xf];
        }
        return new String(buf);
    } catch (Exception e) {
        return null;
    }	// 使用commons codec生成sha1字符串
}
}
