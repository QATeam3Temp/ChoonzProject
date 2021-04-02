package com.qa.choonz.rest.controller;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qa.choonz.persistence.domain.User;
import com.qa.choonz.rest.dto.UserDTO;
import com.qa.choonz.service.UserService;

@RestController
@RequestMapping(path = "/users")
@CrossOrigin
public class UserController {

	private UserService userService;

	@Autowired
	public UserController(UserService userService) {
		super();
		this.userService = userService;
	}

	@PostMapping("/signup")
	public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO) throws Exception {
		UserDTO newUser;
		try {
			newUser = userService.create(userDTO);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		HttpHeaders headers = new HttpHeaders();
		headers.add("Location", String.valueOf(newUser.getId()));

		return new ResponseEntity<UserDTO>(newUser, headers, HttpStatus.CREATED);
	}

	@PostMapping("/login")
	public ResponseEntity<Boolean> loginAsUser(@Valid @RequestBody UserDTO userDTO) {
		return new ResponseEntity<Boolean>(userService.login(user,password), HttpStatus.OK);

	}

	@GetMapping
	public ResponseEntity<List<UserDTO>> getAllUsers() {
		List<UserDTO> data = userService.read();

		return new ResponseEntity<List<UserDTO>>(data, HttpStatus.OK);
	}

}
