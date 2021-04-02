package com.qa.choonz.service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qa.choonz.persistence.domain.User;
import com.qa.choonz.persistence.repository.UserRepository;
import com.qa.choonz.rest.dto.UserDTO;
import com.qa.choonz.utils.userSecurity;

@Service
public class UserService {
	
	private UserRepository userRepository;
	
	private ModelMapper mapper;
	
	@Autowired
	public UserService(UserRepository userRepository, ModelMapper mapper) {
		this.userRepository = userRepository;
		this.mapper = mapper;
	}
	
	private UserDTO mapToDTO(User user) {
		return this.mapper.map(user, UserDTO.class);
	}
	
	public UserDTO create(@Valid UserDTO userDTO) throws NoSuchAlgorithmException, InvalidKeySpecException {
		User user = new User(userDTO.getUsername(),userDTO.getPassword());

		User newUser = this.userRepository.save(user);
		return this.mapToDTO(newUser);
	}
	
	public Boolean login(User user, String password) {
		return userSecurity.verifyLogin(user,password);
	}
	
	public List<UserDTO> read() {
		return this.userRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
	}
	
	
}
