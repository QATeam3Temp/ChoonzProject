package com.qa.choonz.rest.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.qa.choonz.persistence.domain.Track;
import com.qa.choonz.rest.dto.TrackDTO;
import com.qa.choonz.service.TrackService;

@SpringBootTest
public class TrackControllerUnitTest {

	@Autowired
	private TrackController controller;
	
	@MockBean
	private TrackService service;
	
	private List<Track> track;
	private List<TrackDTO> trackDTO;
	
	private Track validTrack;
	private TrackDTO validTrackDTO;
	
	@BeforeEach
	public void init() {
		validTrack = new Track(1, "test", null, null, 1000, "test");
		validTrackDTO = new TrackDTO(1, "test", 1000, "test");
		
		track = new ArrayList<Track>();
		trackDTO = new ArrayList<TrackDTO>();
		
		track.add(validTrack);
		trackDTO.add(validTrackDTO);
	}
	
	@Test
	public void readTest() {
		when(service.read()).thenReturn(trackDTO);
		ResponseEntity<List<TrackDTO>> response = new ResponseEntity<List<TrackDTO>>(trackDTO, HttpStatus.OK);
		assertThat(response).isEqualTo(controller.read());
		verify(service, times(1)).read();
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
	
}
