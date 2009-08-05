package de.uplinkgmbh.lms.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.Ostermiller.util.Base64;

public abstract class Tokenaizer {

	private static final byte[] key1 = { 14, 16, 3, 4, 12, 6, 7, 8, 10, 9, 11, 5, 13, 1, 15, 2 };
	private static final byte[] IV = { '0', '1', 'A', '3', '4', '3', '6', 'E', '8', '9', 'A', 'B', 'F', 'D', 'E', 'E' };
	
	public static String buildAESToken( String application, long userId ){
		
		LMSToken token = new LMSToken();
		token.application = application;
		token.userId = userId;
		
		return buildAESToken( token );
	}
	
	public static String buildAESToken( LMSToken token ){
		
		IvParameterSpec ivpara = new IvParameterSpec(IV);
		SecretKeySpec Key = new SecretKeySpec(key1, "AES");
		Cipher cipher = null;
		
		try {
			cipher = Cipher.getInstance( "AES/CBC/PKCS5Padding" );
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		}
		
		try {
			cipher.init(Cipher.ENCRYPT_MODE, Key, ivpara);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		}
		
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		ObjectOutputStream s;
		try {
			s = new ObjectOutputStream( bout );
			s.writeObject( token );
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		String AESToken = "";
		try {
			AESToken = new String( Base64.encode( cipher.doFinal( bout.toByteArray() ) ) );
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return AESToken;
	}
	
	public static LMSToken restoreLMSToken( byte[] serializedAESToken ){
		
		IvParameterSpec ivpara = new IvParameterSpec(IV);
		SecretKeySpec Key = new SecretKeySpec(key1, "AES");
		Cipher cipher = null;
		
		try {
			cipher = Cipher.getInstance( "AES/CBC/PKCS5Padding" );
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		}
		
		try {
			cipher.init( Cipher.DECRYPT_MODE, Key, ivpara );
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		}
		
		LMSToken lmstoken = null;
		try {
			byte[] token = cipher.doFinal( Base64.decode( serializedAESToken ) );
			ByteArrayInputStream in = new ByteArrayInputStream( token );
			ObjectInputStream is = new ObjectInputStream( in );
			lmstoken = (LMSToken)is.readObject();
		} catch (IllegalBlockSizeException e1) {
			e1.printStackTrace();
			lmstoken = null;
		} catch (BadPaddingException e1) {
			e1.printStackTrace();
			lmstoken = null;
		} catch (IOException e) {
			e.printStackTrace();
			lmstoken = null;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			lmstoken = null;
		}
		return lmstoken;
	}
}
