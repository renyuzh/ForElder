package com.heart.util;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class PwdSercurity {

	public static final String DES = "DES";
	public static final String KEY = "heart@www%123";
	

	public static String encode(String originPwd) throws Exception
	{
		byte[] datas = originPwd.getBytes();
		byte[] keys = KEY.getBytes();
		
	
		SecureRandom sr = new SecureRandom();
	
		DESKeySpec dks = new DESKeySpec(keys);
		
	
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		SecretKey secretKey = keyFactory.generateSecret(dks);
		
		Cipher cipher = Cipher.getInstance(DES);
		
		cipher.init(Cipher.ENCRYPT_MODE, secretKey,sr);
		
		byte[] bt = cipher.doFinal(datas);
		
		String result = new BASE64Encoder().encode(bt);
		
		return result;
	}
	
	
	public static String decode(String codeString) throws Exception
	{
		BASE64Decoder decoder = new BASE64Decoder();
		byte[] bufs = decoder.decodeBuffer(codeString);
		byte[] keys = KEY.getBytes();
		
        SecureRandom sr = new SecureRandom();
        
        DESKeySpec dks = new DESKeySpec(keys);
        
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        
        SecretKey secretKey = keyFactory.generateSecret(dks);
        
        Cipher cipher = Cipher.getInstance(DES);
        
        cipher.init(Cipher.DECRYPT_MODE, secretKey,sr);
        
        byte[] bt = cipher.doFinal(bufs);
		
        
		return new String(bt);
	}
}
