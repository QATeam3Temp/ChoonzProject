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
import com.qa.choonz.rest.dto.AlbumDTO;
import com.qa.choonz.service.AlbumService;
import com.qa.choonz.utils.TestWatch;
import com.qa.choonz.utils.UserSecurity;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.LogStatus;

@SpringBootTest
@ExtendWith(TestWatch.class)
public class AlbumControllerUnitTest {

	@Autowired
	private AlbumController controller;

	@MockBean
	private AlbumService service;

	@MockBean
	private UserSecurity security;

	ExtentReports report = TestWatch.report;

	private List<Long> emptyList = new ArrayList<Long>();
	private List<Album> album;
	private List<AlbumDTO> albumDTO;

	private Album validAlbum;
	private AlbumDTO validAlbumDTO;
	private AlbumDTO updatedAlbumDTO;

	@BeforeEach
	void init() {
		validAlbum = new Album(1, "test", null, null, null, "test");
		validAlbumDTO = new AlbumDTO(1, "test", emptyList, 0L, 0L, "test");
		updatedAlbumDTO = new AlbumDTO(1, "updated", emptyList, 0L, 0L, "updated");

		album = new ArrayList<Album>();
		albumDTO = new ArrayList<AlbumDTO>();

		album.add(validAlbum);
		albumDTO.add(validAlbumDTO);
	}

	@AfterAll
	static void Exit() {
		TestWatch.report.flush();
	}

	@Test
	void createAlbumTest() {
		TestWatch.test = report.startTest("Create album test - controller unit");
		when(service.create(validAlbumDTO)).thenReturn(validAlbumDTO);
		when(security.testKey(Mockito.anyString())).thenReturn(true);
		ResponseEntity<AlbumDTO> response = new ResponseEntity<AlbumDTO>(validAlbumDTO, HttpStatus.CREATED);
		assertThat(response).isEqualTo(controller.create(validAlbumDTO, "Imahash"));
		verify(service, times(1)).create(validAlbumDTO);
		TestWatch.test.log(LogStatus.PASS, "Ok");
		report.endTest(TestWatch.test);
	}
	
	@Test
	public void badCreateAlbumRequestTest() {
		test = report.startTest("Bad create album request test");
		AlbumDTO badAlbumDTO = new AlbumDTO();
		
		when(service.create(Mockito.any())).thenReturn(badAlbumDTO);
		when(security.testKey(Mockito.anyString())).thenReturn(true);
		
		ResponseEntity<AlbumDTO> response = new ResponseEntity<AlbumDTO>(HttpStatus.BAD_REQUEST);
		assertThat(response).isEqualTo(controller.create(badAlbumDTO, "Imahash"));
		
		test.log(LogStatus.PASS, "Ok");
	}

	@Test
	void createAlbumUnauthorisedTest() {
		TestWatch.test = report.startTest("Unauthorised album create test - controller unit");
		when(service.create(validAlbumDTO)).thenReturn(validAlbumDTO);
		when(security.testKey(Mockito.anyString())).thenReturn(false);
		ResponseEntity<AlbumDTO> response = new ResponseEntity<AlbumDTO>(HttpStatus.UNAUTHORIZED);
		assertThat(response).isEqualTo(controller.create(validAlbumDTO, null));
		TestWatch.test.log(LogStatus.PASS, "Ok");
		report.endTest(TestWatch.test);
	}

	@Test
	void readAlbumTest() {
		TestWatch.test = report.startTest("Read albums test - controller unit");
		when(service.read()).thenReturn(albumDTO);
		ResponseEntity<List<AlbumDTO>> response = new ResponseEntity<List<AlbumDTO>>(albumDTO, HttpStatus.OK);
		assertThat(response).isEqualTo(controller.read());
		verify(service, times(1)).read();
		TestWatch.test.log(LogStatus.PASS, "Ok");
		report.endTest(TestWatch.test);
	}

