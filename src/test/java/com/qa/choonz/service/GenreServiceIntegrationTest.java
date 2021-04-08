package com.qa.choonz.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.qa.choonz.persistence.domain.Album;
import com.qa.choonz.persistence.domain.Genre;
import com.qa.choonz.persistence.repository.AlbumRepository;
import com.qa.choonz.persistence.repository.GenreRepository;
import com.qa.choonz.rest.dto.GenreDTO;

@SpringBootTest
public class GenreServiceIntegrationTest {

	@Autowired
	private AlbumRepository aRepo;
	
	@Autowired
	private GenreService service;
	
	@Autowired
	private GenreRepository repo;
	
	private List<Long> emptyList = new ArrayList<Long>();
	private List<Genre> genre = new ArrayList<Genre>();
	private List<GenreDTO> genreDTO = new ArrayList<GenreDTO>();
	private Album validAlbum = new Album(1, "test", null, null, null, "test");
	private List<Album> validAlbums = new ArrayList<Album>();
	private Genre validGenre;
	private GenreDTO validGenreDTO;
	
	@BeforeEach
	public void init() {
		repo.deleteAll();
		validAlbum = aRepo.save(validAlbum);
		validGenre = new Genre(1, "test", "test", validAlbums);
		genre = new ArrayList<Genre>();
		genreDTO = new ArrayList<GenreDTO>();
		validGenre = repo.save(validGenre);
		validGenreDTO = service.map(validGenre);
		genre.add(validGenre);
		genreDTO.add(validGenreDTO);
	}
	
	@Test
	public void createGenreTest() {
		GenreDTO newGenre = new GenreDTO("test2", "test2");
		GenreDTO expectedGenre = new GenreDTO(validGenre.getId()+1, "test2", "test2", emptyList);
		assertThat(expectedGenre).isEqualTo(service.create(newGenre));
	}
	
	@Test
	public void readAllGenreTest() {
		List<GenreDTO> genreInDb = service.read();
		assertThat(genreDTO).isEqualTo(genreInDb);
	}
	
	@Test
	public void readGenreIdTest() {
		assertThat(validGenreDTO).isEqualTo(service.read(validGenre.getId()));
	}
	
	@Test
	public void readGenreNameTest() {
		assertThat(validGenreDTO).isEqualTo(service.read(validGenre.getName()));
	}
	
	@Test
	public void updateGenreTest() {
		GenreDTO sentGenre = new GenreDTO("updated", "updated");
		GenreDTO responseGenre = new GenreDTO(validGenre.getId(), "updated", "updated", emptyList);
		GenreDTO updatedGenre = service.update(sentGenre, validGenre.getId());
		
		assertThat(responseGenre).isEqualTo(updatedGenre);
	}
	
	@Test
	public void deleteGenreTest() {
		assertThat(true).isEqualTo(service.delete(validGenre.getId()));
	}
	
}
