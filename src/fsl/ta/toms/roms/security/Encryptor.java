package fsl.ta.toms.roms.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.lang.exception.NestableRuntimeException;


public class Encryptor {
private static String algorithm = "md5";
	
	public static String encrypt(String plainText)
		throws NestableRuntimeException {
		try {
			return encrypt(plainText, algorithm);
		} catch (NoSuchAlgorithmException e) {
			throw new NestableRuntimeException(
				"Could not encrypt the string",
				e);
		}
	}
	
	public static String encrypt(String plainText, String algorithm)
		throws NoSuchAlgorithmException {
		MessageDigest mdAlgorithm = MessageDigest.getInstance(algorithm);
	
		mdAlgorithm.update(plainText.getBytes());
	
		byte[] digest = mdAlgorithm.digest();
		StringBuffer hexString = new StringBuffer();
	
		for (int i = 0; i < digest.length; i++) {
	
			plainText = Integer.toHexString(0xFF & digest[i]);
	
			if (plainText.length() < 2) {
				plainText = "0" + plainText;
			}
	
			hexString.append(plainText);
		}
	
		return hexString.toString();
	}
	
}
