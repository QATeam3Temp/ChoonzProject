package com.qa.choonz.rest.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
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
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

@SpringBootTest
public class PlaylistControllerUnitTest {

	@Autowired
	private PlaylistController controller;

	@MockBean
	private PlaylistService service;

	@MockBean
	private UserSecurity security;

	static ExtentReports report = new ExtentReports("Documentation/reports/Choonz_test_Report.html", false);
	static ExtentTest test;

	private List<Long> emptyList = new ArrayList<Long>();
	private List<Playlist> playlist;
	private List<PlaylistDTO> playlistDTO;

	private Playlist validPlaylist;
	private PlaylistDTO validPlaylistDTO;
	private PlaylistDTO updatedPlaylistDTO;

	@BeforeEach
	void init() {
		validPlaylist = new Playlist(1, "test", "test", "test", null);
		validPlaylistDTO = new PlaylistDTO(1, "test", "test", "test", emptyList);
		updatedPlaylistDTO = new PlaylistDTO(1, "updated", "updated", "updated", emptyList);

		playlist = new ArrayList<Playlist>();
		playlistDTO = new ArrayList<PlaylistDTO>();

		playlist.add(validPlaylist);
		playlistDTO.add(validPlaylistDTO);
	}

	@AfterAll
	static void Exit() {
		report.flush();
	}

	@Test
	void createPlaylistTest() {
		test = report.startTest("Create playlist test - controller unit");
		when(service.create(validPlaylistDTO)).thenReturn(validPlaylistDTO);
		when(security.testKey(Mockito.anyString())).thenReturn(true);
		ResponseEntity<PlaylistDTO> response = new ResponseEntity<PlaylistDTO>(validPlaylistDTO, HttpStatus.CREATED);
		assertThat(response).isEqualTo(controller.create(validPlaylistDTO, "Imahash"));
		verify(service, times(1)).create(validPlaylistDTO);
		test.log(LogStatus.PASS, "Ok");
		report.endTest(test);
	}

	@Test
	void createPlaylistUnauthorisedTest() {
		test = report.startTest("Unauthorised create playlist test - controller unit");
		when(service.create(validPlaylistDTO)).thenReturn(validPlaylistDTO);
		when(security.testKey(Mockito.anyString())).thenReturn(false);
		ResponseEntity<PlaylistDTO> response = new ResponseEntity<PlaylistDTO>(HttpStatus.UNAUTHORIZED);
		assertThat(response).isEqualTo(controller.create(validPlaylistDTO, null));
		test.log(LogStatus.PASS, "Ok");
		report.endTest(test);
	}

	@Test
	void readPlaylistTest() {
		test = report.startTest("Read playlists test - controller unit");
		when(service.read()).thenReturn(playlistDTO);
		ResponseEntity<List<PlaylistDTO>> response = new ResponseEntity<List<PlaylistDTO>>(playlistDTO, HttpStatus.OK);
		assertThat(response).isEqualTo(controller.read());
		verify(service, times(1)).read();
		test.log(LogStatus.PASS, "Ok");
		report.endTest(test);
	}

	@Test
	void readPlaylistIdTest() {
		test = report.startTest("Read playlist by id test - controller unit");
		when(service.read(validPlaylistDTO.getId())).thenReturn(validPlaylistDTO);
		ResponseEntity<PlaylistDTO> response = new ResponseEntity<PlaylistDTO>(validPlaylistDTO, HttpStatus.OK);
		assertThat(response).isEqualTo(controller.read(validPlaylistDTO.getId()));
		verify(service, times(1)).read(validPlaylistDTO.getId());
		test.log(LogStatus.PASS, "Ok");
		report.endTest(test);
	}

	@Test
	void readPlaylistNameTest() {
		test = report.startTest("Read playlist by name test - controller unit");
		when(service.read(validPlaylistDTO.getName())).thenReturn(validPlaylistDTO);
		ResponseEntity<PlaylistDTO> response = new ResponseEntity<PlaylistDTO>(validPlaylistDTO, HttpStatus.OK);
		assertThat(response).isEqualTo(controller.getPlaylistByName(validPlaylistDTO.getName()));
		verify(service, times(1)).read(validPlaylistDTO.getName());
		test.log(LogStatus.PASS, "Ok");
		report.endTest(test);
	}

	@Test
	void updatePlaylistTest() {
		test = report.startTest("Update playlist test - controller unit");
		when(service.update(Mockito.any(PlaylistDTO.class), Mockito.anyLong())).thenReturn(updatedPlaylistDTO);
		when(security.testKey(Mockito.anyString())).thenReturn(true);

		ResponseEntity<PlaylistDTO> response = new ResponseEntity<PlaylistDTO>(updatedPlaylistDTO, HttpStatus.ACCEPTED);
		assertThat(response).isEqualTo(controller.update(updatedPlaylistDTO, validPlaylist.getId(), "imakey"));

		verify(service, times(1)).update(Mockito.any(PlaylistDTO.class), Mockito.anyLong());
		verify(security, times(1)).testKey(Mockito.anyString());
		test.log(LogStatus.PASS, "Ok");
		report.endTest(test);
	}

	@Test
	void deletePlaylistTest() {
		test = report.startTest("Delete playlist test - controller unit");
		when(service.delete(Mockito.anyLong())).thenReturn(true);
		when(security.testKey(Mockito.anyString())).thenReturn(true);

		ResponseEntity<Boolean> response = new ResponseEntity<Boolean>(true, HttpStatus.NO_CONTENT);
		assertThat(response).isEqualTo(controller.delete(validPlaylistDTO.getId(), "imakey"));
		verify(service, times(1)).delete(Mockito.anyLong());
		verify(security, times(1)).testKey(Mockito.anyString());
		test.log(LogStatus.PASS, "Ok");
		report.endTest(test);
	}

}
