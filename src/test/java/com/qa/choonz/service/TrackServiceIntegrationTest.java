package com.qa.choonz.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.qa.choonz.persistence.domain.Track;
import com.qa.choonz.persistence.repository.TrackRepository;
import com.qa.choonz.rest.dto.TrackDTO;

@SpringBootTest
public class TrackServiceIntegrationTest {
	
	@Autowired
	private TrackService service;
	
	@Autowired
	private TrackRepository repo;
	
	private List<Track> track;
	private List<TrackDTO> trackDTO;
	
	private Track validTrack;
	private TrackDTO validTrackDTO;
	
	@BeforeEach
	public void init() {
		validTrack = new Track(1, "test", null, null, 1000, "test");		
		track = new ArrayList<Track>();
		trackDTO = new ArrayList<TrackDTO>();		
		repo.deleteAll();
		validTrack = repo.save(validTrack);
		validTrackDTO = service.mapToDTO(validTrack);
		track.add(validTrack);
		trackDTO.add(validTrackDTO);
	}
	
	@Test
	public void readAll() {
		List<TrackDTO> trackInDb = service.read();
		assertThat(trackDTO).isEqualTo(trackInDb);
	}
	
	@Test
	public void readId() {
		assertThat(validTrackDTO).isEqualTo(service.read(validTrack.getId()));
	}
	
	@Test
	public void readName() {
		assertThat(validTrackDTO).isEqualTo(service.read(validTrack.getName()));
	}

}