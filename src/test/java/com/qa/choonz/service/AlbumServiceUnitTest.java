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
public class AlbumServiceUnitTest {

	@Autowired
	private AlbumService service;

	@MockBean
	private AlbumRepository repo;

	private List<Album> album;
	private List<AlbumDTO> albumDTO;

	private Track validTrack;
	private Genre validGenre;
	private Artist validArtist;
	private Album validAlbum;
	private AlbumDTO validAlbumDTO;

	@BeforeEach
	public void init() {
		validGenre = new Genre();
		validArtist = new Artist();
		validTrack = new Track();
		validAlbum = new Album();
		validAlbumDTO = new AlbumDTO();

		album = new ArrayList<Album>();
		albumDTO = new ArrayList<AlbumDTO>();

		album.add(validAlbum);
		albumDTO.add(validAlbumDTO);
	}

	@Test
	public void createAlbumTest() {

	}

}
