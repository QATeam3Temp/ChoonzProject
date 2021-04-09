package com.qa.choonz.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
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
import com.qa.choonz.utils.mappers.PlaylistMapper;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

@SpringBootTest
public class PlaylistServiceUnitTest {

	@Autowired
	private PlaylistService service;

	@MockBean
	private PlaylistRepository repo;

	@MockBean
	private PlaylistMapper mapper;

	static ExtentReports report = new ExtentReports("Documentation/reports/Choonz_test_Report.html", false);
	static ExtentTest test;

	private List<Playlist> playlists;
	private List<PlaylistDTO> playlistDTOs;

	private Track validTrack = new Track();
	private List<Track> validTracks = List.of(validTrack);
	private Playlist playlist;
	private PlaylistDTO playlistDTO;

	@BeforeEach
	void init() {
		playlist = new Playlist(1L, "Running music", "Mostly Eurobeat", "A pair of legs", validTracks);
		playlistDTO = new PlaylistDTO(1L, "Running music", "Mostly Eurobeat", "A pair of legs", List.of(0L));

		playlists = new ArrayList<Playlist>();
		playlistDTOs = new ArrayList<PlaylistDTO>();

		playlists.add(playlist);
		playlistDTOs.add(playlistDTO);
	}

	@AfterAll
	static void Exit() {
		report.flush();
	}

	@Test
	void createPlaylistTest() {
		test = report.startTest("Create playlist test - service unit");
		when(repo.save(Mockito.any(Playlist.class))).thenReturn(playlist);
		when(mapper.MapToDTO(Mockito.any(Playlist.class))).thenReturn(playlistDTO);
		when(mapper.MapFromDTO(Mockito.any(PlaylistDTO.class))).thenReturn(playlist);
		assertThat(playlistDTO).isEqualTo(service.create(playlistDTO));
		verify(repo, times(1)).save(Mockito.any(Playlist.class));
		verify(mapper, times(1)).MapFromDTO(Mockito.any(PlaylistDTO.class));
		verify(mapper, times(1)).MapToDTO(Mockito.any(Playlist.class));
		test.log(LogStatus.PASS, "Ok");
		report.endTest(test);
	}

	@Test
	void readPlaylistTest() {
		test = report.startTest("Read playlists test - service unit");
		when(repo.findAll()).thenReturn(playlists);
		when(mapper.MapToDTO(Mockito.any(Playlist.class))).thenReturn(playlistDTO);
		assertThat(playlistDTOs).isEqualTo(service.read());
		verify(repo, times(1)).findAll();
		verify(mapper, times(1)).MapToDTO(Mockito.any(Playlist.class));
		test.log(LogStatus.PASS, "Ok");
		report.endTest(test);
	}

	@Test
	void readPlaylistIdTest() {
		test = report.startTest("Read playlist by id test - service unit");
		when(repo.findById(Mockito.anyLong())).thenReturn(Optional.of(playlist));
		when(mapper.MapToDTO(Mockito.any(Playlist.class))).thenReturn(playlistDTO);
		assertThat(playlistDTO).isEqualTo(service.read(1L));
		verify(repo, times(1)).findById(Mockito.anyLong());
		verify(mapper, times(1)).MapToDTO(Mockito.any(Playlist.class));
		test.log(LogStatus.PASS, "Ok");
		report.endTest(test);
	}

	@Test
	void readPlaylistNameTest() {
		test = report.startTest("Read playlist by name test - service unit");
		when(repo.getPlaylistByNameJPQL(Mockito.anyString())).thenReturn(playlist);
		when(mapper.MapToDTO(Mockito.any(Playlist.class))).thenReturn(playlistDTO);
		assertThat(playlistDTO).isEqualTo(service.read("Running music"));
		verify(repo, times(1)).getPlaylistByNameJPQL(Mockito.anyString());
		verify(mapper, times(1)).MapToDTO(Mockito.any(Playlist.class));
		test.log(LogStatus.PASS, "Ok");
		report.endTest(test);
	}

	@Test
	void updatePlaylistTest() {
		test = report.startTest("Updated playlist test - service unit");
		Playlist updatedplaylist = new Playlist(1L, "Running", "Eurobeat", "A pair of legs", validTracks);
		PlaylistDTO updatedplaylistDTO = new PlaylistDTO(1L, "Running", "Eurobeat", "A pair of legs", List.of(0L));
		when(repo.findById(Mockito.anyLong())).thenReturn(Optional.of(playlist));
		when(mapper.MapFromDTO(Mockito.any(PlaylistDTO.class))).thenReturn(playlist);
		when(repo.save(Mockito.any(Playlist.class))).thenReturn(updatedplaylist);
		when(mapper.MapToDTO(Mockito.any(Playlist.class))).thenReturn(updatedplaylistDTO);

		PlaylistDTO testUpdateTrackDTO = service.update(updatedplaylistDTO, playlist.getId());

		assertThat(updatedplaylistDTO).isEqualTo(testUpdateTrackDTO);
		test.log(LogStatus.PASS, "Ok");
		report.endTest(test);
	}

	@Test
	void deletePlaylistTest() {
		test = report.startTest("Delete playlist test - service unit");
		when(repo.existsById(Mockito.anyLong())).thenReturn(true).thenReturn(false);
		assertThat(true).isEqualTo(service.delete(playlist.getId()));
		verify(repo, times(2)).existsById(Mockito.anyLong());
		verify(repo, times(1)).deleteById(Mockito.anyLong());
		test.log(LogStatus.PASS, "Ok");
		report.endTest(test);
	}
}
