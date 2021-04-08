package com.qa.choonz.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.qa.choonz.persistence.domain.Album;
import com.qa.choonz.persistence.domain.Genre;
import com.qa.choonz.persistence.repository.GenreRepository;
import com.qa.choonz.rest.dto.AlbumDTO;
import com.qa.choonz.rest.dto.GenreDTO;
import com.qa.choonz.utils.mappers.GenreMapper;

@SpringBootTest
public class GenreServiceUnitTest {

	@Autowired
	private GenreService service;

	@MockBean
	private GenreRepository repo;

	@MockBean
	private GenreMapper mapper;

	private List<Long> emptyList = new ArrayList<Long>();
	private List<Genre> genre;
	private List<GenreDTO> genreDTO;

	private Genre validGenre;
	private GenreDTO validGenreDTO;

	@BeforeEach
	public void init() {
		validGenre = new Genre(1, "test", "test", null);
		validGenreDTO = new GenreDTO(1, "test", "test", emptyList);

		genre = new ArrayList<Genre>();
		genreDTO = new ArrayList<GenreDTO>();

		genre.add(validGenre);
		genreDTO.add(validGenreDTO);
	}

	@Test
	public void createGenreTest() {
		when(repo.save(Mockito.any(Genre.class))).thenReturn(validGenre);
		when(mapper.MapToDTO(Mockito.any(Genre.class))).thenReturn(validGenreDTO);
		when(mapper.MapFromDTO(Mockito.any(GenreDTO.class))).thenReturn(validGenre);
		assertThat(validGenreDTO).isEqualTo(service.create(validGenreDTO));
		verify(repo, times(1)).save(Mockito.any(Genre.class));
		verify(mapper, times(1)).MapFromDTO(Mockito.any(GenreDTO.class));
		verify(mapper, times(1)).MapToDTO(Mockito.any(Genre.class));
	}
	
	@Test
	public void readGenresTest() {
		when(repo.findAll()).thenReturn(genre);
		when(mapper.MapToDTO(Mockito.any(Genre.class))).thenReturn(validGenreDTO);
		assertThat(genreDTO).isEqualTo(service.read());
		verify(repo, times(1)).findAll();
		verify(mapper, times(1)).MapToDTO(Mockito.any(Genre.class));
	}
	
	@Test
	public void readGenreIdTest() {
		when(repo.findById(Mockito.anyLong())).thenReturn(Optional.of(validGenre));
		when(mapper.MapToDTO(Mockito.any(Genre.class))).thenReturn(validGenreDTO);
		assertThat(validGenreDTO).isEqualTo(service.read(validGenreDTO.getId()));
		verify(repo, times(1)).findById(validGenreDTO.getId());
		verify(mapper, times(1)).MapToDTO(Mockito.any(Genre.class));
	}
	
	@Test
	public void readGenreNameTest() {
		when(repo.getGenreByNameJPQL(validGenreDTO.getName())).thenReturn(validGenre);
		when(mapper.MapToDTO(Mockito.any(Genre.class))).thenReturn(validGenreDTO);
		assertThat(validGenreDTO).isEqualTo(service.read(validGenreDTO.getName()));
		verify(repo, times(1)).getGenreByNameJPQL(validGenreDTO.getName());
		verify(mapper, times(1)).MapToDTO(Mockito.any(Genre.class));
	}

	@Test
	public void deleteGenreTest() {
		when(repo.existsById(Mockito.anyLong())).thenReturn(true).thenReturn(false);
		System.out.println(validGenre.getId());
		assertThat(true).isEqualTo(service.delete(validGenre.getId()));

		verify(repo, times(2)).existsById(Mockito.anyLong());
		verify(repo, times(1)).deleteById(Mockito.anyLong());
	}

}
