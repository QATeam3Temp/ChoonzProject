package com.qa.choonz.rest.controller;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qa.choonz.rest.dto.AlbumDTO;
import com.qa.choonz.service.AlbumService;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = { "classpath:test-schema.sql" },
executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class AlbumControllerIntegrationTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	AlbumService service;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private AlbumDTO albumDTO = new AlbumDTO("test", "test");
	
	private List<AlbumDTO> validAlbumDTO = List.of(albumDTO);
	
	@BeforeEach
	void init () {
		albumDTO = service.create(albumDTO);
		validAlbumDTO = List.of(albumDTO);
	}
	
	@Test
	public void createAlbumTest() {
		
	}

}
