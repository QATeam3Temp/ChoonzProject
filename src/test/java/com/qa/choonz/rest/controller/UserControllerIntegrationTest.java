package com.qa.choonz.rest.controller;

import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qa.choonz.rest.dto.UserDTO;
import com.qa.choonz.service.UserService;
import com.qa.choonz.utils.UserSecurity;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Sql(scripts = { "classpath:test-schema.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class UserControllerIntegrationTest {

	@Autowired
	private MockMvc mvc;
	@Autowired
	private UserController userController;

	@Autowired
	private UserSecurity security;

	@Autowired
	private UserService service;
	@Autowired
	private ObjectMapper objectMapper;

	static ExtentReports report = new ExtentReports("Documentation/reports/User_Controller_Integration_Report.html",
			true);
	static ExtentTest test;

	static UserDTO validUserDTO;
	static UserDTO createUserDTO;
	static UserDTO badLoginUserDTO;

	@BeforeEach
	void init() {
		try {
			createUserDTO = new UserDTO(1,"CowieJr", "password");
			validUserDTO = service.create(createUserDTO);
			createUserDTO.setId(validUserDTO.getId());
			badLoginUserDTO = new UserDTO(validUserDTO.getId(), "CowieJr", "pa$$word");
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
		}
	}

	@AfterAll
	static void Exit() {
		report.flush();
	}

	@Test
	void createUserTest() throws Exception {
		test = report.startTest("Create user test");
		UserDTO newUser = new UserDTO("username", "passsword");

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.POST, "/users/signup");
		mockRequest.contentType(MediaType.APPLICATION_JSON);
		mockRequest.content(objectMapper.writeValueAsString(newUser));
		mockRequest.accept(MediaType.APPLICATION_JSON);
		ResultMatcher statusMatcher = MockMvcResultMatchers.status().isCreated();
		ResultMatcher headerMatcher = MockMvcResultMatchers.header().string("Location", "2");
		ResultMatcher headerMatcher2 = MockMvcResultMatchers.header().string("Key", any(String.class));
		mvc.perform(mockRequest).andExpect(statusMatcher).andExpect(jsonPath("id", is(validUserDTO.getId() + 1)))
				.andExpect(jsonPath("password", any(String.class))).andExpect(jsonPath("username", is("username")))
				.andExpect(headerMatcher).andExpect(headerMatcher2);
		test.log(LogStatus.PASS, "Ok");
	}

	@Test
	void loginTest() throws Exception {
		test = report.startTest("Login test");
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.POST, "/users/login");
		mockRequest.contentType(MediaType.APPLICATION_JSON);
		mockRequest.content(objectMapper.writeValueAsString(createUserDTO));
		System.out.println(createUserDTO);
		mockRequest.accept(MediaType.APPLICATION_JSON);
		ResultMatcher statusMatcher = MockMvcResultMatchers.status().isOk();
		ResultMatcher headerMatcher = MockMvcResultMatchers.header().string("Key", any(String.class));
		ResultMatcher contentMatcher = MockMvcResultMatchers.content().string("true");
		mvc.perform(mockRequest).andExpect(statusMatcher).andExpect(headerMatcher).andExpect(contentMatcher);
		test.log(LogStatus.PASS, "Ok");
	}

}
