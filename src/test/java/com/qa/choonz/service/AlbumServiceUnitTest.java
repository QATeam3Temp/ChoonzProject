package com.qa.choonz.service;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import com.qa.choonz.utils.mappers.AlbumMapper;

@SpringBootTest
public class AlbumServiceUnitTest {

	@Autowired
	private AlbumService service;

	@MockBean
	private AlbumRepository repo;
	
	@MockBean
	private AlbumMapper mapper;

	private List<Album> album;
	private List<AlbumDTO> albumDTO;

	private Track validTrack = new Track();
	private List<Track> validTracks = List.of(validTrack);
	private Genre validGenre = new Genre();
	private Artist validArtist = new Artist();
	private Album validAlbum;
	private AlbumDTO validAlbumDTO;

	@BeforeEach
	public void init() {
		validAlbum = new Album(1, "test", validTracks, validArtist, validGenre, "test");
		validAlbumDTO = new AlbumDTO(1, "test", List.of(0L), 0L, 0L, "test");

		album = new ArrayList<Album>();
		albumDTO = new ArrayList<AlbumDTO>();

		album.add(validAlbum);
		albumDTO.add(validAlbumDTO);
	}

	@Test
	public void createAlbumTest() {
		when(repo.save(Mockito.any(Album.class))).thenReturn(validAlbum);
		when(mapper.MapToDTO(Mockito.any(Album.class))).thenReturn(validAlbumDTO);
		when(mapper.MapFromDTO(Mockito.any(AlbumDTO.class))).thenReturn(validAlbum);
		assertThat(validAlbumDTO).isEqualTo(service.create(validAlbumDTO));
		verify(repo, times(1)).save(Mockito.any(Album.class));
		verify(mapper, times(1)).MapFromDTO(Mockito.any(AlbumDTO.class));
		verify(mapper, times(1)).MapToDTO(Mockito.any(Album.class));
	}
	
	@Test
	public void readAlbumsTest() {
		when(repo.findAll()).thenReturn(album);
		when(mapper.MapToDTO(Mockito.any(Album.class))).thenReturn(validAlbumDTO);
		assertThat(albumDTO).isEqualTo(service.read());
		verify(repo, times(1)).findAll();
		verify(mapper, times(1)).MapToDTO(Mockito.any(Album.class));
	}
	
	@Test
	public void readAlbumIdTest() {
		when(repo.findById(validAlbumDTO.getId())).thenReturn(Optional.of(validAlbum));
		when(mapper.MapToDTO(Mockito.any(Album.class))).thenReturn(validAlbumDTO);
		assertThat(validAlbumDTO).isEqualTo(service.read(validAlbumDTO.getId()));
		verify(repo, times(1)).findById(validAlbumDTO.getId());
		verify(mapper, times(1)).MapToDTO(Mockito.any(Album.class));
	}

	@Test
	public void readAlbumNameTest() {
		when(repo.getAlbumByNameJPQL(validAlbumDTO.getName())).thenReturn(validAlbum);
		when(mapper.MapToDTO(Mockito.any(Album.class))).thenReturn(validAlbumDTO);
		assertThat(validAlbumDTO).isEqualTo(service.read(validAlbumDTO.getName()));
		verify(repo, times(1)).getAlbumByNameJPQL(validAlbumDTO.getName());
		verify(mapper, times(1)).MapToDTO(Mockito.any(Album.class));
	}
	
}
