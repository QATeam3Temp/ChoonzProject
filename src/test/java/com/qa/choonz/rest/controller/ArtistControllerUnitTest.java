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
import com.qa.choonz.persistence.domain.Artist;
import com.qa.choonz.rest.dto.ArtistDTO;
import com.qa.choonz.service.ArtistService;
import com.qa.choonz.utils.TestWatch;
import com.qa.choonz.utils.UserSecurity;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.LogStatus;

@SpringBootTest
@ExtendWith(TestWatch.class)
public class ArtistControllerUnitTest {

	@Autowired
	private ArtistController controller;

	@MockBean
	private ArtistService service;

	@MockBean
	private UserSecurity security;

	ExtentReports report = TestWatch.report;

	private List<Long> emptyList = new ArrayList<Long>();
	private List<Artist> artist;
	private List<ArtistDTO> artistDTO;
	private List<Album> validAlbums = new ArrayList<Album>();
	private Artist validArtist;
	private ArtistDTO validArtistDTO;
	private ArtistDTO updatedArtistDTO;

	@BeforeEach
	void init() {
		validArtist = new Artist(1, "test", validAlbums);
		validArtistDTO = new ArtistDTO(1, "test", emptyList);
		updatedArtistDTO = new ArtistDTO(1, "updated", emptyList);

		artist = new ArrayList<Artist>();
		artistDTO = new ArrayList<ArtistDTO>();

		artist.add(validArtist);
		artistDTO.add(updatedArtistDTO);
	}

	@AfterAll
	static void Exit() {
		TestWatch.report.flush();
	}

	@Test
	void createArtistTest() {
		TestWatch.test = report.startTest("Create artist test - controller unit");
		when(service.create(validArtistDTO)).thenReturn(validArtistDTO);
		when(security.testKey(Mockito.anyString())).thenReturn(true);
		ResponseEntity<ArtistDTO> response = new ResponseEntity<ArtistDTO>(validArtistDTO, HttpStatus.CREATED);
		assertThat(response).isEqualTo(controller.create(validArtistDTO, "Imahash"));
		verify(service, times(1)).create(validArtistDTO);
		TestWatch.test.log(LogStatus.PASS, "Ok");
		report.endTest(TestWatch.test);
	}

	@Test
	public void badCreateArtistRequestTest() {
		TestWatch.test = report.startTest("Bad create artist request test");
		ArtistDTO badArtistDTO = new ArtistDTO();

		when(service.create(Mockito.any())).thenReturn(badArtistDTO);
		when(security.testKey(Mockito.anyString())).thenReturn(true);

		ResponseEntity<ArtistDTO> response = new ResponseEntity<ArtistDTO>(HttpStatus.BAD_REQUEST);
		assertThat(response).isEqualTo(controller.create(badArtistDTO, "Imahash"));

		TestWatch.test.log(LogStatus.PASS, "Ok");
	}

	@Test
	void createArtistUnauthorisedTest() {
		TestWatch.test = report.startTest("Unauthorised create artist test - controller unit");
		when(service.create(validArtistDTO)).thenReturn(validArtistDTO);
		when(security.testKey(Mockito.anyString())).thenReturn(false);
		ResponseEntity<ArtistDTO> response = new ResponseEntity<ArtistDTO>(HttpStatus.UNAUTHORIZED);
		assertThat(response).isEqualTo(controller.create(validArtistDTO, null));
		TestWatch.test.log(LogStatus.PASS, "Ok");
		report.endTest(TestWatch.test);
	}

	@Test
	void readAllArtists() {
		TestWatch.test = report.startTest("Read artists test - controller unit");
		when(service.read()).thenReturn(artistDTO);
		ResponseEntity<List<ArtistDTO>> response = new ResponseEntity<List<ArtistDTO>>(artistDTO, HttpStatus.OK);
		assertThat(response).isEqualTo(controller.read());
		verify(service, times(1)).read();
		TestWatch.test.log(LogStatus.PASS, "Ok");
		report.endTest(TestWatch.test);
	}

	@Test
	void readArtistIdTest() {
		TestWatch.test = report.startTest("Read artists by id test - controller unit");
		when(service.read(validArtistDTO.getId())).thenReturn(validArtistDTO);
		ResponseEntity<ArtistDTO> response = new ResponseEntity<ArtistDTO>(validArtistDTO, HttpStatus.OK);
		assertThat(response).isEqualTo(controller.read(validArtistDTO.getId()));
		verify(service, times(1)).read(validArtistDTO.getId());
		TestWatch.test.log(LogStatus.PASS, "Ok");
		report.endTest(TestWatch.test);
	}

	@Test
	void readArtistNameTest() {
		TestWatch.test = report.startTest("Read artists by name test - controller unit");
		when(service.read(validArtistDTO.getName())).thenReturn(validArtistDTO);
		ResponseEntity<ArtistDTO> response = new ResponseEntity<ArtistDTO>(validArtistDTO, HttpStatus.OK);
		assertThat(response).isEqualTo(controller.read(validArtistDTO.getName()));
		verify(service, times(1)).read(validArtistDTO.getName());
		TestWatch.test.log(LogStatus.PASS, "Ok");
		report.endTest(TestWatch.test);
	}

	@Test
	void updateArtistTest() {
		TestWatch.test = report.startTest("Update artist test - controller unit");
		when(service.update(Mockito.any(ArtistDTO.class), Mockito.anyLong())).thenReturn(updatedArtistDTO);
		when(security.testKey(Mockito.anyString())).thenReturn(true);

		ResponseEntity<ArtistDTO> response = new ResponseEntity<ArtistDTO>(updatedArtistDTO, HttpStatus.ACCEPTED);
		assertThat(response).isEqualTo(controller.update(updatedArtistDTO, validArtistDTO.getId(), "imakey"));

		verify(service, times(1)).update(Mockito.any(ArtistDTO.class), Mockito.anyLong());
		verify(security, times(1)).testKey(Mockito.anyString());
		TestWatch.test.log(LogStatus.PASS, "Ok");
		report.endTest(TestWatch.test);
	}

	@Test
	void deleteArtistTest() {
		TestWatch.test = report.startTest("Delete artist test - controller unit");
		when(service.delete(Mockito.anyLong())).thenReturn(true);
		when(security.testKey(Mockito.anyString())).thenReturn(true);

		ResponseEntity<Boolean> response = new ResponseEntity<Boolean>(true, HttpStatus.NO_CONTENT);
		assertThat(response).isEqualTo(controller.delete(validArtistDTO.getId(), "imakey"));
		verify(service, times(1)).delete(Mockito.anyLong());
		verify(security, times(1)).testKey(Mockito.anyString());
		TestWatch.test.log(LogStatus.PASS, "Ok");
		report.endTest(TestWatch.test);
	}

}
