package com.qa.choonz.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qa.choonz.persistence.domain.User;
import com.qa.choonz.persistence.repository.UserRepository;
import com.qa.choonz.rest.dto.UserDTO;

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
	
	public UserDTO create(User user) {
		User newUser = this.userRepository.save(user);
		return this.mapToDTO(newUser);
	}
	
	public List<UserDTO> readAllUsers() {
		return this.userRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
	}
	
	
}
