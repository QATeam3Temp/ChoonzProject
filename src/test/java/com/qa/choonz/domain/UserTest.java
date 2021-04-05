package com.qa.choonz.domain;



import static org.assertj.core.api.Assertions.fail;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

import javax.persistence.Id;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.qa.choonz.persistence.domain.User;

import nl.jqno.equalsverifier.EqualsVerifier;


public class UserTest {
		
	
	
	@Test
	 void testEquals() {
		EqualsVerifier.simple().forClass(User.class).withIgnoredAnnotations(javax.persistence.Id.class).verify();
	}
	@Test
	 void ConstructorTest() {
		try {
			User user = new User("Cowiejr","password");
			Assertions.assertEquals(user.toString(),("User [id=" + user.getId() + ", username=" + user.getUsername() + ", password=" + user.getPassword() + ", salt="
					+ Arrays.toString(user.getSalt()) + "]"));
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
			Assertions.fail();
		}
		
	}

}