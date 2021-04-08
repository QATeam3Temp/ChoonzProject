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

import com.qa.choonz.persistence.domain.Artist;
import com.qa.choonz.rest.dto.ArtistDTO;
import com.qa.choonz.service.ArtistService;
import com.qa.choonz.utils.UserSecurity;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

@SpringBootTest
public class ArtistControllerUnitTest {

	@Autowired
	private ArtistController controller;

	@MockBean
	private ArtistService service;

	@MockBean
	private UserSecurity security;

	static ExtentReports report = new ExtentReports("Documentation/reports/Artist_Controller_Unit_Report.html", true);
	static ExtentTest test;

	private List<Long> emptyList = new ArrayList<Long>();
	private List<Artist> artist;
	private List<ArtistDTO> artistDTO;

	private Artist validArtist;
	private ArtistDTO validArtistDTO;
	private ArtistDTO updatedArtistDTO;

	@BeforeEach
	public void init() {
		validArtist = new Artist(1, "test", null);
		validArtistDTO = new ArtistDTO(1, "test", emptyList);
		updatedArtistDTO = new ArtistDTO(1, "updated", emptyList);

		artist = new ArrayList<Artist>();
		artistDTO = new ArrayList<ArtistDTO>();

		artist.add(validArtist);
		artistDTO.add(updatedArtistDTO);
	}

	@AfterAll
	static void Exit() {
		report.flush();
	}

	@Test
	public void createArtistTest() {
		test = report.startTest("Create artist test");
		when(service.create(validArtistDTO)).thenReturn(validArtistDTO);
		when(security.testKey(Mockito.anyString())).thenReturn(true);
		ResponseEntity<ArtistDTO> response = new ResponseEntity<ArtistDTO>(validArtistDTO, HttpStatus.CREATED);
		assertThat(response).isEqualTo(controller.create(validArtistDTO, "Imahash"));
		verify(service, times(1)).create(validArtistDTO);
		test.log(LogStatus.PASS, "Ok");
	}

	@Test
	public void createArtistUnauthorisedTest() {
		test = report.startTest("Unauthorised create artist test");
		when(service.create(validArtistDTO)).thenReturn(validArtistDTO);
		when(security.testKey(Mockito.anyString())).thenReturn(false);
		ResponseEntity<ArtistDTO> response = new ResponseEntity<ArtistDTO>(HttpStatus.UNAUTHORIZED);
		assertThat(response).isEqualTo(controller.create(validArtistDTO, null));
		test.log(LogStatus.PASS, "Ok");
	}

	@Test
	public void readAllArtists() {
		test = report.startTest("Read artists test");
		when(service.read()).thenReturn(artistDTO);
		ResponseEntity<List<ArtistDTO>> response = new ResponseEntity<List<ArtistDTO>>(artistDTO, HttpStatus.OK);
		assertThat(response).isEqualTo(controller.read());
		verify(service, times(1)).read();
		test.log(LogStatus.PASS, "Ok");
	}

	@Test
	public void readArtistIdTest() {
		test = report.startTest("Read artists by id test");
		when(service.read(validArtistDTO.getId())).thenReturn(validArtistDTO);
		ResponseEntity<ArtistDTO> response = new ResponseEntity<ArtistDTO>(validArtistDTO, HttpStatus.OK);
		assertThat(response).isEqualTo(controller.read(validArtistDTO.getId()));
		verify(service, times(1)).read(validArtistDTO.getId());
		test.log(LogStatus.PASS, "Ok");
	}

	@Test
	public void readArtistNameTest() {
		test = report.startTest("Read artists by name test");
		when(service.read(validArtistDTO.getName())).thenReturn(validArtistDTO);
		ResponseEntity<ArtistDTO> response = new ResponseEntity<ArtistDTO>(validArtistDTO, HttpStatus.OK);
		assertThat(response).isEqualTo(controller.read(validArtistDTO.getName()));
		verify(service, times(1)).read(validArtistDTO.getName());
		test.log(LogStatus.PASS, "Ok");
	}
	
	@Test
	public void updateArtistTest() {
		when(service.update(Mockito.any(ArtistDTO.class), Mockito.anyLong())).thenReturn(updatedArtistDTO);
		when(security.testKey(Mockito.anyString())).thenReturn(true);
		
		ResponseEntity<ArtistDTO> response = new ResponseEntity<ArtistDTO>(updatedArtistDTO, HttpStatus.ACCEPTED);
		assertThat(response).isEqualTo(controller.update(updatedArtistDTO, validArtistDTO.getId(), "imakey"));
		
		verify(service, times(1)).update(Mockito.any(ArtistDTO.class), Mockito.anyLong());
		verify(security, times(1)).testKey(Mockito.anyString());
	}
	
	@Test
	public void deleteArtistTest() {
		when(service.delete(Mockito.anyLong())).thenReturn(true);
		when(security.testKey(Mockito.anyString())).thenReturn(true);
		
		ResponseEntity<Boolean> response = new ResponseEntity<Boolean>(true, HttpStatus.NO_CONTENT);
		assertThat(response).isEqualTo(controller.delete(validArtistDTO.getId(), "imakey"));
		
		verify(service, times(2)).delete(Mockito.anyLong());
		verify(security, times(1)).testKey(Mockito.anyString());
	}
	
}
