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

	static ExtentReports report = new ExtentReports("Documentation/reports/Artist_Service_Integration_Report.html",
			true);
	static ExtentTest test;

	private List<Long> emptyList = new ArrayList<Long>();
	private List<Artist> artist = new ArrayList<Artist>();
	private List<ArtistDTO> artistDTO = new ArrayList<ArtistDTO>();
	private Album validAlbum = new Album(1, "test", null, null, null, "test");
	private List<Album> validAlbums = new ArrayList<Album>();
	private Artist validArtist;
	private ArtistDTO validArtistDTO;

	@BeforeEach
	public void init() {
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
	public void createArtistTest() {
		test = report.startTest("Create artist test");
		ArtistDTO newArtist = new ArtistDTO("test2");
		ArtistDTO expectedArtist = new ArtistDTO(validArtist.getId() + 1, "test2", emptyList);
		assertThat(expectedArtist).isEqualTo(service.create(newArtist));
		test.log(LogStatus.PASS, "Ok");
	}

	@Test
	public void readAllArtistsTest() {
		test = report.startTest("Read artists test");
		List<ArtistDTO> artistInDb = service.read();
		assertThat(artistDTO).isEqualTo(artistInDb);
		test.log(LogStatus.PASS, "Ok");
	}

	@Test
	public void readArtistIdTest() {
		test = report.startTest("Read artist by id test");
		assertThat(validArtistDTO).isEqualTo(service.read(validArtist.getId()));
		test.log(LogStatus.PASS, "Ok");
	}

	@Test
	public void readArtistNameTest() {
		test = report.startTest("Read artist by name test");
		assertThat(validArtistDTO).isEqualTo(service.read(validArtist.getName()));
		test.log(LogStatus.PASS, "Ok");
	}

	@Test
	public void updateArtistTest() {
		test = report.startTest("Update artist test");
		ArtistDTO sentArtist = new ArtistDTO("updated");
		ArtistDTO responseArtist = new ArtistDTO(validArtist.getId(), "updated", emptyList);
		ArtistDTO updatedAritst = service.update(sentArtist, validArtist.getId());

		assertThat(responseArtist).isEqualTo(updatedAritst);
		test.log(LogStatus.PASS, "Ok");
	}

	@Test
	public void deleteArtistTest() {
		test = report.startTest("Delete artist test");
		assertThat(true).isEqualTo(service.delete(validArtist.getId()));
		test.log(LogStatus.PASS, "Ok");
	}

}
