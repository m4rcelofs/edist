package com.erep.dist;

import static org.junit.Assert.*;

import org.junit.Test;

import static com.erep.dist.auth.SymmetricCipherUtils.*;

public class CipherUtilsTest {

	@Test
	public void test() throws Exception {

		String key = "random string";
		String original = "string encryption test";

		String encrypted = encrypt(key, original);

		System.out.println("Encrypted string: " + encrypted);

		String decrypted = decrypt(key, encrypted);

		assertEquals(original, decrypted);

	}

}
