package com.qa.choonz.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.qa.choonz.persistence.domain.Album;
import com.qa.choonz.persistence.domain.Playlist;
import com.qa.choonz.persistence.domain.Track;
import com.qa.choonz.persistence.repository.AlbumRepository;
import com.qa.choonz.persistence.repository.PlaylistRepository;
import com.qa.choonz.persistence.repository.TrackRepository;
import com.qa.choonz.rest.dto.TrackDTO;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

@SpringBootTest
@Transactional
public class TrackServiceIntegrationTest {

	@Autowired
	private TrackService service;

	@Autowired
	private TrackRepository repo;

	@Autowired
	private AlbumRepository aRepo;

	@Autowired
	private PlaylistRepository pRepo;

	static ExtentReports report = new ExtentReports("Documentation/reports/Track_Service_Integration_Report.html",
			true);
	static ExtentTest test;

	private List<Track> track;
	private List<TrackDTO> trackDTO;

	private Track validTrack;
	private TrackDTO validTrackDTO;
	private Album validAlbum = new Album(1, "Greatest Hits", track, null, null, "A large 2");
	private Playlist validPlaylist = new Playlist(1, "Running songs", "Primarily eurobeat", "A pair of legs moving",
			track);

	@BeforeEach
	public void init() {
		aRepo.deleteAll();
		pRepo.deleteAll();
		validAlbum = aRepo.save(validAlbum);
		validPlaylist = pRepo.save(validPlaylist);
		validTrack = new Track(1, "test", validAlbum, validPlaylist, 1000, "test");
		track = new ArrayList<Track>();
		trackDTO = new ArrayList<TrackDTO>();
		repo.deleteAll();
		validTrack = repo.save(validTrack);
		validTrackDTO = service.map(validTrack);
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
		TrackDTO newTrack = new TrackDTO("test2", 1000, "test2");
		TrackDTO expectedTrack = new TrackDTO(validTrack.getId() + 1, "test2", 0L, 0L, 1000, "test2");
		assertThat(expectedTrack).isEqualTo(service.create(newTrack));
		test.log(LogStatus.PASS, "Ok");
	}

	@Test
	public void readAllTest() {
		test = report.startTest("Read tracks test");
		List<TrackDTO> trackInDb = service.read();
		assertThat(trackDTO).isEqualTo(trackInDb);
		test.log(LogStatus.PASS, "Ok");
	}

	@Test
	public void readIdTest() {
		test = report.startTest("Read track by id test");
		assertThat(validTrackDTO).isEqualTo(service.read(validTrack.getId()));
		test.log(LogStatus.PASS, "Ok");
	}

	@Test
	public void readNameTest() {
		test = report.startTest("Read track by name test");
		assertThat(validTrackDTO).isEqualTo(service.read(validTrack.getName()));
		test.log(LogStatus.PASS, "Ok");
	}

	@Test
	public void readByAlbumTest() {
		test = report.startTest("Read tracks by album test");
		assertThat(trackDTO).isEqualTo(service.readByAlbum(validTrackDTO.getAlbum()));
		test.log(LogStatus.PASS, "Ok");
	}

	@Test
	public void readByPlaylistTest() {
		test = report.startTest("Read tracks by playlist test");
		assertThat(trackDTO).isEqualTo(service.readByPlaylist(validTrackDTO.getPlaylist()));
		test.log(LogStatus.PASS, "Ok");
	}

	@Test
	public void deleteTrackTest() {
		test = report.startTest("Delete track test");
		assertThat(true).isEqualTo(service.delete(validTrack.getId()));
		test.log(LogStatus.PASS, "Ok");
	}

	@Test
	public void deleteAlbumTrackTest() {
		test = report.startTest("Delete track by album test");
		TrackDTO noAlbumTrackDTO = validTrackDTO;
		noAlbumTrackDTO.setAlbum(0L);
		assertThat(noAlbumTrackDTO).isEqualTo(service.setAlbumToNull(validTrack.getId()));
		test.log(LogStatus.PASS, "Ok");
	}

	@Test
	public void deletePlaylistTrackTest() {
		test = report.startTest("Delete track by playlist test");
		TrackDTO noPlaylistTrackDTO = validTrackDTO;
		noPlaylistTrackDTO.setPlaylist(0L);
		assertThat(noPlaylistTrackDTO).isEqualTo(service.setPlaylistToNull(validTrack.getId()));
		test.log(LogStatus.PASS, "Ok");
	}

	@Test
	public void updateTrackTest() {
		test = report.startTest("Updated track test");
		TrackDTO sentTrack = new TrackDTO("updateTest", 5000, "heheheh");
		TrackDTO responseTrack = new TrackDTO(validTrack.getId(), "updateTest", validAlbum.getId(),
				validPlaylist.getId(), 5000, "heheheh");
		TrackDTO updatedTrack = service.update(sentTrack, validTrack.getId());

		assertThat(responseTrack).isEqualTo(updatedTrack);
		test.log(LogStatus.PASS, "Ok");
	}

}
