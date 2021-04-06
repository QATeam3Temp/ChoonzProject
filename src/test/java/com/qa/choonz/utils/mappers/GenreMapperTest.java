package com.qa.choonz.utils.mappers;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.qa.choonz.persistence.domain.Album;
import com.qa.choonz.persistence.domain.Genre;
import com.qa.choonz.persistence.repository.AlbumRepository;
import com.qa.choonz.rest.dto.GenreDTO;

@SpringBootTest
public class GenreMapperTest {

	@MockBean
	private AlbumRepository aRepo;
	
	@Autowired
	private GenreMapper mapper;

	Album validAlbum = new Album();
	Genre validGenre = new Genre(1L,"Eurobeat","Europe but the fun parts",List.of(validAlbum));
	GenreDTO validGenreDTO = new GenreDTO(1L,"Eurobeat","Europe but the fun parts",List.of(0L));
	@Test
	void mapToDTOTest() {
		Assertions.assertEquals(validGenreDTO, mapper.MapToDTO(validGenre));

	}
	@Test
	void mapFromDTOTest() {

		when(aRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(validAlbum));
		Assertions.assertEquals(validGenre, mapper.MapFromDTO(validGenreDTO));

		verify(aRepo, times(1)).findById(Mockito.anyLong());
	}
}
