package com.qa.choonz.rest.controller;

import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qa.choonz.rest.dto.UserDTO;
import com.qa.choonz.service.UserService;
import com.qa.choonz.utils.UserSecurity;

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
	public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		UserDTO newUser = null;

		try {
			newUser = userService.create(userDTO);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.IM_USED);
		}

		byte[] key = ByteBuffer.allocate(4).putInt(newUser.getId()).array();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Location", String.valueOf(newUser.getId()));
		headers.add("Key", String.valueOf(newUser.getUsername() + ":" + UserSecurity.encrypt(newUser.getUsername(), key)));
		return new ResponseEntity<UserDTO>(newUser, headers, HttpStatus.CREATED);
	}

	@PostMapping("/login")
	public ResponseEntity<String> loginAsUser(@Valid @RequestBody UserDTO userDTO) {
		UserDTO user = userService.read(userDTO.getUsername());
		if (userService.login(userDTO)) {
			byte[] key = ByteBuffer.allocate(4).putInt(user.getId()).array();
			HttpHeaders headers = new HttpHeaders();
			try {
				headers.add("Key", String.valueOf(user.getUsername() + ":" + UserSecurity.encrypt(user.getUsername(), key)));
			} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
				return new ResponseEntity<Boolean>(false, HttpStatus.INTERNAL_SERVER_ERROR);

			}
			return new ResponseEntity<Boolean>(true, headers, HttpStatus.OK);
		}

		return new ResponseEntity<Boolean>(false, HttpStatus.UNAUTHORIZED);

	}

}
