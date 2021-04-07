package com.qa.choonz.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.qa.choonz.persistence.domain.Album;
import com.qa.choonz.persistence.domain.Artist;
import com.qa.choonz.persistence.domain.Genre;
import com.qa.choonz.persistence.domain.Track;
import com.qa.choonz.persistence.repository.AlbumRepository;
import com.qa.choonz.rest.dto.AlbumDTO;

@SpringBootTest
public class AlbumServiceIntegrationTest {

	@Autowired
	private AlbumService service;

	@MockBean
	private AlbumRepository repo;

	private Track validTrack = new Track();
	private Genre validGenre = new Genre();
	private Artist validArtist = new Artist();
	private List<Track> validTracks = List.of(validTrack);
	
	private List<Album> album;
	private List<AlbumDTO> albumDTO;

	private Album validAlbum;
	private AlbumDTO validAlbumDTO;

	@BeforeEach
	public void init() {
		repo.deleteAll();
		
		validAlbum = new Album(1, "test", validTracks, validArtist, validGenre, "test");
		album = new ArrayList<Album>();
		albumDTO = new ArrayList<AlbumDTO>();
		
		validAlbum = repo.save(validAlbum);
		System.out.println(validAlbum);
		validAlbumDTO = service.map(validAlbum);
		album.add(validAlbum);
		albumDTO.add(validAlbumDTO);
	}

	@Test
	public void createAlbumTest() {

	}

}
