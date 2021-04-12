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
import com.qa.choonz.persistence.domain.Artist;
import com.qa.choonz.persistence.repository.ArtistRepository;
import com.qa.choonz.rest.dto.ArtistDTO;
import com.qa.choonz.utils.TestWatch;
import com.qa.choonz.utils.mappers.ArtistMapper;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.LogStatus;

@SpringBootTest
@ExtendWith(TestWatch.class)
public class ArtistServiceUnitTest {

	@Autowired
	private ArtistService service;

	@MockBean
	private ArtistRepository repo;

	@MockBean
	private ArtistMapper mapper;

	ExtentReports report = TestWatch.report;

	private List<Long> emptyList = new ArrayList<Long>();
	private List<Album> albumList = new ArrayList<Album>();
	private List<Artist> artist;
	private List<ArtistDTO> artistDTO;

	private Artist validArtist;
	private ArtistDTO validArtistDTO;

	@BeforeEach
	void init() {
		validArtist = new Artist(1, "test", albumList);
		validArtistDTO = new ArtistDTO(1, "test", emptyList);
		artist = new ArrayList<Artist>();
		artistDTO = new ArrayList<ArtistDTO>();
		albumList = new ArrayList<Album>();

		artist.add(validArtist);
		artistDTO.add(validArtistDTO);
	}

	@AfterAll
	static void Exit() {
		TestWatch.report.flush();
	}

	@Test
	void createArtistTest() {
		TestWatch.test = report.startTest("Create artist test - service unit");
		when(repo.save(Mockito.any(Artist.class))).thenReturn(validArtist);
		when(mapper.MapToDTO(Mockito.any(Artist.class))).thenReturn(validArtistDTO);
		when(mapper.MapFromDTO(Mockito.any(ArtistDTO.class))).thenReturn(validArtist);
		assertThat(validArtistDTO).isEqualTo(service.create(validArtistDTO));
		verify(repo, times(1)).save(Mockito.any(Artist.class));
		verify(mapper, times(1)).MapFromDTO(Mockito.any(ArtistDTO.class));
		verify(mapper, times(1)).MapToDTO(Mockito.any(Artist.class));
		TestWatch.test.log(LogStatus.PASS, "Ok");
		report.endTest(TestWatch.test);
	}

	@Test
	void readArtistTest() {
		TestWatch.test = report.startTest("Read artists test - service unit");
		when(repo.findAll()).thenReturn(artist);
		when(mapper.MapToDTO(Mockito.any(Artist.class))).thenReturn(validArtistDTO);
		assertThat(artistDTO).isEqualTo(service.read());
		verify(repo, times(1)).findAll();
		verify(mapper, times(1)).MapToDTO(Mockito.any(Artist.class));
		TestWatch.test.log(LogStatus.PASS, "Ok");
		report.endTest(TestWatch.test);
	}

	@Test
	void readArtistIdTest() {
		TestWatch.test = report.startTest("Read artist by id test - service unit");
		when(repo.findById(Mockito.anyLong())).thenReturn(Optional.of(validArtist));
		when(mapper.MapToDTO(Mockito.any(Artist.class))).thenReturn(validArtistDTO);
		assertThat(validArtistDTO).isEqualTo(service.read(validArtistDTO.getId()));
		verify(repo, times(1)).findById(validArtistDTO.getId());
		verify(mapper, times(1)).MapToDTO(Mockito.any(Artist.class));
		TestWatch.test.log(LogStatus.PASS, "Ok");
		report.endTest(TestWatch.test);
	}

	@Test
	void readArtistNameTest() {
		TestWatch.test = report.startTest("Read artist by name test - service unit");
		when(repo.getArtistByNameJPQL(validArtistDTO.getName())).thenReturn(validArtist);
		when(mapper.MapToDTO(Mockito.any(Artist.class))).thenReturn(validArtistDTO);
		assertThat(validArtistDTO).isEqualTo(service.read(validArtistDTO.getName()));
		verify(repo, times(1)).getArtistByNameJPQL(validArtistDTO.getName());
		verify(mapper, times(1)).MapToDTO(Mockito.any(Artist.class));
		TestWatch.test.log(LogStatus.PASS, "Ok");
		report.endTest(TestWatch.test);
	}

	@Test
	void updateArtistTest() {
		TestWatch.test = report.startTest("Update artist test - service unit");
		ArtistDTO updatedArtistDTO = new ArtistDTO("Updated");
		Artist updatedArtist = new Artist(1, "Updated", albumList);

		when(repo.findById(Mockito.anyLong())).thenReturn(Optional.of(validArtist));
		when(repo.save(Mockito.any(Artist.class))).thenReturn(updatedArtist);
		when(mapper.MapFromDTO(Mockito.any(ArtistDTO.class))).thenReturn(updatedArtist);
		when(mapper.MapToDTO(Mockito.any(Artist.class))).thenReturn(updatedArtistDTO);

		ArtistDTO testUpdatedArtistDTO = service.update(updatedArtistDTO, validArtist.getId());

		assertThat(updatedArtistDTO).isEqualTo(testUpdatedArtistDTO);

		verify(repo, times(1)).findById(Mockito.anyLong());
		verify(repo, times(1)).save(Mockito.any(Artist.class));
		verify(mapper, times(1)).MapToDTO(Mockito.any(Artist.class));
		TestWatch.test.log(LogStatus.PASS, "Ok");
		report.endTest(TestWatch.test);

	}

	@Test
	void deleteArtistTest() {
		TestWatch.test = report.startTest("Delete artist test - service unit");
		when(repo.existsById(Mockito.anyLong())).thenReturn(true).thenReturn(false);

		assertThat(true).isEqualTo(service.delete(validArtist.getId()));

		verify(repo, times(2)).existsById(Mockito.anyLong());
		verify(repo, times(1)).deleteById(Mockito.anyLong());
		TestWatch.test.log(LogStatus.PASS, "Ok");
		report.endTest(TestWatch.test);
	}

}
