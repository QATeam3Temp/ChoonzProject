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
import com.qa.choonz.persistence.domain.Artist;
import com.qa.choonz.persistence.domain.Genre;
import com.qa.choonz.persistence.domain.Track;
import com.qa.choonz.persistence.repository.AlbumRepository;
import com.qa.choonz.persistence.repository.ArtistRepository;
import com.qa.choonz.persistence.repository.GenreRepository;
import com.qa.choonz.persistence.repository.TrackRepository;
import com.qa.choonz.rest.dto.AlbumDTO;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

@SpringBootTest
@Transactional
public class AlbumServiceIntegrationTest {

	@Autowired
	private AlbumService service;

	@Autowired
	private AlbumRepository repo;

	@Autowired
	private GenreRepository gRepo;

	@Autowired
	private TrackRepository tRepo;

	@Autowired
	private ArtistRepository aRepo;

	static ExtentReports report = new ExtentReports("Documentation/reports/Album_Service_Integration_Report.html",
			true);
	static ExtentTest test;

	private List<Long> emptyList = new ArrayList<Long>();
	private List<Album> album = new ArrayList<Album>();
	private List<AlbumDTO> albumDTO = new ArrayList<AlbumDTO>();
	private Track validTrack = new Track(1, "test", 1000, "test");
	private Genre validGenre = new Genre(1, "test", "test", album);
	private Artist validArtist = new Artist(1, "test", album);
	private List<Track> validTracks = new ArrayList<Track>();
	private Album validAlbum;
	private AlbumDTO validAlbumDTO;

	@BeforeEach
	public void init() {
		aRepo.deleteAll();
		gRepo.deleteAll();
		repo.deleteAll();
		tRepo.deleteAll();
		validArtist = aRepo.save(validArtist);
		validGenre = gRepo.save(validGenre);
		validAlbum = new Album(1, "test", validTracks, validArtist, validGenre, "test");
		album = new ArrayList<Album>();
		albumDTO = new ArrayList<AlbumDTO>();
		validAlbum = repo.save(validAlbum);
		validTrack.setAlbum(validAlbum);
		validTrack.setPlaylist(null);
		validTrack = tRepo.save(validTrack);
		validAlbum.setTracks(List.of(validTrack));
		// validAlbum = repo.save(validAlbum);
		validAlbumDTO = service.map(validAlbum);
		album.add(validAlbum);
		albumDTO.add(validAlbumDTO);
	}

	@AfterAll
	static void Exit() {
		report.flush();
	}
	
	@Test
	public void createAlbumTest() {
		test = report.startTest("Create album test");
		AlbumDTO newAlbum = new AlbumDTO("running in the 90s", "test");
		AlbumDTO expectedAlbum = new AlbumDTO(validAlbum.getId() + 1, "running in the 90s", emptyList, 0L, 0L, "test");
		assertThat(expectedAlbum).isEqualTo(service.create(newAlbum));
		test.log(LogStatus.PASS, "Ok");
	}

	@Test
	public void readAllAlbumsTest() {
		test = report.startTest("Read albums test");
		List<AlbumDTO> albumInDb = service.read();
		assertThat(albumDTO).isEqualTo(albumInDb);
		test.log(LogStatus.PASS, "Ok");
	}

	@Test
	public void readAlbumIdTest() {
		test = report.startTest("Read album by id test");
		assertThat(validAlbumDTO).isEqualTo(service.read(validAlbum.getId()));
		test.log(LogStatus.PASS, "Ok");
	}

	@Test
	public void readAlbumNameTest() {
		test = report.startTest("Read album by name test");
		assertThat(validAlbumDTO).isEqualTo(service.read(validAlbum.getName()));
		test.log(LogStatus.PASS, "Ok");
	}

	@Test
	public void readAlbumGenreTest() {
		test = report.startTest("Read albums by genre test");
		assertThat(albumDTO).isEqualTo(service.readByGenre(validGenre.getId()));
		test.log(LogStatus.PASS, "Ok");
	}

	@Test
	public void readAlbumArtistTest() {
		test = report.startTest("Read albums by artist test");
		assertThat(albumDTO).isEqualTo(service.readByArtist(validArtist.getId()));
		test.log(LogStatus.PASS, "Ok");
	}

	@Test
	public void updateAlbumTest() {
		test = report.startTest("Update album test");
		AlbumDTO sentAlbum = new AlbumDTO("updated", "updated");
		AlbumDTO responseAlbum = new AlbumDTO(validAlbum.getId(), "updated", emptyList, 0L, validGenre.getId(),
				"updated");
		AlbumDTO updatedAlbum = service.update(sentAlbum, validAlbum.getId());

		assertThat(responseAlbum).isEqualTo(updatedAlbum);
		test.log(LogStatus.PASS, "Ok");
	}

	@Test
	public void deleteAlbumTest() {
		test = report.startTest("Delete album test");
		assertThat(true).isEqualTo(service.delete(validAlbum.getId()));
		test.log(LogStatus.PASS, "Ok");
	}

}
