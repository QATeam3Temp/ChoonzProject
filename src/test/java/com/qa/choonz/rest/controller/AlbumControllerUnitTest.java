package com.qa.choonz.rest.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.qa.choonz.persistence.domain.Album;
import com.qa.choonz.rest.dto.AlbumDTO;
import com.qa.choonz.service.AlbumService;
import com.qa.choonz.utils.UserSecurity;

@SpringBootTest
public class AlbumControllerUnitTest {

	@Autowired
	private AlbumController controller;

	@MockBean
	private AlbumService service;
	
	@MockBean
	private UserSecurity security;

	private List<Long> emptyList = new ArrayList<Long>();
	private List<Album> album;
	private List<AlbumDTO> albumDTO;

	private Album validAlbum;
	private AlbumDTO validAlbumDTO;

	@BeforeEach
	public void init() {
		validAlbum = new Album(1, "test", null, null, null, "test");
		validAlbumDTO = new AlbumDTO(1, "test", emptyList, 0L, 0L, "test");

		album = new ArrayList<Album>();
		albumDTO = new ArrayList<AlbumDTO>();

		album.add(validAlbum);
		albumDTO.add(validAlbumDTO);
	}

	@Test
	public void createAlbumTest() {
		when(service.create(validAlbumDTO)).thenReturn(validAlbumDTO);
		when(security.testKey(Mockito.anyString())).thenReturn(true);
		ResponseEntity<AlbumDTO> response = new ResponseEntity<AlbumDTO>(validAlbumDTO, HttpStatus.CREATED);
		assertThat(response).isEqualTo(controller.create(validAlbumDTO, "Imahash"));
		verify(service, times(1)).create(validAlbumDTO);
	}

	@Test
	public void createAlbumUnauthorisedTest() {
		when(service.create(validAlbumDTO)).thenReturn(validAlbumDTO);
		when(security.testKey(Mockito.anyString())).thenReturn(false);
		ResponseEntity<AlbumDTO> response = new ResponseEntity<AlbumDTO>(HttpStatus.UNAUTHORIZED);
		assertThat(response).isEqualTo(controller.create(validAlbumDTO, null));
	}

	@Test
	public void readAlbumTest() {
		when(service.read()).thenReturn(albumDTO);
		ResponseEntity<List<AlbumDTO>> response = new ResponseEntity<List<AlbumDTO>>(albumDTO, HttpStatus.OK);
		assertThat(response).isEqualTo(controller.read());
		verify(service, times(1)).read();
	}

	@Test
	public void readAlbumIdTest() {
		when(service.read(validAlbumDTO.getId())).thenReturn(validAlbumDTO);
		ResponseEntity<AlbumDTO> response = new ResponseEntity<AlbumDTO>(validAlbumDTO, HttpStatus.OK);
		assertThat(response).isEqualTo(controller.read(validAlbumDTO.getId()));
		verify(service, times(1)).read(validAlbumDTO.getId());
	}

	@Test
	public void readAlbumNameTest() {
		when(service.read(validAlbumDTO.getName())).thenReturn(validAlbumDTO);
		ResponseEntity<AlbumDTO> response = new ResponseEntity<AlbumDTO>(validAlbumDTO, HttpStatus.OK);
		assertThat(response).isEqualTo(controller.getAlbumByName(validAlbumDTO.getName()));
		verify(service, times(1)).read(validAlbumDTO.getName());
	}

	@Test
	public void deleteAlbumTest() {
		when(service.delete(Mockito.anyLong())).thenReturn(true);
		when(security.testKey(Mockito.anyString())).thenReturn(true);
		ResponseEntity<Boolean> response = new ResponseEntity<Boolean>(true,HttpStatus.NO_CONTENT);
		assertThat(response).isEqualTo(controller.delete(validAlbumDTO.getId(), "imakey"));
	}
	
	@Test
	public void readAlbumArtistTest() {
		when(service.readByArtist(validAlbumDTO.getArtist())).thenReturn(albumDTO);
		ResponseEntity<List<AlbumDTO>> response = new ResponseEntity<List<AlbumDTO>>(albumDTO, HttpStatus.OK);
		assertThat(response).isEqualTo(controller.readByArtist(validAlbumDTO.getArtist()));
		verify(service, times(1)).readByArtist(validAlbumDTO.getArtist());
	}
	
	@Test
	public void readAlbumGenreTest() {
		when(service.readByGenre(validAlbumDTO.getGenre())).thenReturn(albumDTO);
		ResponseEntity<List<AlbumDTO>> response = new ResponseEntity<List<AlbumDTO>>(albumDTO, HttpStatus.OK);
		assertThat(response).isEqualTo(controller.readByGenre(validAlbumDTO.getGenre()));
		verify(service, times(1)).readByGenre(validAlbumDTO.getGenre());
	}

}