	@Test
	void readAlbumIdTest() {
		TestWatch.test = report.startTest("Read album by id test - controller unit");
		when(service.read(validAlbumDTO.getId())).thenReturn(validAlbumDTO);
		ResponseEntity<AlbumDTO> response = new ResponseEntity<AlbumDTO>(validAlbumDTO, HttpStatus.OK);
		assertThat(response).isEqualTo(controller.read(validAlbumDTO.getId()));
		verify(service, times(1)).read(validAlbumDTO.getId());
		TestWatch.test.log(LogStatus.PASS, "Ok");
		report.endTest(TestWatch.test);
	}

	@Test
	void readAlbumNameTest() {
		TestWatch.test = report.startTest("Read album by name test - controller unit");
		when(service.read(validAlbumDTO.getName())).thenReturn(validAlbumDTO);
		ResponseEntity<AlbumDTO> response = new ResponseEntity<AlbumDTO>(validAlbumDTO, HttpStatus.OK);
		assertThat(response).isEqualTo(controller.getAlbumByName(validAlbumDTO.getName()));
		verify(service, times(1)).read(validAlbumDTO.getName());
		TestWatch.test.log(LogStatus.PASS, "Ok");
		report.endTest(TestWatch.test);
	}

	@Test
	void deleteAlbumTest() {
		TestWatch.test = report.startTest("Delete album test - controller unit");
		when(service.delete(Mockito.anyLong())).thenReturn(true);
		when(security.testKey(Mockito.anyString())).thenReturn(true);
		ResponseEntity<Boolean> response = new ResponseEntity<Boolean>(true, HttpStatus.NO_CONTENT);
		assertThat(response).isEqualTo(controller.delete(validAlbumDTO.getId(), "imakey"));
		TestWatch.test.log(LogStatus.PASS, "Ok");
		report.endTest(TestWatch.test);
	}

	@Test
	void readAlbumArtistTest() {
		TestWatch.test = report.startTest("Delete album by artist test - controller unit");
		when(service.readByArtist(validAlbumDTO.getArtist())).thenReturn(albumDTO);
		ResponseEntity<List<AlbumDTO>> response = new ResponseEntity<List<AlbumDTO>>(albumDTO, HttpStatus.OK);
		assertThat(response).isEqualTo(controller.readByArtist(validAlbumDTO.getArtist()));
		verify(service, times(1)).readByArtist(validAlbumDTO.getArtist());
		TestWatch.test.log(LogStatus.PASS, "Ok");
		report.endTest(TestWatch.test);
	}

	@Test
	void readAlbumGenreTest() {
		TestWatch.test = report.startTest("Read album by genre test - controller unit");
		when(service.readByGenre(validAlbumDTO.getGenre())).thenReturn(albumDTO);
		ResponseEntity<List<AlbumDTO>> response = new ResponseEntity<List<AlbumDTO>>(albumDTO, HttpStatus.OK);
		assertThat(response).isEqualTo(controller.readByGenre(validAlbumDTO.getGenre()));
		verify(service, times(1)).readByGenre(validAlbumDTO.getGenre());
		TestWatch.test.log(LogStatus.PASS, "Ok");
		report.endTest(TestWatch.test);
	}

	@Test
	void updateAlbumTest() {
		TestWatch.test = report.startTest("Update album test - controller unit");
		when(service.update(Mockito.any(AlbumDTO.class), Mockito.anyLong())).thenReturn(updatedAlbumDTO);
		when(security.testKey(Mockito.anyString())).thenReturn(true);

		ResponseEntity<AlbumDTO> response = new ResponseEntity<AlbumDTO>(updatedAlbumDTO, HttpStatus.ACCEPTED);
		assertThat(response).isEqualTo(controller.update(updatedAlbumDTO, validAlbumDTO.getId(), "imakey"));

		verify(service, times(1)).update(Mockito.any(AlbumDTO.class), Mockito.anyLong());
		verify(security, times(1)).testKey(Mockito.anyString());
		TestWatch.test.log(LogStatus.PASS, "Ok");
		report.endTest(TestWatch.test);

	}

}
