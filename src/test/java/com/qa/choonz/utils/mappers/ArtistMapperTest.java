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
import com.qa.choonz.persistence.domain.Artist;
import com.qa.choonz.persistence.repository.AlbumRepository;
import com.qa.choonz.rest.dto.ArtistDTO;

@SpringBootTest
public class ArtistMapperTest {

	@MockBean
	private AlbumRepository aRepo;
	
	@Autowired
	private ArtistMapper mapper;
	
	Album validAlbum = new Album();
	Artist validArtist = new Artist(1L,"Rachie",List.of(validAlbum));
	ArtistDTO validArtistDTO = new ArtistDTO(1L,"Rachie",List.of(0L));
	
	@Test
	void mapToDTOTest() {
		Assertions.assertEquals(validArtistDTO, mapper.MapToDTO(validArtist));

	}
	@Test
	void mapFromDTOTest() {

		when(aRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(validAlbum));
		Assertions.assertEquals(validArtist, mapper.MapFromDTO(validArtistDTO));
		verify(aRepo, times(1)).findById(Mockito.anyLong());
	}
	
}
