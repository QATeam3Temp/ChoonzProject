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
	private List<Album> albumList = new ArrayList<Album>();
	private List<Genre> genre;
	private List<GenreDTO> genreDTO;

	private Genre validGenre;
	private GenreDTO validGenreDTO;
	
	@BeforeEach
	public void init() {
		validGenre = new Genre(1, "test", "test", albumList);
		validGenreDTO = new GenreDTO(1, "test", "test", emptyList);
		
		
		
		genre = new ArrayList<Genre>();
		genreDTO = new ArrayList<GenreDTO>();
		albumList = new ArrayList<Album>();

		genre.add(validGenre);
		genreDTO.add(validGenreDTO);

	}
	
	@Test
	public void updateGenreTest() {
		GenreDTO updatedGenreDTO = new GenreDTO("updated genre", "updated genre");
		Genre updatedGenre = new Genre(1, "updated genre", "updated genre", albumList);
		
		when(repo.findById(Mockito.anyLong())).thenReturn(Optional.of(validGenre));
		when(repo.save(Mockito.any(Genre.class))).thenReturn(updatedGenre);
		when(mapper.MapFromDTO(Mockito.any(GenreDTO.class))).thenReturn(updatedGenre);
		when(mapper.MapToDTO(Mockito.any(Genre.class))).thenReturn(updatedGenreDTO);
		
		GenreDTO testUpdatedGenreDTO = service.update(updatedGenreDTO, validGenre.getId());
		
		assertThat(updatedGenreDTO).isEqualTo(testUpdatedGenreDTO);
		
		verify(repo, times(1)).findById(Mockito.anyLong());
		verify(repo, times(1)).save(Mockito.any(Genre.class));
		verify(mapper, times(1)).MapToDTO(Mockito.any(Genre.class));
	}
	
	@Test
	public void deleteGenreTest() {
		when(repo.existsById(Mockito.anyLong())).thenReturn(true).thenReturn(false);
		
		assertThat(true).isEqualTo(service.delete(validGenre.getId()));
		
		verify(repo, times(2)).existsById(Mockito.anyLong());
		verify(repo, times(1)).deleteById(Mockito.anyLong());
	}

}
