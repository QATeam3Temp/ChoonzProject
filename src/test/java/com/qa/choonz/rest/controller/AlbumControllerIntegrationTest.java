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
import com.qa.choonz.persistence.domain.Artist;
import com.qa.choonz.persistence.domain.Genre;
import com.qa.choonz.rest.dto.AlbumDTO;
import com.qa.choonz.rest.dto.ArtistDTO;
import com.qa.choonz.rest.dto.GenreDTO;
import com.qa.choonz.rest.dto.TrackDTO;
import com.qa.choonz.service.AlbumService;
import com.qa.choonz.service.ArtistService;
import com.qa.choonz.service.GenreService;
import com.qa.choonz.service.TrackService;
import com.qa.choonz.utils.mappers.AlbumMapper;

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
	TrackService tService;
	
	@Autowired
	GenreService gService;
	
	@Autowired
	ArtistService aService;
	
	@Autowired
	AlbumMapper mapper;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	TrackDTO validTrackDTO = new TrackDTO();
	GenreDTO validGenreDTO = new GenreDTO();
	ArtistDTO validArtistDTO = new ArtistDTO();
	AlbumDTO albumDTO = new AlbumDTO();

	
	@BeforeEach
	void init () {
		validTrackDTO = new TrackDTO("test", 1000, "test");
		tService.create(validTrackDTO);
		validGenreDTO = new GenreDTO("test", "test");
		gService.create(validGenreDTO);
		validArtistDTO = new ArtistDTO("test");
		System.out.println(validArtistDTO);
		aService.create(validArtistDTO);
		albumDTO = new AlbumDTO(1, "test",List.of(1L), 1L, 1L, "test");
		List<AlbumDTO> validAlbumDTO = List.of(albumDTO);
		albumDTO = service.create(albumDTO);
		validAlbumDTO = List.of(albumDTO);
	}
	
	@Test
	public void createAlbumTest() {
		
	}

}
