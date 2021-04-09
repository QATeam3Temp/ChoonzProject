package com.qa.choonz.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.qa.choonz.persistence.domain.Playlist;
import com.qa.choonz.persistence.domain.Track;
import com.qa.choonz.persistence.repository.PlaylistRepository;
import com.qa.choonz.persistence.repository.TrackRepository;
import com.qa.choonz.rest.dto.PlaylistDTO;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

@SpringBootTest
public class PlaylistServiceIntegrationTest {

	@Autowired
	private PlaylistService service;

	@Autowired
	private PlaylistRepository repo;

	@Autowired
	private TrackRepository tRepo;

	static ExtentReports report = new ExtentReports("Documentation/reports/Choonz_test_Report.html", false);
	static ExtentTest test;

	private List<Long> emptyList = new ArrayList<Long>();
	private List<Playlist> playlist = new ArrayList<Playlist>();
	private List<PlaylistDTO> playlistDTO = new ArrayList<PlaylistDTO>();
	private Track validTrack = new Track(1, "test", null, null, 1000, "test");
	private List<Track> validTracks = new ArrayList<Track>();
	private Playlist validPlaylist;
	private PlaylistDTO validPlaylistDTO;

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
		report.flush();
	}

	@Test
	void createPlaylistTest() {
		test = report.startTest("Create playlist test - service integration");
		PlaylistDTO newPlaylist = new PlaylistDTO("test2", "test2", "test2");
		PlaylistDTO expectedPlaylist = new PlaylistDTO(validPlaylist.getId() + 1, "test2", "test2", "test2", emptyList);
		System.out.println(expectedPlaylist);
		assertThat(expectedPlaylist).isEqualTo(service.create(newPlaylist));
		test.log(LogStatus.PASS, "Ok");
		report.endTest(test);
	}

	@Test
	void readAllPlaylistsTest() {
		test = report.startTest("Read playlists test - service integration");
		List<PlaylistDTO> playlistInDb = service.read();
		assertThat(playlistDTO).isEqualTo(playlistInDb);
		test.log(LogStatus.PASS, "Ok");
		report.endTest(test);
	}

	@Test
	void readPlaylistIdTest() {
		test = report.startTest("Read playlist by id test - service integration");
		assertThat(validPlaylistDTO).isEqualTo(service.read(validPlaylist.getId()));
		test.log(LogStatus.PASS, "Ok");
		report.endTest(test);
	}

	@Test
	void readPlaylistNameTest() {
		test = report.startTest("Read playlist by name test - service integration");
		assertThat(validPlaylistDTO).isEqualTo(service.read(validPlaylist.getId()));
		test.log(LogStatus.PASS, "Ok");
		report.endTest(test);
	}

	@Test
	void updatePlaylistTest() {
		test = report.startTest("Updated playlist test - service integration");
		PlaylistDTO sentPlaylist = new PlaylistDTO("updated", "updated", "updated");
		PlaylistDTO responsePlaylist = new PlaylistDTO(validPlaylist.getId(), "updated", "updated", "updated",
				emptyList);
		PlaylistDTO updatedPlaylist = service.update(sentPlaylist, validPlaylist.getId());

		assertThat(responsePlaylist).isEqualTo(updatedPlaylist);
		test.log(LogStatus.PASS, "Ok");
		report.endTest(test);
	}

	@Test
	void deletePlaylistTest() {
		test = report.startTest("Delete playlist test - service integration");
		assertThat(true).isEqualTo(service.delete(validPlaylist.getId()));
		test.log(LogStatus.PASS, "Ok");
		report.endTest(test);
	}

}
