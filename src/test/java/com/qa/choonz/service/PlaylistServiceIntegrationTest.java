package com.qa.choonz.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.qa.choonz.persistence.domain.Playlist;
import com.qa.choonz.persistence.domain.Track;
import com.qa.choonz.persistence.repository.PlaylistRepository;
import com.qa.choonz.persistence.repository.TrackRepository;
import com.qa.choonz.rest.dto.PlaylistDTO;

@SpringBootTest
public class PlaylistServiceIntegrationTest {

	@Autowired
	private PlaylistService service;

	@Autowired
	private PlaylistRepository repo;

	@Autowired
	private TrackRepository tRepo;

	private List<Long> emptyList = new ArrayList<Long>();
	private List<Playlist> playlist = new ArrayList<Playlist>();
	private List<PlaylistDTO> playlistDTO = new ArrayList<PlaylistDTO>();
	private Track validTrack = new Track(1, "test", null, null, 1000, "test");
	private List<Track> validTracks = new ArrayList<Track>();
	private Playlist validPlaylist;
	private PlaylistDTO validPlaylistDTO;

	@BeforeEach
	public void init() {
		repo.deleteAll();
		validTrack = tRepo.save(validTrack);
		validPlaylist = new Playlist(1, "test", "test", "test", validTracks);
		playlist = new ArrayList<Playlist>();
		playlistDTO = new ArrayList<PlaylistDTO>();
		validPlaylist = repo.save(validPlaylist);
		validPlaylistDTO = service.map(validPlaylist);
		playlist.add(validPlaylist);
		playlistDTO.add(validPlaylistDTO);
	}

	@Test
	public void createPlaylistTest() {
		PlaylistDTO newPlaylist = new PlaylistDTO("test2", "test2", "test2");
		PlaylistDTO expectedPlaylist = new PlaylistDTO(validPlaylist.getId() + 1, "test2", "test2", "test2", emptyList);
		System.out.println(expectedPlaylist);
		assertThat(expectedPlaylist).isEqualTo(service.create(newPlaylist));
	}

	@Test
	public void readAllPlaylistsTest() {
		List<PlaylistDTO> playlistInDb = service.read();
		assertThat(playlistDTO).isEqualTo(playlistInDb);
	}
	
	@Test
	public void readPlaylistIdTest() {
		assertThat(validPlaylistDTO).isEqualTo(service.read(validPlaylist.getId()));
	}
	
	@Test
	public void readPlaylistNameTest() {
		assertThat(validPlaylistDTO).isEqualTo(service.read(validPlaylist.getId()));
	}
	
}
