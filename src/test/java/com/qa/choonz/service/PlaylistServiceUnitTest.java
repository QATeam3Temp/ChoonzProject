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

import com.qa.choonz.persistence.domain.Playlist;
import com.qa.choonz.persistence.domain.Track;
import com.qa.choonz.persistence.repository.PlaylistRepository;
import com.qa.choonz.rest.dto.PlaylistDTO;
import com.qa.choonz.rest.dto.TrackDTO;
import com.qa.choonz.utils.mappers.PlaylistMapper;

@SpringBootTest
public class PlaylistServiceUnitTest {
	
	@Autowired
	private PlaylistService service;

	@MockBean
	private PlaylistRepository repo;
	
	@MockBean
	private PlaylistMapper mapper;
	

	private List<Playlist> playlists;
	private List<PlaylistDTO> playlistDTOs;
	
	private Track validTrack = new Track();
	private List<Track> validTracks = List.of(validTrack);
	private Playlist playlist;
	private PlaylistDTO playlistDTO;
	
	@BeforeEach
	public void init() {
		playlist = new Playlist(1L,"Running music","Mostly Eurobeat","A pair of legs",validTracks);
		playlistDTO = new PlaylistDTO(1L,"Running music","Mostly Eurobeat","A pair of legs",List.of(0L));

		playlists = new ArrayList<Playlist>();
		playlistDTOs = new ArrayList<PlaylistDTO>();

		playlists.add(playlist);
		playlistDTOs.add(playlistDTO);
	}
	
	@Test
	public void createPlaylistTest() {
		when(repo.save(Mockito.any(Playlist.class))).thenReturn(playlist);
		when(mapper.MapToDTO(Mockito.any(Playlist.class))).thenReturn(playlistDTO);
		when(mapper.MapFromDTO(Mockito.any(PlaylistDTO.class))).thenReturn(playlist);
		assertThat(playlistDTO).isEqualTo(service.create(playlistDTO));
		verify(repo, times(1)).save(Mockito.any(Playlist.class));
		verify(mapper, times(1)).MapFromDTO(Mockito.any(PlaylistDTO.class));
		verify(mapper, times(1)).MapToDTO(Mockito.any(Playlist.class));
	}
	
	@Test
	public void readPlaylistTest() {
		when(repo.findAll()).thenReturn(playlists);
		when(mapper.MapToDTO(Mockito.any(Playlist.class))).thenReturn(playlistDTO);
		assertThat(playlistDTOs).isEqualTo(service.read());
		verify(repo, times(1)).findAll();
		verify(mapper, times(1)).MapToDTO(Mockito.any(Playlist.class));
	}
	
	@Test
	public void readPlaylistIdTest() {
		when(repo.findById(Mockito.anyLong())).thenReturn(Optional.of(playlist));
		when(mapper.MapToDTO(Mockito.any(Playlist.class))).thenReturn(playlistDTO);
		assertThat(playlistDTO).isEqualTo(service.read(1L));
		verify(repo, times(1)).findById(Mockito.anyLong());
		verify(mapper, times(1)).MapToDTO(Mockito.any(Playlist.class));
	}
	
	@Test
	public void readPlaylistNameTest() {
		when(repo.getPlaylistByNameJPQL(Mockito.anyString())).thenReturn(playlist);
		when(mapper.MapToDTO(Mockito.any(Playlist.class))).thenReturn(playlistDTO);
		assertThat(playlistDTO).isEqualTo(service.read("Running music"));
		verify(repo, times(1)).getPlaylistByNameJPQL(Mockito.anyString());
		verify(mapper, times(1)).MapToDTO(Mockito.any(Playlist.class));
	}
	
	@Test
	public void updatePlaylistTest() {
		Playlist updatedplaylist = new Playlist(1L,"Running","Eurobeat","A pair of legs",validTracks);
		PlaylistDTO updatedplaylistDTO = new PlaylistDTO(1L,"Running","Eurobeat","A pair of legs",List.of(0L));
		when(repo.findById(Mockito.anyLong())).thenReturn(Optional.of(playlist));
		when(mapper.MapFromDTO(Mockito.any(PlaylistDTO.class))).thenReturn(playlist);
		when(repo.save(Mockito.any(Playlist.class))).thenReturn(updatedplaylist);
		when(mapper.MapToDTO(Mockito.any(Playlist.class))).thenReturn(updatedplaylistDTO);
		
		PlaylistDTO testUpdateTrackDTO = service.update(updatedplaylistDTO, playlist.getId());
		
		assertThat(updatedplaylistDTO).isEqualTo(testUpdateTrackDTO);
	}
	
	@Test
	public void deletePlaylistTest() {
		when(repo.existsById(Mockito.anyLong())).thenReturn(false);
		assertThat(true).isEqualTo(service.delete(playlist.getId()));
		verify(repo, times(1)).existsById(Mockito.anyLong());
		verify(repo, times(1)).deleteById(Mockito.anyLong());
	}
}
