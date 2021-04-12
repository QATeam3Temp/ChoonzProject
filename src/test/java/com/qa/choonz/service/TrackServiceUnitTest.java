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

import com.qa.choonz.persistence.domain.Album;
import com.qa.choonz.persistence.domain.Playlist;
import com.qa.choonz.persistence.domain.Track;
import com.qa.choonz.persistence.repository.TrackRepository;
import com.qa.choonz.rest.dto.TrackDTO;
import com.qa.choonz.utils.mappers.TrackMapper;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

@SpringBootTest
public class TrackServiceUnitTest {

	@Autowired
	private TrackService service;

	@MockBean
	private TrackRepository repo;

	@MockBean
	private TrackMapper mapper;

	static ExtentReports report = new ExtentReports("Documentation/reports/Choonz_test_Report.html", false);
	static ExtentTest test;

	private List<Track> track;
	private List<TrackDTO> trackDTO;

	private Album validAlbum;
	private Playlist validPlaylist;
	private Track validTrack;
	private TrackDTO validTrackDTO;

	@BeforeEach
	void init() {
		validAlbum = new Album(1, "test", null, null, null, "test");
		validPlaylist = new Playlist(1, "test", "test", "test", null);
		validTrack = new Track(1, "test", validAlbum, validPlaylist, 1000, "test");
		validTrackDTO = new TrackDTO(1, "test", validAlbum, validPlaylist, 1000, "test");

		track = new ArrayList<Track>();
		trackDTO = new ArrayList<TrackDTO>();

		track.add(validTrack);
		trackDTO.add(validTrackDTO);
	}

	@AfterAll
	static void Exit() {
		report.flush();
	}

	@Test
	void createTest() {
		test = report.startTest("Create track test - service unit");
		when(repo.save(Mockito.any(Track.class))).thenReturn(validTrack);
		when(mapper.mapToDTO(Mockito.any(Track.class))).thenReturn(validTrackDTO);
		when(mapper.mapFromDTO(Mockito.any(TrackDTO.class))).thenReturn(validTrack);
		assertThat(validTrackDTO).isEqualTo(service.create(validTrackDTO));
		verify(repo, times(1)).save(Mockito.any(Track.class));
		verify(mapper, times(1)).mapToDTO(Mockito.any(Track.class));
		verify(mapper, times(1)).mapFromDTO(Mockito.any(TrackDTO.class));
		test.log(LogStatus.PASS, "Ok");
		report.endTest(test);
	}

	@Test
	void readAllTest() {
		test = report.startTest("Read tracks test - service unit");
		when(repo.findAll()).thenReturn(track);
		when(mapper.mapToDTO(Mockito.any(Track.class))).thenReturn(validTrackDTO);
		assertThat(trackDTO).isEqualTo(service.read());
		verify(repo, times(1)).findAll();
		verify(mapper, times(1)).mapToDTO(Mockito.any(Track.class));
		test.log(LogStatus.PASS, "Ok");
		report.endTest(test);
	}

	@Test
	void readIdTest() {
		test = report.startTest("Read track by id test - service unit");
		when(repo.findById(Mockito.anyLong())).thenReturn(Optional.of(validTrack));
		when(mapper.mapToDTO(Mockito.any(Track.class))).thenReturn(validTrackDTO);
		assertThat(validTrackDTO).isEqualTo(service.read(validTrackDTO.getId()));
		verify(repo, times(1)).findById(Mockito.anyLong());
		verify(mapper, times(1)).mapToDTO(Mockito.any(Track.class));
		test.log(LogStatus.PASS, "Ok");
		report.endTest(test);
	}

	@Test
	void readByAlbumTest() {
		test = report.startTest("Read tracks by album test - service unit");
		when(repo.getTrackByAlbumSQL(Mockito.anyLong())).thenReturn(List.of(validTrack));
		when(mapper.mapToDTO(Mockito.any(Track.class))).thenReturn(validTrackDTO);
		assertThat(trackDTO).isEqualTo(service.readByAlbum(validTrackDTO.getAlbum()));
		verify(repo, times(1)).getTrackByAlbumSQL(Mockito.anyLong());
		verify(mapper, times(1)).mapToDTO(Mockito.any(Track.class));
		test.log(LogStatus.PASS, "Ok");
		report.endTest(test);
	}

