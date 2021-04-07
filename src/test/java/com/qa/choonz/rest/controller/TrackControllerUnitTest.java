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
import com.qa.choonz.persistence.domain.Playlist;
import com.qa.choonz.persistence.domain.Track;
import com.qa.choonz.rest.dto.TrackDTO;
import com.qa.choonz.service.TrackService;
import com.qa.choonz.utils.UserSecurity;

@SpringBootTest
public class TrackControllerUnitTest {

	@Autowired
	private TrackController controller;
	
	@MockBean
	private TrackService service;
	
	@MockBean
	private UserSecurity security;
	
	private List<Track> track;
	private List<TrackDTO> trackDTO;
	
	private Track validTrack;
	private TrackDTO validTrackDTO;


	private Album validAlbum;
	private Playlist validPlaylist;

	private TrackDTO updatedTrackDTO;

	@BeforeEach
	public void init() {
		validAlbum = new Album(1, "test", null, null, null, "test");
		validPlaylist = new Playlist(1, "test", "test", "test", null);
		validTrack = new Track(1, "test", validAlbum, validPlaylist, 1000, "test");
		validTrackDTO = new TrackDTO(1, "test", 1000, "test");
		updatedTrackDTO = new TrackDTO(1, "update test", 4000, "test");
		
		track = new ArrayList<Track>();
		trackDTO = new ArrayList<TrackDTO>();
		
		track.add(validTrack);
		trackDTO.add(validTrackDTO);
	}
	
	@Test
	public void createTest() {
		when(service.create(validTrackDTO)).thenReturn(validTrackDTO);
		when(security.testKey(Mockito.anyString())).thenReturn(true);
		ResponseEntity<TrackDTO> response = new ResponseEntity<TrackDTO>(validTrackDTO, HttpStatus.CREATED);
		assertThat(response).isEqualTo(controller.create(validTrackDTO, "ImaKey"));
		verify(service, times(1)).create(validTrackDTO);
	}
	
	@Test
	public void readTest() {
		when(service.read()).thenReturn(trackDTO);
		ResponseEntity<List<TrackDTO>> response = new ResponseEntity<List<TrackDTO>>(trackDTO, HttpStatus.OK);
		assertThat(response).isEqualTo(controller.read());
		verify(service, times(1)).read();
	}
	
	@Test
	public void readByPlaylistTest() {
		when(service.readByPlaylist(Mockito.anyLong())).thenReturn(trackDTO);
		ResponseEntity<List<TrackDTO>> response = new ResponseEntity<List<TrackDTO>>(trackDTO, HttpStatus.OK);
		assertThat(response).isEqualTo(controller.getTrackByPlaylist(0L));
		verify(service, times(1)).readByPlaylist(Mockito.anyLong());
	}
	
	@Test
	public void readByAlbumTest() {
		when(service.readByAlbum(Mockito.anyLong())).thenReturn(trackDTO);
		ResponseEntity<List<TrackDTO>> response = new ResponseEntity<List<TrackDTO>>(trackDTO, HttpStatus.OK);
		assertThat(response).isEqualTo(controller.getTrackByAlbum(0L));
		verify(service, times(1)).readByAlbum(Mockito.anyLong());
	}
	
	@Test
	public void readIdTest() {
		when(service.read(validTrackDTO.getId())).thenReturn(validTrackDTO);
		ResponseEntity<TrackDTO> response = new ResponseEntity<TrackDTO>(validTrackDTO, HttpStatus.OK);
		assertThat(response).isEqualTo(controller.read(validTrackDTO.getId()));
		verify(service, times(1)).read(validTrackDTO.getId());
	}
	
	@Test
	public void readNameTest() {
		when(service.read(validTrackDTO.getName())).thenReturn(validTrackDTO);
		ResponseEntity<TrackDTO> response = new ResponseEntity<TrackDTO>(validTrackDTO, HttpStatus.OK);
		assertThat(response).isEqualTo(controller.getTrackByName(validTrackDTO.getName()));
		verify(service, times(1)).read(validTrackDTO.getName());
	}
	
	@Test
	public void updateTrackTest() {
		when(service.update(Mockito.any(TrackDTO.class), Mockito.anyLong())).thenReturn(updatedTrackDTO);
		when(security.testKey(Mockito.anyString())).thenReturn(true);
		
		ResponseEntity<TrackDTO> response = new ResponseEntity<TrackDTO>(updatedTrackDTO, HttpStatus.ACCEPTED);
		assertThat(response).isEqualTo(controller.update(updatedTrackDTO, validTrackDTO.getId(), "ImaKey"));
		
		verify(service, times(1)).update(Mockito.any(TrackDTO.class), Mockito.anyLong());
	}
	
	@Test
	public void setPlaylistToNullTest() {
		when(service.setPlaylistToNull(Mockito.anyLong())).thenReturn(validTrackDTO);
		when(security.testKey(Mockito.anyString())).thenReturn(true);
		
		ResponseEntity<TrackDTO> response = new ResponseEntity<TrackDTO>(validTrackDTO, HttpStatus.ACCEPTED);
		assertThat(response).isEqualTo(controller.setPlaylistToNull(validTrack.getId(), "Imakey"));
		
		verify(service, times(1)).setPlaylistToNull(Mockito.anyLong());
	}
	
	@Test
	public void deleteTrackTest() {
		when(service.delete(Mockito.anyLong())).thenReturn(true);
		when(security.testKey(Mockito.anyString())).thenReturn(true);
		
		ResponseEntity<Boolean> response = new ResponseEntity<Boolean>(true, HttpStatus.NO_CONTENT);
		
		assertThat(response).isEqualTo(controller.delete(validTrackDTO.getId(), "ImaKey"));
		
		verify(service, times(1)).delete(Mockito.anyLong());
		
	}
	
}
