package com.qa.choonz.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.qa.choonz.persistence.domain.User;
import com.qa.choonz.persistence.repository.UserRepository;
import com.qa.choonz.rest.dto.UserDTO;
import com.qa.choonz.utils.UserSecurity;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

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

	static ExtentReports report = new ExtentReports("Documentation/reports/User_Service_Integration_Report.html", true);
	static ExtentTest test;

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
		test = report.startTest("Create user test");
		UserDTO createUserDTO = new UserDTO("UserJr", "pa$$word");
		UserDTO ExpectedUserDTO = new UserDTO(validUserDTO.getId() + 1, "UserJr", "pa$$word");
		try {
			UserDTO created = service.create(createUserDTO);
			assertThat(ExpectedUserDTO.getId()).isEqualTo(created.getId());
			assertThat(ExpectedUserDTO.getUsername()).isEqualTo(created.getUsername());
			assertThat(createUserDTO.getPassword()).isNotEqualTo(created.getPassword());
			test.log(LogStatus.PASS, "Ok");
			report.endTest(test);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			test.log(LogStatus.FAIL, "UserService Error");
			Assertions.fail();
		}

	}

	@Test
	void loginTest() {
		test = report.startTest("Login user test");
		assertThat(true).isEqualTo(service.login(loginUserDTO));
		test.log(LogStatus.PASS, "Ok");
		report.endTest(test);

	}

	@AfterAll
	static void Exit() {
		report.flush();
	}
}
