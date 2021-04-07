package com.qa.choonz.rest.controller;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.qa.choonz.persistence.domain.Genre;
import com.qa.choonz.rest.dto.GenreDTO;
import com.qa.choonz.service.GenreService;
import com.qa.choonz.utils.UserSecurity;

@SpringBootTest
public class GenreControllerUnitTest {
	
	@Autowired
	private GenreController controller;
	
	@MockBean
	private GenreService service;
	
	@MockBean
	private UserSecurity security;
	
	private List<Genre> genre;
	private List<GenreDTO> genreDTO;
	
	private Genre validGenre;
	private GenreDTO validGenreDTO;

	@BeforeEach
	public void init() {

	}
	
}
