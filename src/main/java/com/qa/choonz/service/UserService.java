package com.qa.choonz.service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Optional;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qa.choonz.persistence.domain.User;
import com.qa.choonz.persistence.repository.UserRepository;
import com.qa.choonz.rest.dto.UserDTO;
import com.qa.choonz.utils.UserSecurity;

@Service
public class UserService {

	private UserRepository userRepository;
	private UserSecurity security;
	private ModelMapper mapper;

	@Autowired
	public UserService(UserSecurity security, UserRepository userRepository, ModelMapper mapper) {
		this.userRepository = userRepository;
		this.mapper = mapper;
		this.security = security;
	}

	public UserDTO mapToDTO(User user) {
		return this.mapper.map(user, UserDTO.class);
	}

	public UserDTO create(@Valid UserDTO userDTO) throws NoSuchAlgorithmException, InvalidKeySpecException {
		User user = new User(userDTO);
		User newUser = this.userRepository.save(user);
		return this.mapToDTO(newUser);
	}

	public Boolean login(UserDTO userDTO) {

		Optional<User> user = userRepository.findbyName(userDTO.getUsername());
		if (user.isPresent()) {
			return security.verifyLogin(user.get(), userDTO.getPassword());
		} else {
			return false;
		}
	}

	public UserDTO read(String username) {
		Optional<User> user = userRepository.findbyName(username);
		if (user.isPresent()) {
			return mapToDTO(user.get());
		} else {
			return null;
		}

	}

}
