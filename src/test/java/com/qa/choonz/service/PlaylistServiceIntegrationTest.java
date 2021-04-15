package com.qa.choonz.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.qa.choonz.persistence.domain.Album;
import com.qa.choonz.persistence.domain.Playlist;
import com.qa.choonz.persistence.domain.Track;
import com.qa.choonz.persistence.repository.PlaylistRepository;
import com.qa.choonz.persistence.repository.TrackRepository;
import com.qa.choonz.rest.dto.PlaylistDTO;
import com.qa.choonz.utils.TestWatch;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.LogStatus;

@SpringBootTest
@ExtendWith(TestWatch.class)
public class PlaylistServiceIntegrationTest {

	@Autowired
	private PlaylistService service;

	@Autowired
	private PlaylistRepository repo;

	@Autowired
	private TrackRepository tRepo;

	ExtentReports report = TestWatch.report;

	private List<Long> emptyList = new ArrayList<Long>();
	private List<Playlist> playlist = new ArrayList<Playlist>();
	private List<PlaylistDTO> playlistDTO = new ArrayList<PlaylistDTO>();
	private Album validAlbum;
	private Playlist validPlaylist;
	private PlaylistDTO validPlaylistDTO;
	private Track validTrack = new Track(1, "test", validAlbum, validPlaylist, 1000, "test");
	private List<Track> validTracks = new ArrayList<Track>();


	@BeforeEach
	void init() {
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

	@AfterAll
	static void Exit() {
		TestWatch.report.flush();
	}

	@Test
	void createPlaylistTest() {
		TestWatch.test = report.startTest("Create playlist test - service integration");
		PlaylistDTO newPlaylist = new PlaylistDTO("test2", "test2", "test2");
		PlaylistDTO expectedPlaylist = new PlaylistDTO(validPlaylist.getId() + 1, "test2", "test2", "test2", emptyList);
		System.out.println(expectedPlaylist);
		assertThat(expectedPlaylist).isEqualTo(service.create(newPlaylist));
		TestWatch.test.log(LogStatus.PASS, "Ok");
		report.endTest(TestWatch.test);
	}

	@Test
	void readAllPlaylistsTest() {
		TestWatch.test = report.startTest("Read playlists test - service integration");
		List<PlaylistDTO> playlistInDb = service.read();
		assertThat(playlistDTO).isEqualTo(playlistInDb);
		TestWatch.test.log(LogStatus.PASS, "Ok");
		report.endTest(TestWatch.test);
	}

	@Test
	void readPlaylistIdTest() {
		TestWatch.test = report.startTest("Read playlist by id test - service integration");
		assertThat(validPlaylistDTO).isEqualTo(service.read(validPlaylist.getId()));
		TestWatch.test.log(LogStatus.PASS, "Ok");
		report.endTest(TestWatch.test);
	}

	@Test
	void readPlaylistNameTest() {
		TestWatch.test = report.startTest("Read playlist by name test - service integration");
		assertThat(validPlaylistDTO).isEqualTo(service.read(validPlaylist.getId()));
		TestWatch.test.log(LogStatus.PASS, "Ok");
		report.endTest(TestWatch.test);
	}

	@Test
	void updatePlaylistTest() {
		TestWatch.test = report.startTest("Updated playlist test - service integration");
		PlaylistDTO sentPlaylist = new PlaylistDTO("updated", "updated", "updated");
		PlaylistDTO responsePlaylist = new PlaylistDTO(validPlaylist.getId(), "updated", "updated", "updated",
				emptyList);
		PlaylistDTO updatedPlaylist = service.update(sentPlaylist, validPlaylist.getId());

		assertThat(responsePlaylist).isEqualTo(updatedPlaylist);
		TestWatch.test.log(LogStatus.PASS, "Ok");
		report.endTest(TestWatch.test);
	}

	@Test
	void deletePlaylistTest() {
		TestWatch.test = report.startTest("Delete playlist test - service integration");
		assertThat(true).isEqualTo(service.delete(validPlaylist.getId()));
		TestWatch.test.log(LogStatus.PASS, "Ok");
		report.endTest(TestWatch.test);
	}

}