	@Test
	void readByPlaylistTest() {
		test = report.startTest("Read tracks by playlist test - service unit");
		when(repo.getTrackByPlaylistSQL(Mockito.anyLong())).thenReturn(List.of(validTrack));
		when(mapper.mapToDTO(Mockito.any(Track.class))).thenReturn(validTrackDTO);
		assertThat(trackDTO).isEqualTo(service.readByPlaylist(validTrackDTO.getPlaylist()));
		verify(repo, times(1)).getTrackByPlaylistSQL(Mockito.anyLong());
		verify(mapper, times(1)).mapToDTO(Mockito.any(Track.class));
		test.log(LogStatus.PASS, "Ok");
		report.endTest(test);
	}

	@Test
	void readNameTest() {
		test = report.startTest("Read track by name test - service unit");
		when(repo.getTrackByNameJPQL(validTrackDTO.getName())).thenReturn(validTrack);
		when(mapper.mapToDTO(Mockito.any(Track.class))).thenReturn(validTrackDTO);
		assertThat(validTrackDTO).isEqualTo(service.read(validTrackDTO.getName()));
		verify(repo, times(1)).getTrackByNameJPQL(validTrackDTO.getName());
		verify(mapper, times(1)).mapToDTO(Mockito.any(Track.class));
		test.log(LogStatus.PASS, "Ok");
		report.endTest(test);
	}

	@Test
	void deleteTrackTest() {
		test = report.startTest("Delete track test - service unit");
		when(repo.existsById(Mockito.anyLong())).thenReturn(true).thenReturn(false);

		assertThat(true).isEqualTo(service.delete(validTrack.getId()));

		verify(repo, times(2)).existsById(Mockito.anyLong());
		verify(repo, times(1)).deleteById(Mockito.anyLong());
		test.log(LogStatus.PASS, "Ok");
		report.endTest(test);
	}

	@Test
	void deleteAlbumTrackTest() {
		test = report.startTest("Delete track by album test - service unit");
		TrackDTO noAlbumTrackDTO = validTrackDTO;
		noAlbumTrackDTO.setAlbum(0L);
		when(repo.findById(Mockito.anyLong())).thenReturn(Optional.of(validTrack));
		when(repo.save(Mockito.any(Track.class))).thenReturn(validTrack);
		when(mapper.mapToDTO(Mockito.any(Track.class))).thenReturn(noAlbumTrackDTO);
		assertThat(noAlbumTrackDTO).isEqualTo(service.setAlbumToNull(validTrack.getId()));

		verify(repo, times(1)).findById(Mockito.anyLong());
		verify(repo, times(1)).save(Mockito.any(Track.class));
		verify(mapper, times(1)).mapToDTO(Mockito.any(Track.class));
		test.log(LogStatus.PASS, "Ok");
		report.endTest(test);
	}

	@Test
	void deletePlaylistTrackTest() {
		test = report.startTest("Delete track by playlist test - service unit");
		TrackDTO noPlaylistTrackDTO = validTrackDTO;
		noPlaylistTrackDTO.setPlaylist(0L);
		when(repo.findById(Mockito.anyLong())).thenReturn(Optional.of(validTrack));
		when(repo.save(Mockito.any(Track.class))).thenReturn(validTrack);
		when(mapper.mapToDTO(Mockito.any(Track.class))).thenReturn(noPlaylistTrackDTO);
		assertThat(noPlaylistTrackDTO).isEqualTo(service.setPlaylistToNull(validTrack.getId()));

		verify(repo, times(1)).findById(Mockito.anyLong());
		verify(repo, times(1)).save(Mockito.any(Track.class));
		verify(mapper, times(1)).mapToDTO(Mockito.any(Track.class));
		test.log(LogStatus.PASS, "Ok");
		report.endTest(test);
	}

	@Test
	void updateTrackTest() {
		test = report.startTest("Updated track test - service unit");
		TrackDTO updatedTrackDTO = new TrackDTO("Updated track", 5100, "Updated track");
		Track updatedTrack = new Track(1, "Updated track", validAlbum, validPlaylist, 5100, "Updated track");

		when(repo.findById(Mockito.anyLong())).thenReturn(Optional.of(validTrack));
		when(repo.save(Mockito.any(Track.class))).thenReturn(updatedTrack);
		when(mapper.mapToDTO(Mockito.any(Track.class))).thenReturn(updatedTrackDTO);

		TrackDTO testUpdateTrackDTO = service.update(updatedTrackDTO, validTrack.getId());

		assertThat(updatedTrackDTO).isEqualTo(testUpdateTrackDTO);
		test.log(LogStatus.PASS, "Ok");
		report.endTest(test);
	}

}
