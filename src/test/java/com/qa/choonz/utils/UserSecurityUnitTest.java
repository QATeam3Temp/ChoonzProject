package com.qa.choonz.utils;

import static org.mockito.Mockito.when;

import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.qa.choonz.persistence.domain.User;
import com.qa.choonz.persistence.repository.UserRepository;

@SpringBootTest
public class UserSecurityUnitTest {

	@Autowired
	UserSecurity userSecurity;

	@MockBean
	UserRepository userRepository;

	@Test
	void getSaltTest() {
		// assert salt is the right class
		Assertions.assertEquals(UserSecurity.getSalt().getClass(), (byte[].class));
		// assert salt is not the same twice in a row
		Assertions.assertNotEquals(UserSecurity.getSalt(), (UserSecurity.getSalt()));
	}

	@Test
	void testKeyTest() {
		User validUser = null;
		byte[] key = ByteBuffer.allocate(4).putInt(1).array();
			validUser = new User(1, "CowieJr", "password");

		when(userRepository.findbyName(Mockito.anyString())).thenReturn(Optional.of(validUser));
		
		try {
			Assertions.assertTrue(userSecurity.testKey("CowieJr:" + UserSecurity.encrypt("CowieJr", key)));
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			// test.log(LogStatus.FAIL, "UserService Error");
			Assertions.fail();
		}
	}

	@Test
	void testEncrypt() {
		String string = "HELLOMYFRIENDS";
		byte[] key = ByteBuffer.allocate(4).putInt(1).array();
		String expected = "1000:00000001:0c11223eca0643ecc0a7905b6cb16154";
		try {
			Assertions.assertEquals(UserSecurity.encrypt(string, key), expected);
			Assertions.assertNotEquals(UserSecurity.encrypt(string, key), string);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			// test.log(LogStatus.FAIL, "UserService Error");
			Assertions.fail();
		}
	}

	@Test
	void verifyLoginTest() {
		User validUser = new User(1, "CowieJr", "1000:00000001:0c11223eca0643ecc0a7905b6cb16154");
		Assertions.assertTrue(userSecurity.verifyLogin(validUser, "HELLOMYFRIENDS"));
	}
}
