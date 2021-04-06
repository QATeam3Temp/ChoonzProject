package com.qa.choonz.rest.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
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
import com.qa.choonz.utils.UserSecurity;

@SpringBootTest
public class UserControllerUnitTest {
	
	@Autowired
	private UserController userController;
	
	@MockBean
	private UserSecurity security;
	
	@MockBean
	private UserService service;
	
	//static ExtentReports  report = new ExtentReports("Documentation/reports/User_Controller_Unit_Report.html", true);
    //static ExtentTest test;
    
    static User validUser;
    static UserDTO validUserDTO;
    static UserDTO createUserDTO;
    static UserDTO badLoginUserDTO;
    @BeforeAll
	 static void init() {
    	try {
			validUser = new User(1,"CowieJr","password");
			validUserDTO = new UserDTO(1,"CowieJr","ImaHash");
			createUserDTO = new UserDTO(1,"CowieJr","password");
			badLoginUserDTO = new UserDTO(1,"CowieJr","pa$$word");
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
		}
    }
    
    @Test
	 void createUserTest() {
    	//test = report.startTest("Create user test");
		try {
			when(service.create(Mockito.any(UserDTO.class))).thenReturn(validUserDTO);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			//test.log(LogStatus.FAIL, "UserService Error");
			Assertions.fail();
		}
		byte[] key = ByteBuffer.allocate(4).putInt(1).array();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Location", String.valueOf(1));
		try {
			headers.add("Key", String.valueOf(UserSecurity.encrypt("CowieJr", key)));
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			//test.log(LogStatus.FAIL, "UserService Error");
			Assertions.fail();
		}
		ResponseEntity<UserDTO> response =
				new ResponseEntity<UserDTO>(validUserDTO,headers,HttpStatus.CREATED);
		try {
			assertThat(response).isEqualTo(userController.createUser(createUserDTO));
		} catch (Exception e) {
			//test.log(LogStatus.FAIL, "UserService Error");
			Assertions.fail();
		}
		
		try {
			verify(service, times(1)).create(Mockito.any(UserDTO.class));
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			//test.log(LogStatus.FAIL, "UserService Error");
			Assertions.fail();
		}

    }
    @Test
	 void loginUserTest() {
    	when(service.login(Mockito.any(UserDTO.class))).thenReturn(true);
    	when(service.read(Mockito.anyString())).thenReturn(validUserDTO);
    	byte[] key = ByteBuffer.allocate(4).putInt(1).array();
		HttpHeaders headers = new HttpHeaders();
		try {
			headers.add("Key", String.valueOf(UserSecurity.encrypt("CowieJr", key)));
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			//test.log(LogStatus.FAIL, "UserService Error");
			Assertions.fail();
		}
		ResponseEntity<Boolean> response =
				new ResponseEntity<>(true, headers,HttpStatus.OK);
		assertThat(response).isEqualTo(userController.loginAsUser(createUserDTO));
    }
    @Test
	 void badLoginUserTest() {
    	when(service.login(Mockito.any(UserDTO.class))).thenReturn(false);
    	when(service.read(Mockito.anyString())).thenReturn(validUserDTO);
		ResponseEntity<Boolean> response =
				new ResponseEntity<>(false,HttpStatus.UNAUTHORIZED);
		assertThat(response).isEqualTo(userController.loginAsUser(badLoginUserDTO));
    }
    
}
