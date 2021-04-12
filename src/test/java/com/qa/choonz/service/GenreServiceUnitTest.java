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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.qa.choonz.persistence.domain.Album;
import com.qa.choonz.persistence.domain.Genre;
import com.qa.choonz.persistence.repository.GenreRepository;
import com.qa.choonz.rest.dto.GenreDTO;
import com.qa.choonz.utils.TestWatch;
import com.qa.choonz.utils.mappers.GenreMapper;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.LogStatus;

@SpringBootTest
@ExtendWith(TestWatch.class)
public class GenreServiceUnitTest {

	@Autowired
	private GenreService service;

	@MockBean
	private GenreRepository repo;

	@MockBean
	private GenreMapper mapper;

	ExtentReports report = TestWatch.report;

	private List<Long> emptyList = new ArrayList<Long>();
	private List<Album> albumList = new ArrayList<Album>();
	private List<Genre> genre;
	private List<GenreDTO> genreDTO;

	private Genre validGenre;
	private GenreDTO validGenreDTO;

	@BeforeEach
	void init() {
		validGenre = new Genre(1, "test", "test", albumList);
		validGenreDTO = new GenreDTO(1, "test", "test", emptyList);
		genre = new ArrayList<Genre>();
		genreDTO = new ArrayList<GenreDTO>();
		albumList = new ArrayList<Album>();

		genre.add(validGenre);
		genreDTO.add(validGenreDTO);

	}

	@AfterAll
	static void Exit() {
		TestWatch.report.flush();
	}

	@Test
	void updateGenreTest() {
		TestWatch.test = report.startTest("Updated genre test - service unit");
		GenreDTO updatedGenreDTO = new GenreDTO("updated genre", "updated genre");
		Genre updatedGenre = new Genre(1, "updated genre", "updated genre", albumList);

		when(repo.findById(Mockito.anyLong())).thenReturn(Optional.of(validGenre));
		when(repo.save(Mockito.any(Genre.class))).thenReturn(updatedGenre);
		when(mapper.MapFromDTO(Mockito.any(GenreDTO.class))).thenReturn(updatedGenre);
		when(mapper.MapToDTO(Mockito.any(Genre.class))).thenReturn(updatedGenreDTO);

		GenreDTO testUpdatedGenreDTO = service.update(updatedGenreDTO, validGenre.getId());

		assertThat(updatedGenreDTO).isEqualTo(testUpdatedGenreDTO);

		verify(repo, times(1)).findById(Mockito.anyLong());
		verify(repo, times(1)).save(Mockito.any(Genre.class));
		verify(mapper, times(1)).MapToDTO(Mockito.any(Genre.class));
		TestWatch.test.log(LogStatus.PASS, "Ok");
		report.endTest(TestWatch.test);
	}

	@Test
	void createGenreTest() {
		TestWatch.test = report.startTest("Create genre test - service unit");
		when(repo.save(Mockito.any(Genre.class))).thenReturn(validGenre);
		when(mapper.MapToDTO(Mockito.any(Genre.class))).thenReturn(validGenreDTO);
		when(mapper.MapFromDTO(Mockito.any(GenreDTO.class))).thenReturn(validGenre);
		assertThat(validGenreDTO).isEqualTo(service.create(validGenreDTO));
		verify(repo, times(1)).save(Mockito.any(Genre.class));
		verify(mapper, times(1)).MapFromDTO(Mockito.any(GenreDTO.class));
		verify(mapper, times(1)).MapToDTO(Mockito.any(Genre.class));
		TestWatch.test.log(LogStatus.PASS, "Ok");
		report.endTest(TestWatch.test);
	}

	@Test
	void readGenresTest() {
		TestWatch.test = report.startTest("Read genres test - service unit");
		when(repo.findAll()).thenReturn(genre);
		when(mapper.MapToDTO(Mockito.any(Genre.class))).thenReturn(validGenreDTO);
		assertThat(genreDTO).isEqualTo(service.read());
		verify(repo, times(1)).findAll();
		verify(mapper, times(1)).MapToDTO(Mockito.any(Genre.class));
		TestWatch.test.log(LogStatus.PASS, "Ok");
		report.endTest(TestWatch.test);
	}

	@Test
	void readGenreIdTest() {
		TestWatch.test = report.startTest("Read genre by id test - service unit");
		when(repo.findById(Mockito.anyLong())).thenReturn(Optional.of(validGenre));
		when(mapper.MapToDTO(Mockito.any(Genre.class))).thenReturn(validGenreDTO);
		assertThat(validGenreDTO).isEqualTo(service.read(validGenreDTO.getId()));
		verify(repo, times(1)).findById(validGenreDTO.getId());
		verify(mapper, times(1)).MapToDTO(Mockito.any(Genre.class));
		TestWatch.test.log(LogStatus.PASS, "Ok");
		report.endTest(TestWatch.test);
	}

	@Test
	void readGenreNameTest() {
		TestWatch.test = report.startTest("Read genre by name test - service unit");
		when(repo.getGenreByNameJPQL(validGenreDTO.getName())).thenReturn(validGenre);
		when(mapper.MapToDTO(Mockito.any(Genre.class))).thenReturn(validGenreDTO);
		assertThat(validGenreDTO).isEqualTo(service.read(validGenreDTO.getName()));
		verify(repo, times(1)).getGenreByNameJPQL(validGenreDTO.getName());
		verify(mapper, times(1)).MapToDTO(Mockito.any(Genre.class));
		TestWatch.test.log(LogStatus.PASS, "Ok");
		report.endTest(TestWatch.test);
	}

	@Test
	void deleteGenreTest() {
		TestWatch.test = report.startTest("Deleted genre test - service unit");
		when(repo.existsById(Mockito.anyLong())).thenReturn(true).thenReturn(false);

		assertThat(true).isEqualTo(service.delete(validGenre.getId()));

		verify(repo, times(2)).existsById(Mockito.anyLong());
		verify(repo, times(1)).deleteById(Mockito.anyLong());
		TestWatch.test.log(LogStatus.PASS, "Ok");
		report.endTest(TestWatch.test);
	}

}
