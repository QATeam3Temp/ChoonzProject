package com.qa.choonz.rest.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.qa.choonz.persistence.domain.User;
import com.qa.choonz.rest.dto.UserDTO;
import com.qa.choonz.service.UserService;
import com.qa.choonz.utils.TestWatch;
import com.qa.choonz.utils.UserSecurity;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.LogStatus;

@SpringBootTest
@ExtendWith(TestWatch.class)
public class UserControllerUnitTest {

	@Autowired
	private UserController userController;

	@MockBean
	private UserSecurity security;

	@MockBean
	private UserService service;

	ExtentReports report = TestWatch.report;

	static User validUser;
	static UserDTO validUserDTO;
	static UserDTO createUserDTO;
	static UserDTO badLoginUserDTO;

	@BeforeAll
	static void init() {

		validUser = new User(1, "CowieJr", "password");
		validUserDTO = new UserDTO(1, "CowieJr", "ImaHash");
		createUserDTO = new UserDTO(1, "CowieJr", "password");
		badLoginUserDTO = new UserDTO(1, "CowieJr", "pa$$word");

	}

	@AfterAll
	static void Exit() {
		TestWatch.report.flush();
	}

	@Test
	void createUserTest() {
		TestWatch.test = report.startTest("Create user test - controller unit");
		try {
			when(service.create(Mockito.any(UserDTO.class))).thenReturn(validUserDTO);

		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			TestWatch.test.log(LogStatus.FAIL, "UserService Error");
			Assertions.fail();
		}
		byte[] key = ByteBuffer.allocate(4).putInt(1).array();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Location", String.valueOf(1));
		try {
			headers.add("Key", String.valueOf("CowieJr:" + UserSecurity.encrypt("CowieJr", key)));
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			TestWatch.test.log(LogStatus.FAIL, "UserService Error");
			Assertions.fail();
		}
		ResponseEntity<UserDTO> response = new ResponseEntity<UserDTO>(validUserDTO, headers, HttpStatus.CREATED);
		try {
			Assertions.assertEquals(response, userController.createUser(createUserDTO));
			TestWatch.test.log(LogStatus.PASS, "Ok");
			report.endTest(TestWatch.test);
//			if (response.equals(userController.createUser(createUserDTO))) {
//				test.log(LogStatus.PASS, "Ok");
//				report.endTest(test);
//			} else {
//				test.log(LogStatus.FAIL, "Despite proper values being given was unable to create account.");
//				Assertions.fail();
//			}
		} catch (Exception e) {
			TestWatch.test.log(LogStatus.FAIL, "UserService Error");
			Assertions.fail();
		}

		try {
			verify(service, times(1)).create(Mockito.any(UserDTO.class));
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			TestWatch.test.log(LogStatus.FAIL, "UserService Error");
			Assertions.fail();
		}

	}

	@Test
	void loginUserTest() {
		TestWatch.test = report.startTest("Login user test - controller unit");
		when(service.login(Mockito.any(UserDTO.class))).thenReturn(true);
		when(service.read(Mockito.anyString())).thenReturn(validUserDTO);
		byte[] key = ByteBuffer.allocate(4).putInt(1).array();
		HttpHeaders headers = new HttpHeaders();
		try {
			headers.add("Key", "CowieJr:" + UserSecurity.encrypt("CowieJr", key));
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			TestWatch.test.log(LogStatus.FAIL, "UserService Error");
			Assertions.fail();
		}
		ResponseEntity<Boolean> response = new ResponseEntity<>(true, headers, HttpStatus.OK);

		if (response.equals(userController.loginAsUser(createUserDTO))) {
			TestWatch.test.log(LogStatus.PASS, "Ok");
			report.endTest(TestWatch.test);
		} else {
			TestWatch.test.log(LogStatus.FAIL, "unable to log in despire correct login details");
			Assertions.fail();
		}
	}

	@Test
	void badLoginUserTest() {
		TestWatch.test = report.startTest("Bad Login user test - controller unit");
		when(service.login(Mockito.any(UserDTO.class))).thenReturn(false);
		when(service.read(Mockito.anyString())).thenReturn(validUserDTO);
		ResponseEntity<Boolean> response = new ResponseEntity<>(false, HttpStatus.UNAUTHORIZED);

		if (response.equals(userController.loginAsUser(badLoginUserDTO))) {
			TestWatch.test.log(LogStatus.PASS, "Ok");
			report.endTest(TestWatch.test);
		} else {
			TestWatch.test.log(LogStatus.FAIL, "Able to log in despire incorrect login details");
			Assertions.fail();
		}

	}

}
