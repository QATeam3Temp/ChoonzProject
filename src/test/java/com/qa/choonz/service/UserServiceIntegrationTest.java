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
import org.junit.jupiter.api.BeforeEach;
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
public class UserServiceIntegrationTest {

	
	@Autowired
	private UserService service;

	@Autowired
	private UserRepository repo;

	@Autowired
	private UserSecurity security;
	
	static User validUser;
	static UserDTO validUserDTO;
	static UserDTO loginUserDTO;

	@BeforeEach
	void init() {
		try {
			repo.deleteAll();
			validUser = repo.save(new User("CowieJr", "password"));
			validUserDTO = service.mapToDTO(validUser);
			loginUserDTO = new UserDTO((int) validUser.getId(), "CowieJr", "password");
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
		}
	}

	@Test
	void createTest() {
		UserDTO createUserDTO = new UserDTO("UserJr", "pa$$word");
		UserDTO ExpectedUserDTO = new UserDTO(validUserDTO.getId()+1,"UserJr", "pa$$word");
		try {
			UserDTO created = service.create(createUserDTO);
			assertThat(ExpectedUserDTO.getId()).isEqualTo(created.getId());
			assertThat(ExpectedUserDTO.getUsername()).isEqualTo(created.getUsername());
			assertThat(createUserDTO.getPassword()).isNotEqualTo(created.getPassword());
			
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			// test.log(LogStatus.FAIL, "UserService Error");
			Assertions.fail();
		}

	}

	@Test
	void loginTest() {
		assertThat(true).isEqualTo(service.login(loginUserDTO));

	}
}
