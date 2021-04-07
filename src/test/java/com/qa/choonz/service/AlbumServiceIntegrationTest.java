package com.qa.choonz.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.qa.choonz.persistence.domain.Album;
import com.qa.choonz.persistence.domain.Artist;
import com.qa.choonz.persistence.domain.Genre;
import com.qa.choonz.persistence.domain.Track;
import com.qa.choonz.persistence.repository.AlbumRepository;
import com.qa.choonz.persistence.repository.ArtistRepository;
import com.qa.choonz.persistence.repository.GenreRepository;
import com.qa.choonz.persistence.repository.TrackRepository;
import com.qa.choonz.rest.dto.AlbumDTO;

@SpringBootTest
public class AlbumServiceIntegrationTest {

	@Autowired
	private AlbumService service;

	@Autowired
	private AlbumRepository repo;

	@Autowired
	private GenreRepository gRepo;

	@Autowired
	private TrackRepository tRepo;
	@Autowired
	private ArtistRepository aRepo;
	

	
	private List<Album> album = new ArrayList<Album>();
	private List<AlbumDTO> albumDTO = new ArrayList<AlbumDTO>();
	private Track validTrack = new Track(1, "test", 1000, "test");
	private Genre validGenre = new Genre(1,"test","test",album);
	private Artist validArtist = new Artist(1,"test",album);
	private List<Track> validTracks = new ArrayList<Track>();
	private Album validAlbum;
	private AlbumDTO validAlbumDTO;

	@BeforeEach
	public void init() {
		repo.deleteAll();
		validArtist = aRepo.save(validArtist);
		validGenre = gRepo.save(validGenre);
		validAlbum = new Album(1, "test", validTracks, validArtist, validGenre, "test");
		album = new ArrayList<Album>();
		albumDTO = new ArrayList<AlbumDTO>();
		validAlbum = repo.save(validAlbum);
		validTrack.setAlbum(validAlbum);
		validTrack.setPlaylist(null);
		validTrack = tRepo.save(validTrack);
		validAlbum = repo.save(validAlbum);
		validAlbumDTO = service.map(validAlbum);
		album.add(validAlbum);
		albumDTO.add(validAlbumDTO);
	}

	@Test
	public void createAlbumTest() {

	}

}
