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
import org.junit.jupiter.api.extension.ExtendWith;
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
import com.qa.choonz.utils.TestWatch;
import com.qa.choonz.utils.UserSecurity;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.LogStatus;

@SpringBootTest
@ExtendWith(TestWatch.class)
public class TrackControllerUnitTest {

	@Autowired
	private TrackController controller;

	@MockBean
	private TrackService service;

	@MockBean
	private UserSecurity security;

	ExtentReports report = TestWatch.report;

	private List<Track> track;
	private List<TrackDTO> trackDTO;

	private Track validTrack;
	private TrackDTO validTrackDTO;

	private Album validAlbum;
	private Playlist validPlaylist;

	private TrackDTO updatedTrackDTO;

	@BeforeEach
	void init() {
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

	@AfterAll
	static void Exit() {
		TestWatch.report.flush();
	}

	@Test
	void createTrack() {
		TestWatch.test = report.startTest("Create track test - controller unit");
		when(service.create(validTrackDTO)).thenReturn(validTrackDTO);
		when(security.testKey(Mockito.anyString())).thenReturn(true);
		ResponseEntity<TrackDTO> response = new ResponseEntity<TrackDTO>(validTrackDTO, HttpStatus.CREATED);
		assertThat(response).isEqualTo(controller.create(validTrackDTO, "ImaKey"));
		verify(service, times(1)).create(validTrackDTO);
		TestWatch.test.log(LogStatus.PASS, "Ok");
		report.endTest(TestWatch.test);
	}
	
	@Test
	void badCreateTrackRequestTest() {
		test = report.startTest("Create track bad request test - controller unit");
		TrackDTO badTrack = new TrackDTO();
		
		when(service.create(Mockito.any(TrackDTO.class))).thenReturn(badTrack);
		when(security.testKey(Mockito.anyString())).thenReturn(true);
		
		ResponseEntity<TrackDTO> response = new ResponseEntity<TrackDTO>(HttpStatus.BAD_REQUEST);
		assertThat(response).isEqualTo(controller.create(badTrack, "ImaKey"));
		
		test.log(LogStatus.PASS, "Ok");
		report.endTest(test);
	}

	@Test
	void createTrackUnauthorised() {
		TestWatch.test = report.startTest("Unauthorised create track test - controller unit");
		when(service.create(validTrackDTO)).thenReturn(validTrackDTO);
		when(security.testKey(Mockito.anyString())).thenReturn(false);
		ResponseEntity<TrackDTO> response = new ResponseEntity<TrackDTO>(HttpStatus.UNAUTHORIZED);
		assertThat(response).isEqualTo(controller.create(validTrackDTO, null));
		TestWatch.test.log(LogStatus.PASS, "Ok");
		report.endTest(TestWatch.test);
	}

	@Test
	void readTest() {
		TestWatch.test = report.startTest("Read track test - controller unit");
		when(service.read()).thenReturn(trackDTO);
		ResponseEntity<List<TrackDTO>> response = new ResponseEntity<List<TrackDTO>>(trackDTO, HttpStatus.OK);
		assertThat(response).isEqualTo(controller.read());
		verify(service, times(1)).read();
		TestWatch.test.log(LogStatus.PASS, "Ok");
		report.endTest(TestWatch.test);
	}

	@Test
	void readByPlaylistTest() {
		TestWatch.test = report.startTest("Read track by playlist test - controller unit");
		when(service.readByPlaylist(Mockito.anyLong())).thenReturn(trackDTO);
		ResponseEntity<List<TrackDTO>> response = new ResponseEntity<List<TrackDTO>>(trackDTO, HttpStatus.OK);
		assertThat(response).isEqualTo(controller.getTrackByPlaylist(0L));
		verify(service, times(1)).readByPlaylist(Mockito.anyLong());
		TestWatch.test.log(LogStatus.PASS, "Ok");
		report.endTest(TestWatch.test);
	}

	@Test
	void readByAlbumTest() {
		TestWatch.test = report.startTest("Read track by album test - controller unit");
		when(service.readByAlbum(Mockito.anyLong())).thenReturn(trackDTO);
		ResponseEntity<List<TrackDTO>> response = new ResponseEntity<List<TrackDTO>>(trackDTO, HttpStatus.OK);
		assertThat(response).isEqualTo(controller.getTrackByAlbum(0L));
		verify(service, times(1)).readByAlbum(Mockito.anyLong());
		TestWatch.test.log(LogStatus.PASS, "Ok");
		report.endTest(TestWatch.test);
	}

	@Test
	void readIdTest() {
		TestWatch.test = report.startTest("Read track by id test - controller unit");
		when(service.read(validTrackDTO.getId())).thenReturn(validTrackDTO);
		ResponseEntity<TrackDTO> response = new ResponseEntity<TrackDTO>(validTrackDTO, HttpStatus.OK);
		assertThat(response).isEqualTo(controller.read(validTrackDTO.getId()));
		verify(service, times(1)).read(validTrackDTO.getId());
		TestWatch.test.log(LogStatus.PASS, "Ok");
		report.endTest(TestWatch.test);
	}

	@Test
	void readNameTest() {
		TestWatch.test = report.startTest("Read track by name test - controller unit");
		when(service.read(validTrackDTO.getName())).thenReturn(validTrackDTO);
		ResponseEntity<TrackDTO> response = new ResponseEntity<TrackDTO>(validTrackDTO, HttpStatus.OK);
		assertThat(response).isEqualTo(controller.getTrackByName(validTrackDTO.getName()));
		verify(service, times(1)).read(validTrackDTO.getName());
		TestWatch.test.log(LogStatus.PASS, "Ok");
		report.endTest(TestWatch.test);
	}

	@Test
	void updateTrackTest() {
		TestWatch.test = report.startTest("Update track test - controller unit");
		when(service.update(Mockito.any(TrackDTO.class), Mockito.anyLong())).thenReturn(updatedTrackDTO);
		when(security.testKey(Mockito.anyString())).thenReturn(true);

		ResponseEntity<TrackDTO> response = new ResponseEntity<TrackDTO>(updatedTrackDTO, HttpStatus.ACCEPTED);
		assertThat(response).isEqualTo(controller.update(updatedTrackDTO, validTrackDTO.getId(), "ImaKey"));

		verify(service, times(1)).update(Mockito.any(TrackDTO.class), Mockito.anyLong());
		TestWatch.test.log(LogStatus.PASS, "Ok");
		report.endTest(TestWatch.test);
	}

	@Test
	void updateTrackDeletePlaylistTest() {
		TestWatch.test = report.startTest("Update track to delete playlist test - controller unit");
		when(service.setPlaylistToNull(Mockito.anyLong())).thenReturn(updatedTrackDTO);
		when(security.testKey(Mockito.anyString())).thenReturn(true);

		ResponseEntity<TrackDTO> response = new ResponseEntity<TrackDTO>(updatedTrackDTO, HttpStatus.ACCEPTED);
		assertThat(response).isEqualTo(controller.setPlaylistToNull(validTrackDTO.getId(), "ImaKey"));

		verify(service, times(1)).setPlaylistToNull(Mockito.anyLong());
		TestWatch.test.log(LogStatus.PASS, "Ok");
		report.endTest(TestWatch.test);
	}

	@Test
	void updateTrackDeleteAlbumTest() {
		TestWatch.test = report.startTest("Update track to delete album test - controller unit");
		when(service.setAlbumToNull(Mockito.anyLong())).thenReturn(updatedTrackDTO);
		when(security.testKey(Mockito.anyString())).thenReturn(true);

		ResponseEntity<TrackDTO> response = new ResponseEntity<TrackDTO>(updatedTrackDTO, HttpStatus.ACCEPTED);
		assertThat(response).isEqualTo(controller.setAlbumToNull(validTrackDTO.getId(), "ImaKey"));

		verify(service, times(1)).setAlbumToNull(Mockito.anyLong());
		TestWatch.test.log(LogStatus.PASS, "Ok");
		report.endTest(TestWatch.test);
	}

	@Test
	void deleteTrackTest() {
		TestWatch.test = report.startTest("Delete album test - controller unit");
		when(service.delete(Mockito.anyLong())).thenReturn(true);
		when(security.testKey(Mockito.anyString())).thenReturn(true);

		ResponseEntity<Boolean> response = new ResponseEntity<Boolean>(true, HttpStatus.NO_CONTENT);

		assertThat(response).isEqualTo(controller.delete(validTrackDTO.getId(), "ImaKey"));

		verify(service, times(1)).delete(Mockito.anyLong());
		TestWatch.test.log(LogStatus.PASS, "Ok");
		report.endTest(TestWatch.test);
	}

}
