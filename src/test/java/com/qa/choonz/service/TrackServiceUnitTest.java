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

	static ExtentReports report = new ExtentReports("Documentation/reports/Track_Service_Unit_Report.html", true);
	static ExtentTest test;

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

	@AfterAll
	static void Exit() {
		report.flush();
	}

	@Test
	public void createTest() {
		test = report.startTest("Create track test");
		when(repo.save(Mockito.any(Track.class))).thenReturn(validTrack);
		when(mapper.mapToDTO(Mockito.any(Track.class))).thenReturn(validTrackDTO);
		when(mapper.mapFromDTO(Mockito.any(TrackDTO.class))).thenReturn(validTrack);
		assertThat(validTrackDTO).isEqualTo(service.create(validTrackDTO));
		verify(repo, times(1)).save(Mockito.any(Track.class));
		verify(mapper, times(1)).mapToDTO(Mockito.any(Track.class));
		verify(mapper, times(1)).mapFromDTO(Mockito.any(TrackDTO.class));
		test.log(LogStatus.PASS, "Ok");
	}

	@Test
	public void readAllTest() {
		test = report.startTest("Read tracks test");
		when(repo.findAll()).thenReturn(track);
		when(mapper.mapToDTO(Mockito.any(Track.class))).thenReturn(validTrackDTO);
		assertThat(trackDTO).isEqualTo(service.read());
		verify(repo, times(1)).findAll();
		verify(mapper, times(1)).mapToDTO(Mockito.any(Track.class));
		test.log(LogStatus.PASS, "Ok");
	}

	@Test
	public void readIdTest() {
		test = report.startTest("Read track by id test");
		when(repo.findById(Mockito.anyLong())).thenReturn(Optional.of(validTrack));
		when(mapper.mapToDTO(Mockito.any(Track.class))).thenReturn(validTrackDTO);
		assertThat(validTrackDTO).isEqualTo(service.read(validTrackDTO.getId()));
		verify(repo, times(1)).findById(Mockito.anyLong());
		verify(mapper, times(1)).mapToDTO(Mockito.any(Track.class));
		test.log(LogStatus.PASS, "Ok");
	}

	@Test
	public void readByAlbumTest() {
		test = report.startTest("Read tracks by album test");
		when(repo.getTrackByAlbumSQL(Mockito.anyLong())).thenReturn(List.of(validTrack));
		when(mapper.mapToDTO(Mockito.any(Track.class))).thenReturn(validTrackDTO);
		assertThat(trackDTO).isEqualTo(service.readByAlbum(validTrackDTO.getAlbum()));
		verify(repo, times(1)).getTrackByAlbumSQL(Mockito.anyLong());
		verify(mapper, times(1)).mapToDTO(Mockito.any(Track.class));
		test.log(LogStatus.PASS, "Ok");
	}

	@Test
	public void readByPlaylistTest() {
		test = report.startTest("Read tracks by playlist test");
		when(repo.getTrackByPlaylistSQL(Mockito.anyLong())).thenReturn(List.of(validTrack));
		when(mapper.mapToDTO(Mockito.any(Track.class))).thenReturn(validTrackDTO);
		assertThat(trackDTO).isEqualTo(service.readByPlaylist(validTrackDTO.getPlaylist()));
		verify(repo, times(1)).getTrackByPlaylistSQL(Mockito.anyLong());
		verify(mapper, times(1)).mapToDTO(Mockito.any(Track.class));
		test.log(LogStatus.PASS, "Ok");
	}

	@Test
	public void readNameTest() {
		test = report.startTest("Read track by name test");
		when(repo.getTrackByNameJPQL(validTrackDTO.getName())).thenReturn(validTrack);
		when(mapper.mapToDTO(Mockito.any(Track.class))).thenReturn(validTrackDTO);
		assertThat(validTrackDTO).isEqualTo(service.read(validTrackDTO.getName()));
		verify(repo, times(1)).getTrackByNameJPQL(validTrackDTO.getName());
		verify(mapper, times(1)).mapToDTO(Mockito.any(Track.class));
		test.log(LogStatus.PASS, "Ok");
	}

	@Test
	public void deleteTrackTest() {
		test = report.startTest("Delete track test");
		when(repo.existsById(Mockito.anyLong())).thenReturn(true).thenReturn(false);

		assertThat(true).isEqualTo(service.delete(validTrack.getId()));

		verify(repo, times(2)).existsById(Mockito.anyLong());
		verify(repo, times(1)).deleteById(Mockito.anyLong());
		test.log(LogStatus.PASS, "Ok");
	}

	@Test
	public void deleteAlbumTrackTest() {
		test = report.startTest("Delete track by album test");
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
	}

	@Test
	public void deletePlaylistTrackTest() {
		test = report.startTest("Delete track by playlist test");
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
	}

	@Test
	public void updateTrackTest() {
		test = report.startTest("Updated track test");
		TrackDTO updatedTrackDTO = new TrackDTO("Updated track", 5100, "Updated track");
		Track updatedTrack = new Track(1, "Updated track", validAlbum, validPlaylist, 5100, "Updated track");

		when(repo.findById(Mockito.anyLong())).thenReturn(Optional.of(validTrack));
		when(repo.save(Mockito.any(Track.class))).thenReturn(updatedTrack);
		when(mapper.mapToDTO(Mockito.any(Track.class))).thenReturn(updatedTrackDTO);

		TrackDTO testUpdateTrackDTO = service.update(updatedTrackDTO, validTrack.getId());

		assertThat(updatedTrackDTO).isEqualTo(testUpdateTrackDTO);
		test.log(LogStatus.PASS, "Ok");
	}

}
