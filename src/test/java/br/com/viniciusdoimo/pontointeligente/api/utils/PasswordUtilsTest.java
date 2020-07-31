package br.com.viniciusdoimo.pontointeligente.api.utils;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
public class PasswordUtilsTest {
	
	private static final String PASSWORD = "123456";
	private final BCryptPasswordEncoder bCryptEncoder = new BCryptPasswordEncoder();

	@Test
	public void testNullPassword() throws Exception {
		assertNull(PasswordUtils.generateBCrypt(null));
	}
	
	@Test
	public void testGenerateHashPassword() throws Exception {
		String hash = PasswordUtils.generateBCrypt(PASSWORD);
		
		assertTrue(bCryptEncoder.matches(PASSWORD, hash));
	}

}
