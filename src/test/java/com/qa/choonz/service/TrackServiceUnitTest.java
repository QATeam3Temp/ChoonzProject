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
import com.qa.choonz.persistence.domain.Playlist;
import com.qa.choonz.persistence.domain.Track;
import com.qa.choonz.persistence.repository.TrackRepository;
import com.qa.choonz.rest.dto.TrackDTO;

@SpringBootTest
public class TrackServiceUnitTest {

	@Autowired
	private TrackService service;
	
	@MockBean
	private TrackRepository repo;
	
	private List<Track> track;
	private List<TrackDTO> trackDTO;
	
	private Album validAlbum;
	private Playlist validPlaylist;
	private Track validTrack;
	private TrackDTO validTrackDTO;
	
	@BeforeEach
	public void init() {
		validAlbum = new Album(1, "test", null, null, null, "test");
		validPlaylist = new Playlist(1, "test", "test", "test", null);
		validTrack = new Track(1, "test", validAlbum, validPlaylist, 1000, "test");
		validTrackDTO = new TrackDTO(1, "test", validAlbum, validPlaylist, 1000, "test");
		
		track = new ArrayList<Track>();
		trackDTO = new ArrayList<TrackDTO>();
		
		track.add(validTrack);
		trackDTO.add(validTrackDTO);
	}
	
	@Test
	public void readAll() {
		when(repo.findAll()).thenReturn(track);
		assertThat(trackDTO).isEqualTo(service.read());
		verify(repo, times(1)).findAll();
	}
	
	@Test
	public void readId() {
		when(repo.findById(Mockito.anyLong())).thenReturn(Optional.of(validTrack));
		assertThat(validTrackDTO).isEqualTo(service.read(validTrackDTO.getId()));
		verify(repo, times(1)).findById(Mockito.anyLong());
	}
	
	@Test
	public void readName() {
		when(repo.getTrackByNameJPQL(validTrackDTO.getName())).thenReturn(validTrack);
		assertThat(validTrackDTO).isEqualTo(service.read(validTrackDTO.getName()));
		verify(repo, times(1)).getTrackByNameJPQL(validTrackDTO.getName());
	}
	
}
