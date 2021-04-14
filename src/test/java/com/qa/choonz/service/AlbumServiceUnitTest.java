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
import com.qa.choonz.persistence.domain.Genre;
import com.qa.choonz.persistence.domain.Track;
import com.qa.choonz.persistence.repository.AlbumRepository;
import com.qa.choonz.rest.dto.AlbumDTO;
import com.qa.choonz.utils.TestWatch;
import com.qa.choonz.utils.mappers.AlbumMapper;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.LogStatus;

@SpringBootTest
@ExtendWith(TestWatch.class)
public class AlbumServiceUnitTest {

	@Autowired
	private AlbumService service;

	@MockBean
	private AlbumRepository repo;

	@MockBean
	private AlbumMapper mapper;

	ExtentReports report = TestWatch.report;

	private List<Album> album;
	private List<AlbumDTO> albumDTO;

	private Track validTrack = new Track();
	private List<Track> validTracks = List.of(validTrack);
	private Genre validGenre = new Genre();
	private Artist validArtist = new Artist();
	private Album validAlbum;
	private AlbumDTO validAlbumDTO;

	@BeforeEach
	void init() {
		validAlbum = new Album(1, "test", validTracks, validArtist, validGenre, "test");
		validAlbumDTO = new AlbumDTO(1, "test", List.of(0L), 0L, 0L, "test");

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
		TestWatch.test = report.startTest("Create album test - service unit");
		when(repo.save(Mockito.any(Album.class))).thenReturn(validAlbum);
		when(mapper.MapToDTO(Mockito.any(Album.class))).thenReturn(validAlbumDTO);
		when(mapper.MapFromDTO(Mockito.any(AlbumDTO.class))).thenReturn(validAlbum);
		assertThat(validAlbumDTO).isEqualTo(service.create(validAlbumDTO));
		verify(repo, times(1)).save(Mockito.any(Album.class));
		verify(mapper, times(1)).MapFromDTO(Mockito.any(AlbumDTO.class));
		verify(mapper, times(1)).MapToDTO(Mockito.any(Album.class));
		TestWatch.test.log(LogStatus.PASS, "Ok");
		report.endTest(TestWatch.test);
	}

	@Test
	void readAlbumsTest() {
		TestWatch.test = report.startTest("Read albums test - service unit");
		when(repo.findAll()).thenReturn(album);
		when(mapper.MapToDTO(Mockito.any(Album.class))).thenReturn(validAlbumDTO);
		assertThat(albumDTO).isEqualTo(service.read());
		verify(repo, times(1)).findAll();
		verify(mapper, times(1)).MapToDTO(Mockito.any(Album.class));
		TestWatch.test.log(LogStatus.PASS, "Ok");
		report.endTest(TestWatch.test);
	}

	@Test
	void readAlbumIdTest() {
		TestWatch.test = report.startTest("Read album by id test - service unit");
		when(repo.findById(Mockito.anyLong())).thenReturn(Optional.of(validAlbum));
		when(mapper.MapToDTO(Mockito.any(Album.class))).thenReturn(validAlbumDTO);
		assertThat(validAlbumDTO).isEqualTo(service.read(validAlbumDTO.getId()));
		verify(repo, times(1)).findById(validAlbumDTO.getId());
		verify(mapper, times(1)).MapToDTO(Mockito.any(Album.class));
		TestWatch.test.log(LogStatus.PASS, "Ok");
		report.endTest(TestWatch.test);
	}

	@Test
	void readAlbumNameTest() {
		TestWatch.test = report.startTest("Read album by name test - service unit");
		when(repo.getAlbumByNameJPQL(validAlbumDTO.getName())).thenReturn(validAlbum);
		when(mapper.MapToDTO(Mockito.any(Album.class))).thenReturn(validAlbumDTO);
		assertThat(validAlbumDTO).isEqualTo(service.read(validAlbumDTO.getName()));
		verify(repo, times(1)).getAlbumByNameJPQL(validAlbumDTO.getName());
		verify(mapper, times(1)).MapToDTO(Mockito.any(Album.class));
		TestWatch.test.log(LogStatus.PASS, "Ok");
		report.endTest(TestWatch.test);
	}

	@Test
	void readAlbumGenreTest() {
		TestWatch.test = report.startTest("Read albums by genre test - service unit");
		when(repo.getAlbumByGenreSQL(validAlbumDTO.getGenre())).thenReturn(album);
		when(mapper.MapToDTO(Mockito.any(Album.class))).thenReturn(validAlbumDTO);
		assertThat(albumDTO).isEqualTo(service.readByGenre(validAlbumDTO.getGenre()));
		verify(repo, times(1)).getAlbumByGenreSQL(validAlbumDTO.getGenre());
		verify(mapper, times(1)).MapToDTO(Mockito.any(Album.class));
		TestWatch.test.log(LogStatus.PASS, "Ok");
		report.endTest(TestWatch.test);
	}

	@Test
	void readAlbumArtistTest() {
		TestWatch.test = report.startTest("Read albums by artist test - service unit");
		when(repo.getAlbumByArtistSQL(validAlbumDTO.getArtist())).thenReturn(album);
		when(mapper.MapToDTO(Mockito.any(Album.class))).thenReturn(validAlbumDTO);
		assertThat(albumDTO).isEqualTo(service.readByArtist(validAlbumDTO.getArtist()));
		verify(repo, times(1)).getAlbumByArtistSQL(validAlbumDTO.getArtist());
		verify(mapper, times(1)).MapToDTO(Mockito.any(Album.class));
		TestWatch.test.log(LogStatus.PASS, "Ok");
		report.endTest(TestWatch.test);
	}

	@Test
	void updateAlbumTest() {
		TestWatch.test = report.startTest("Update album test - service unit");
		AlbumDTO updatedAlbumDTO = new AlbumDTO("updated", "updated");
		Album updatedAlbum = new Album(1, "updated", validTracks, validArtist, validGenre, "updated");

		when(repo.findById(Mockito.anyLong())).thenReturn(Optional.of(validAlbum));
		when(repo.save(Mockito.any(Album.class))).thenReturn(updatedAlbum);
		when(mapper.MapFromDTO(Mockito.any(AlbumDTO.class))).thenReturn(updatedAlbum);
		when(mapper.MapToDTO(Mockito.any(Album.class))).thenReturn(updatedAlbumDTO);

		AlbumDTO testUpdatedAlbumDTO = service.update(updatedAlbumDTO, validAlbum.getId());

		assertThat(updatedAlbumDTO).isEqualTo(testUpdatedAlbumDTO);

		verify(repo, times(1)).findById(Mockito.anyLong());
		verify(repo, times(1)).save(Mockito.any(Album.class));
		verify(mapper, times(1)).MapToDTO(Mockito.any(Album.class));
		verify(mapper, times(2)).MapFromDTO(Mockito.any(AlbumDTO.class));
		TestWatch.test.log(LogStatus.PASS, "Ok");
		report.endTest(TestWatch.test);
	}

	@Test
	void deleteAlbumTest() {
		TestWatch.test = report.startTest("Delete album test - service unit");

		when(repo.existsById(Mockito.anyLong())).thenReturn(true).thenReturn(false);

		assertThat(true).isEqualTo(service.delete(validAlbum.getId()));

		verify(repo, times(2)).existsById(Mockito.anyLong());
		verify(repo, times(1)).deleteById(Mockito.anyLong());
		TestWatch.test.log(LogStatus.PASS, "Ok");
		report.endTest(TestWatch.test);
	}

}
