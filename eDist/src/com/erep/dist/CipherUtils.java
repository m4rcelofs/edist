package com.erep.dist;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

public class CipherUtils {

	private static final String	CIPHER		= "AES/CBC/PKCS5Padding";
	private static final String	ALGORITHM	= "AES";
	private static final int	KEY_LENGTH	= 128;
	private static final int	ITERATIONS	= 65536;

	private static final byte[]	iv			= new byte[] { -47, 80, 82, -98,
			-80, 62, 127, 48, -126, -9, 51, 11, 39, -96, 41, 49 };
	private static final byte[]	salt		= new byte[] { 0x0A, 0x01, 0x15,
			0x3A, 0x44, 0x37, 0x25, 0x1E	};

	private static String		serverPasswordBuffered;

	private static Logger		logger		= Logger.getLogger(CipherUtils.class
													.getName());

	public static String encrypt(String strToEncrypt) throws Exception {
		return encrypt(getServerPassword(), strToEncrypt);
	}

	public static String encrypt(String password, String strToEncrypt)
			throws Exception {

		Cipher cipher = Cipher.getInstance(CIPHER);
		SecretKey secret = derivedKey(password);
		cipher.init(Cipher.ENCRYPT_MODE, secret, new IvParameterSpec(iv));

		return Base64
				.encodeBase64String(cipher.doFinal(strToEncrypt.getBytes()));

	}

	public static String decrypt(String strToDecrypt) throws Exception {
		return decrypt(getServerPassword(), strToDecrypt);
	}

	public static String decrypt(String password, String strToDecrypt)
			throws Exception {

		Cipher cipher = Cipher.getInstance(CIPHER);
		SecretKey secret = derivedKey(password);
		cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));

		return new String(cipher.doFinal(Base64.decodeBase64(strToDecrypt)));

	}

	private static String getServerPassword() throws Exception {
		if (serverPasswordBuffered == null) {
			try (BufferedReader reader = new BufferedReader(new FileReader(
					"WEB-INF/default.key"))) {
				serverPasswordBuffered = IOUtils.toString(reader);
			} catch (FileNotFoundException e) {
				logger.log(Level.SEVERE,
						"Could not find crypto key, using code default");
				return ServerConstants.DEFAULT;
			}
		}
		return serverPasswordBuffered;
	}

	private static SecretKey derivedKey(String password)
			throws InvalidKeySpecException, NoSuchAlgorithmException {

		SecretKeyFactory factory = SecretKeyFactory
				.getInstance("PBKDF2WithHmacSHA1");

		KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS,
				KEY_LENGTH);
		SecretKey tmp = factory.generateSecret(spec);
		return new SecretKeySpec(tmp.getEncoded(), ALGORITHM);
	}

}
