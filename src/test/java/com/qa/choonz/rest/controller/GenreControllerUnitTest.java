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

import com.qa.choonz.persistence.domain.Genre;
import com.qa.choonz.rest.dto.GenreDTO;
import com.qa.choonz.service.GenreService;
import com.qa.choonz.utils.UserSecurity;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

@SpringBootTest
public class GenreControllerUnitTest {

	@Autowired
	private GenreController controller;

	@MockBean
	private GenreService service;

	@MockBean
	private UserSecurity security;

	static ExtentReports report = new ExtentReports("Documentation/reports/Genre_Controller_Unit_Report.html", true);
	static ExtentTest test;

	private List<Long> emptyList = new ArrayList<Long>();
	private List<Genre> genre;
	private List<GenreDTO> genreDTO;

	private Genre validGenre;
	private GenreDTO validGenreDTO;

	private GenreDTO updatedGenreDTO;

	@BeforeEach
	public void init() {
		validGenre = new Genre(1, "test", "test", null);
		validGenreDTO = new GenreDTO(1, "test", "test", emptyList);
		updatedGenreDTO = new GenreDTO(1, "updated", "updated", emptyList);

		genre = new ArrayList<Genre>();
		genreDTO = new ArrayList<GenreDTO>();

		genre.add(validGenre);
		genreDTO.add(validGenreDTO);
	}

	@AfterAll
	static void Exit() {
		report.flush();
	}

	@Test
	public void createGenre() {
		test = report.startTest("Create genre test");
		when(service.create(validGenreDTO)).thenReturn(validGenreDTO);
		when(security.testKey(Mockito.anyString())).thenReturn(true);
		ResponseEntity<GenreDTO> response = new ResponseEntity<GenreDTO>(validGenreDTO, HttpStatus.CREATED);
		assertThat(response).isEqualTo(controller.create(validGenreDTO, "Imahash"));
		verify(service, times(1)).create(validGenreDTO);
		test.log(LogStatus.PASS, "Ok");
	}

	@Test
	public void createGenreUnauthorised() {
		test = report.startTest("Unauthorised create genre test");
		when(service.create(validGenreDTO)).thenReturn(validGenreDTO);
		when(security.testKey(Mockito.anyString())).thenReturn(false);
		ResponseEntity<GenreDTO> response = new ResponseEntity<GenreDTO>(HttpStatus.UNAUTHORIZED);
		assertThat(response).isEqualTo(controller.create(validGenreDTO, null));
		test.log(LogStatus.PASS, "Ok");
	}

	@Test
	public void readAllGenres() {
		test = report.startTest("Read genres test");
		when(service.read()).thenReturn(genreDTO);
		ResponseEntity<List<GenreDTO>> response = new ResponseEntity<List<GenreDTO>>(genreDTO, HttpStatus.OK);
		assertThat(response).isEqualTo(controller.read());
		verify(service, times(1)).read();
		test.log(LogStatus.PASS, "Ok");
	}

	@Test
	public void readGenreIdTest() {
		test = report.startTest("Read genre by id test");
		when(service.read(validGenreDTO.getId())).thenReturn(validGenreDTO);
		ResponseEntity<GenreDTO> response = new ResponseEntity<GenreDTO>(validGenreDTO, HttpStatus.OK);
		assertThat(response).isEqualTo(controller.read(validGenreDTO.getId()));
		verify(service, times(1)).read(validGenreDTO.getId());
		test.log(LogStatus.PASS, "Ok");
	}

	@Test
	public void readGenreNameTest() {
		test = report.startTest("Read genre by name test");
		when(service.read(validGenreDTO.getName())).thenReturn(validGenreDTO);
		ResponseEntity<GenreDTO> response = new ResponseEntity<GenreDTO>(validGenreDTO, HttpStatus.OK);
		assertThat(response).isEqualTo(controller.getGenreByName(validGenreDTO.getName()));
		verify(service, times(1)).read(validGenreDTO.getName());
		test.log(LogStatus.PASS, "Ok");
	}

	@Test
	public void updateGenre() {
		test = report.startTest("Update genre test");
		when(service.update(Mockito.any(GenreDTO.class), Mockito.anyLong())).thenReturn(updatedGenreDTO);
		when(security.testKey(Mockito.anyString())).thenReturn(true);
		ResponseEntity<GenreDTO> response = new ResponseEntity<GenreDTO>(updatedGenreDTO, HttpStatus.ACCEPTED);
		assertThat(response).isEqualTo(controller.update(updatedGenreDTO, validGenreDTO.getId(), "ImaKey"));
		verify(service, times(1)).update(Mockito.any(GenreDTO.class), Mockito.anyLong());
		test.log(LogStatus.PASS, "Ok");
	}

	@Test
	public void updateGenreUnauthorised() {
		test = report.startTest("Unauthorised update genre test");
		when(service.update(Mockito.any(GenreDTO.class), Mockito.anyLong())).thenReturn(updatedGenreDTO);
		when(security.testKey(Mockito.anyString())).thenReturn(false);
		ResponseEntity<GenreDTO> response = new ResponseEntity<GenreDTO>(HttpStatus.UNAUTHORIZED);
		assertThat(response).isEqualTo(controller.update(updatedGenreDTO, validGenreDTO.getId(), null));
		test.log(LogStatus.PASS, "Ok");
	}

	@Test
	public void deleteGenreTest() {
		test = report.startTest("Delete genre test");
		when(service.delete(Mockito.anyLong())).thenReturn(true);
		when(security.testKey(Mockito.anyString())).thenReturn(true);
		ResponseEntity<Boolean> response = new ResponseEntity<Boolean>(true, HttpStatus.NO_CONTENT);
		assertThat(response).isEqualTo(controller.delete(validGenreDTO.getId(), "ImaKey"));
		verify(service, times(1)).delete(Mockito.anyLong());
		test.log(LogStatus.PASS, "Ok");
	}

	@Test
	public void deleteGenreTestUnauthorised() {
		test = report.startTest("Unauthorised delete genre test");
		when(service.delete(Mockito.anyLong())).thenReturn(true);
		when(security.testKey(Mockito.anyString())).thenReturn(false);
		ResponseEntity<Boolean> response = new ResponseEntity<Boolean>(HttpStatus.UNAUTHORIZED);
		assertThat(response).isEqualTo(controller.delete(validGenreDTO.getId(), null));
		test.log(LogStatus.PASS, "Ok");
	}

}
