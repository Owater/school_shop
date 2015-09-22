/**
 * Copyright (c) 2015
 *
 * Licensed under the UCG License, Version 1.0 (the "License");
 */
package com.siso.app.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * description : DESUtil加密算法
 *
 * @version 1.0
 * @author Owater
 * @createtime : 2015-4-16 下午1:37:33
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                     修改内容
 * --------------- ------------------- -----------------------------------
 * Owater        2015-4-16 下午1:37:33 
 *
 */

public class DESUtil {
	private static byte[] iv = {1,2,3,4,5,6,7,8};
//	private static byte[] iv = {A,1,B,2,C,3,D,4,E,5,F,6,0,7,0,8};
	
	/** 
     * 加密数据
     * @param encryptString 待加密数据
     * @param key 密钥
     * @return 加密后的数据 
     */
    public static String encryptDES(String encryptString, String encryptKey) throws Exception {
//      IvParameterSpec zeroIv = new IvParameterSpec(new byte[8]);
        IvParameterSpec zeroIv = new IvParameterSpec(iv);
        SecretKeySpec key = new SecretKeySpec(encryptKey.getBytes(), "DES");
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
        byte[] encryptedData = cipher.doFinal(encryptString.getBytes());
      
        return Base64.encode(encryptedData);
    }
    
    /** 
     * 解密数据 
     * @param decryptString 待解密数据 
     * @param key 密钥 
     * @return 解密后的数据 
     */
    public static String decryptDES(String decryptString, String decryptKey) throws Exception {
        byte[] byteMi = new Base64().decode(decryptString);
        IvParameterSpec zeroIv = new IvParameterSpec(iv);
//      IvParameterSpec zeroIv = new IvParameterSpec(new byte[8]);
        SecretKeySpec key = new SecretKeySpec(decryptKey.getBytes(), "DES");
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
        byte decryptedData[] = cipher.doFinal(byteMi);
      
        return new String(decryptedData);
    }
}

	