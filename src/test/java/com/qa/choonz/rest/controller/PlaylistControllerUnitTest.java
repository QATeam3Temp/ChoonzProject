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

import com.qa.choonz.persistence.domain.Playlist;
import com.qa.choonz.rest.dto.PlaylistDTO;
import com.qa.choonz.service.PlaylistService;
import com.qa.choonz.utils.UserSecurity;

@SpringBootTest
public class PlaylistControllerUnitTest {

	@Autowired
	private PlaylistController controller;
	
	@MockBean
	private PlaylistService service;
	
	@MockBean
	private UserSecurity security;
	
	private List<Long> emptyList = new ArrayList<Long>();
	private List<Playlist> playlist;
	private List<PlaylistDTO> playlistDTO;
	
	private Playlist validPlaylist;
	private PlaylistDTO validPlaylistDTO;
	
	@BeforeEach
	public void init() {
		validPlaylist = new Playlist(1, "test", "test", "test", null);
		validPlaylistDTO = new PlaylistDTO(1, "test", "test", "test", emptyList);
		
		playlist = new ArrayList<Playlist>();
		playlistDTO = new ArrayList<PlaylistDTO>();
		
		playlist.add(validPlaylist);
		playlistDTO.add(validPlaylistDTO);
	}
	
	@Test
	public void createPlaylistTest() {
		when(service.create(validPlaylistDTO)).thenReturn(validPlaylistDTO);
		when(security.testKey(Mockito.anyString())).thenReturn(true);
		ResponseEntity<PlaylistDTO> response = new ResponseEntity<PlaylistDTO>(validPlaylistDTO, HttpStatus.CREATED);
		assertThat(response).isEqualTo(controller.create(validPlaylistDTO, "Imahash"));
		verify(service, times(1)).create(validPlaylistDTO);
	}
	
	@Test
	public void createPlaylistUnauthorisedTest() {
		when(service.create(validPlaylistDTO)).thenReturn(validPlaylistDTO);
		when(security.testKey(Mockito.anyString())).thenReturn(false);
		ResponseEntity<PlaylistDTO> response = new ResponseEntity<PlaylistDTO>(HttpStatus.UNAUTHORIZED);
		assertThat(response).isEqualTo(controller.create(validPlaylistDTO, null));
	}
	
	@Test
	public void readPlaylistTest() {
		when(service.read()).thenReturn(playlistDTO);
		ResponseEntity<List<PlaylistDTO>> response = new ResponseEntity<List<PlaylistDTO>>(playlistDTO, HttpStatus.OK);
		assertThat(response).isEqualTo(controller.read());
		verify(service, times(1)).read();
	}

	@Test
	public void readPlaylistIdTest() {
		when(service.read(validPlaylistDTO.getId())).thenReturn(validPlaylistDTO);
		ResponseEntity<PlaylistDTO> response = new ResponseEntity<PlaylistDTO>(validPlaylistDTO, HttpStatus.OK);
		assertThat(response).isEqualTo(controller.read(validPlaylistDTO.getId()));
		verify(service, times(1)).read(validPlaylistDTO.getId());
	}
	
	@Test
	public void readPlaylistNameTest() {
		when(service.read(validPlaylistDTO.getName())).thenReturn(validPlaylistDTO);
		ResponseEntity<PlaylistDTO> response = new ResponseEntity<PlaylistDTO>(validPlaylistDTO, HttpStatus.OK);
		assertThat(response).isEqualTo(controller.getPlaylistByName(validPlaylistDTO.getName()));
		verify(service, times(1)).read(validPlaylistDTO.getName());
	}
	
}
