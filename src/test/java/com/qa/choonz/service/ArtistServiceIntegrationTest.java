package com.qa.choonz.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.qa.choonz.persistence.domain.Album;
import com.qa.choonz.persistence.domain.Artist;
import com.qa.choonz.persistence.repository.AlbumRepository;
import com.qa.choonz.persistence.repository.ArtistRepository;
import com.qa.choonz.rest.dto.ArtistDTO;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

@SpringBootTest
@Transactional
public class ArtistServiceIntegrationTest {

	@Autowired
	private AlbumRepository aRepo;

	@Autowired
	private ArtistRepository repo;

	@Autowired
	private ArtistService service;

	static ExtentReports report = new ExtentReports("Documentation/reports/Choonz_test_Report.html", false);
	static ExtentTest test;

	private List<Long> emptyList = new ArrayList<Long>();
	private List<Artist> artist = new ArrayList<Artist>();
	private List<ArtistDTO> artistDTO = new ArrayList<ArtistDTO>();
	private Album validAlbum = new Album(1, "test", null, null, null, "test");
	private List<Album> validAlbums = new ArrayList<Album>();
	private Artist validArtist;
	private ArtistDTO validArtistDTO;

	@BeforeEach
	void init() {
		aRepo.deleteAll();
		repo.deleteAll();
		validAlbum = aRepo.save(validAlbum);
		validArtist = new Artist(1, "test", validAlbums);
		artist = new ArrayList<Artist>();
		artistDTO = new ArrayList<ArtistDTO>();
		validArtist = repo.save(validArtist);
		validArtistDTO = service.map(validArtist);
		artist.add(validArtist);
		artistDTO.add(validArtistDTO);
	}

	@AfterAll
	static void Exit() {
		report.flush();
	}

	@Test
	void createArtistTest() {
		test = report.startTest("Create artist test - service integration");
		ArtistDTO newArtist = new ArtistDTO("test2");
		ArtistDTO expectedArtist = new ArtistDTO(validArtist.getId() + 1, "test2", emptyList);
		assertThat(expectedArtist).isEqualTo(service.create(newArtist));
		test.log(LogStatus.PASS, "Ok");
		report.endTest(test);
	}

	@Test
	void readAllArtistsTest() {
		test = report.startTest("Read artists test - service integration");
		List<ArtistDTO> artistInDb = service.read();
		assertThat(artistDTO).isEqualTo(artistInDb);
		test.log(LogStatus.PASS, "Ok");
		report.endTest(test);
	}

	@Test
	void readArtistIdTest() {
		test = report.startTest("Read artist by id test - service integration");
		assertThat(validArtistDTO).isEqualTo(service.read(validArtist.getId()));
		test.log(LogStatus.PASS, "Ok");
		report.endTest(test);
	}

	@Test
	void readArtistNameTest() {
		test = report.startTest("Read artist by name test - service integration");
		assertThat(validArtistDTO).isEqualTo(service.read(validArtist.getName()));
		test.log(LogStatus.PASS, "Ok");
		report.endTest(test);
	}

	@Test
	void updateArtistTest() {
		test = report.startTest("Update artist test - service integration");
		ArtistDTO sentArtist = new ArtistDTO("updated");
		ArtistDTO responseArtist = new ArtistDTO(validArtist.getId(), "updated", emptyList);
		ArtistDTO updatedAritst = service.update(sentArtist, validArtist.getId());

		assertThat(responseArtist).isEqualTo(updatedAritst);
		test.log(LogStatus.PASS, "Ok");
		report.endTest(test);
	}

	@Test
	void deleteArtistTest() {
		test = report.startTest("Delete artist test - service integration");
		assertThat(true).isEqualTo(service.delete(validArtist.getId()));
		test.log(LogStatus.PASS, "Ok");
		report.endTest(test);
	}

}
