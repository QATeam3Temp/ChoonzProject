package com.qa.choonz.rest.controller;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
import com.qa.choonz.rest.dto.GenreDTO;
import com.qa.choonz.rest.dto.UserDTO;
import com.qa.choonz.service.GenreService;
import com.qa.choonz.service.UserService;
import com.qa.choonz.utils.mappers.GenreMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = { "classpath:test-schema.sql"},
executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class GenreControllerIntegrationTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	GenreService service;
	

	@Autowired
	UserService uService;
	
	@Autowired
	GenreMapper mapper;
	
	@Autowired
	ObjectMapper objectMapper;
	
	GenreDTO validGenreDTO = new GenreDTO("test","test");
	private UserDTO user = new UserDTO("cowiejr","password");
	private String key = "";
	ArrayList<GenreDTO> validGenreDTOs = new ArrayList<GenreDTO>();
	ArrayList<Long> emptyList = new ArrayList<Long>();
	
	
	@BeforeEach
	void init () {
		
		if(key.isBlank()) {
			try {
				uService.create(user);
				key = "1000:00000001:7f1d6351d49e0bb872d4642ecec60ee3";

			} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		validGenreDTO=service.create(validGenreDTO);
		validGenreDTOs.add(validGenreDTO);
	}
	
	@Test
	public void createGenreTest() throws Exception {
		GenreDTO genreToSave = new GenreDTO("test2","test2");
		GenreDTO expectedGenre = new GenreDTO(validGenreDTO.getId()+1,"test2","test2",emptyList);
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.POST, "/genres/create");
		mockRequest.contentType(MediaType.APPLICATION_JSON);
		mockRequest.header("Key", key);
		mockRequest.content(objectMapper.writeValueAsString(genreToSave));
		mockRequest.accept(MediaType.APPLICATION_JSON);
		ResultMatcher statusMatcher = MockMvcResultMatchers.status().isCreated();
		ResultMatcher contentMatcher = MockMvcResultMatchers.content()
				.json(objectMapper.writeValueAsString(expectedGenre));
		mvc.perform(mockRequest).andExpect(statusMatcher).andExpect(contentMatcher);
	}
	
	@Test
	public void readGenreTest() throws Exception {
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.GET, "/genres/read");
		mockRequest.accept(MediaType.APPLICATION_JSON);
		ResultMatcher statusMatcher = MockMvcResultMatchers.status().isOk();
		ResultMatcher contentMatcher = MockMvcResultMatchers.content()
				.json(objectMapper.writeValueAsString(validGenreDTOs));
		mvc.perform(mockRequest).andExpect(statusMatcher).andExpect(contentMatcher);
	}
	
	@Test
	public void readGenreByIdTest() throws Exception {
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.GET, "/genres/read/id/"+validGenreDTO.getId());
		mockRequest.accept(MediaType.APPLICATION_JSON);
		ResultMatcher statusMatcher = MockMvcResultMatchers.status().isOk();
		ResultMatcher contentMatcher = MockMvcResultMatchers.content()
				.json(objectMapper.writeValueAsString(validGenreDTO));
		mvc.perform(mockRequest).andExpect(statusMatcher).andExpect(contentMatcher);
	}
	
	@Test
	public void readGenreByNameTest() throws Exception {
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.GET, "/genres/read/name/"+validGenreDTO.getName());
		mockRequest.accept(MediaType.APPLICATION_JSON);
		ResultMatcher statusMatcher = MockMvcResultMatchers.status().isOk();
		ResultMatcher contentMatcher = MockMvcResultMatchers.content()
				.json(objectMapper.writeValueAsString(validGenreDTO));
		mvc.perform(mockRequest).andExpect(statusMatcher).andExpect(contentMatcher);
	}
	
	@Test
	public void updateGenreTest() throws Exception {
		GenreDTO genreToSave = new GenreDTO("testaa","testaa");
		GenreDTO expectedGenre = new GenreDTO(validGenreDTO.getId(),"testaa","testaa",emptyList);
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.PUT, "/genres/update/1");
		mockRequest.contentType(MediaType.APPLICATION_JSON);
		mockRequest.content(objectMapper.writeValueAsString(genreToSave));
		mockRequest.accept(MediaType.APPLICATION_JSON);
		mockRequest.header("Key", key);
		ResultMatcher statusMatcher = MockMvcResultMatchers.status().isAccepted();
		ResultMatcher contentMatcher = MockMvcResultMatchers.content()
				.json(objectMapper.writeValueAsString(expectedGenre));
		mvc.perform(mockRequest).andExpect(statusMatcher).andExpect(contentMatcher);
	}
	
	@Test
	public void deleteGenreTest() throws Exception {
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.DELETE, "/genres/delete/"+validGenreDTO.getId());
		mockRequest.contentType(MediaType.APPLICATION_JSON);
		mockRequest.header("Key", key);
		ResultMatcher statusMatcher = MockMvcResultMatchers.status().isNoContent();
		mvc.perform(mockRequest).andExpect(statusMatcher);
	}
}
