package com.qa.choonz.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.qa.choonz.persistence.domain.User;
import com.qa.choonz.persistence.repository.UserRepository;
import com.qa.choonz.rest.dto.UserDTO;
import com.qa.choonz.utils.UserSecurity;

@SpringBootTest
public class UserServiceUnitTest {

	@Autowired
	private UserService service;

	@MockBean
	private UserRepository repo;

	@MockBean
	private UserSecurity security;
	
	static User validUser;
	static UserDTO validUserDTO;
	static UserDTO createUserDTO;

	@BeforeAll
	static void init() {
		try {
			validUser = new User(1, "CowieJr", "password");
			validUserDTO = new UserDTO(1, "CowieJr", validUser.getPassword());
			createUserDTO = new UserDTO(1, "CowieJr", "password");
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			// test.log(LogStatus.FAIL, "UserService Error");
			Assertions.fail();
		}
	}

	@Test
	void createTest() {
		when(repo.save(Mockito.any(User.class))).thenReturn(validUser);
		try {
			assertThat(validUserDTO).isEqualTo(service.create(createUserDTO));
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			// test.log(LogStatus.FAIL, "UserService Error");
			Assertions.fail();
		}
		verify(repo, times(1)).save(Mockito.any(User.class));

	}

	@Test
	void loginTest() {
		when(repo.findbyName(Mockito.anyString())).thenReturn(Optional.of(validUser));
		when(security.verifyLogin(Mockito.any(User.class), Mockito.anyString())).thenReturn(true);
		assertThat(true).isEqualTo(service.login(createUserDTO));
		verify(repo, times(1)).findbyName(Mockito.anyString());
		verify(security, times(1)).verifyLogin(Mockito.any(User.class), Mockito.anyString());

	}
}
