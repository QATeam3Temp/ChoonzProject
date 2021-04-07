package com.qa.choonz.rest.dto;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.qa.choonz.persistence.domain.User;

import nl.jqno.equalsverifier.EqualsVerifier;

public class UserDTOTest {

	
	@Test
	void testEquals() {
		EqualsVerifier.simple().forClass(UserDTO.class).verify();
	}
	@Test
	void ConstructorTest() {
		
			UserDTO user = new UserDTO("Cowiejr", "password");
			Assertions.assertEquals(user.toString(), ("UserDTO [id=" + user.getId() + ", username=" + user.getUsername()
					+ ", password=" + user.getPassword() + "]"));
		

	}
}
