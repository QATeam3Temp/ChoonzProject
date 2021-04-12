package com.qa.choonz.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.qa.choonz.persistence.domain.Album;
import com.qa.choonz.persistence.domain.Artist;
import com.qa.choonz.persistence.repository.AlbumRepository;
import com.qa.choonz.persistence.repository.ArtistRepository;
import com.qa.choonz.rest.dto.ArtistDTO;
import com.qa.choonz.utils.TestWatch;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.LogStatus;

@SpringBootTest
@Transactional
@ExtendWith(TestWatch.class)
public class ArtistServiceIntegrationTest {

	@Autowired
	private AlbumRepository aRepo;

	@Autowired
	private ArtistRepository repo;

	@Autowired
	private ArtistService service;

	ExtentReports report = TestWatch.report;

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
		TestWatch.report.flush();
	}

	@Test
	void createArtistTest() {
		TestWatch.test = report.startTest("Create artist test - service integration");
		ArtistDTO newArtist = new ArtistDTO("test2");
		ArtistDTO expectedArtist = new ArtistDTO(validArtist.getId() + 1, "test2", emptyList);
		assertThat(expectedArtist).isEqualTo(service.create(newArtist));
		TestWatch.test.log(LogStatus.PASS, "Ok");
		report.endTest(TestWatch.test);
	}

	@Test
	void readAllArtistsTest() {
		TestWatch.test = report.startTest("Read artists test - service integration");
		List<ArtistDTO> artistInDb = service.read();
		assertThat(artistDTO).isEqualTo(artistInDb);
		TestWatch.test.log(LogStatus.PASS, "Ok");
		report.endTest(TestWatch.test);
	}

	@Test
	void readArtistIdTest() {
		TestWatch.test = report.startTest("Read artist by id test - service integration");
		assertThat(validArtistDTO).isEqualTo(service.read(validArtist.getId()));
		TestWatch.test.log(LogStatus.PASS, "Ok");
		report.endTest(TestWatch.test);
	}

	@Test
	void readArtistNameTest() {
		TestWatch.test = report.startTest("Read artist by name test - service integration");
		assertThat(validArtistDTO).isEqualTo(service.read(validArtist.getName()));
		TestWatch.test.log(LogStatus.PASS, "Ok");
		report.endTest(TestWatch.test);
	}

	@Test
	void updateArtistTest() {
		TestWatch.test = report.startTest("Update artist test - service integration");
		ArtistDTO sentArtist = new ArtistDTO("updated");
		ArtistDTO responseArtist = new ArtistDTO(validArtist.getId(), "updated", emptyList);
		ArtistDTO updatedAritst = service.update(sentArtist, validArtist.getId());

		assertThat(responseArtist).isEqualTo(updatedAritst);
		TestWatch.test.log(LogStatus.PASS, "Ok");
		report.endTest(TestWatch.test);
	}

	@Test
	void deleteArtistTest() {
		TestWatch.test = report.startTest("Delete artist test - service integration");
		assertThat(true).isEqualTo(service.delete(validArtist.getId()));
		TestWatch.test.log(LogStatus.PASS, "Ok");
		report.endTest(TestWatch.test);
	}

}
