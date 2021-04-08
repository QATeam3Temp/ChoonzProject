package com.qa.choonz.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.qa.choonz.persistence.domain.Album;
import com.qa.choonz.persistence.domain.Artist;
import com.qa.choonz.persistence.repository.AlbumRepository;
import com.qa.choonz.persistence.repository.ArtistRepository;
import com.qa.choonz.rest.dto.ArtistDTO;

@SpringBootTest
public class ArtistServiceIntegrationTest {

	@Autowired
	private AlbumRepository aRepo;
	
	@Autowired
	private ArtistRepository repo;
	
	@Autowired
	private ArtistService service;
	
	private List<Long> emptyList = new ArrayList<Long>();
	private List<Artist> artist = new ArrayList<Artist>();
	private List<ArtistDTO> artistDTO = new ArrayList<ArtistDTO>();
	private Album validAlbum = new Album(1, "test", null, null, null, "test");
	private List<Album> validAlbums = new ArrayList<Album>();
	private Artist validArtist;
	private ArtistDTO validArtistDTO;
	
	@BeforeEach
	public void init() {
		repo.deleteAll();
		validAlbum = aRepo.save(validAlbum);
		validArtist = new Artist(1, "test", validAlbums);
		artist = new ArrayList<Artist>();
		artistDTO = new ArrayList<ArtistDTO>();
		validArtist = repo.save(validArtist);
		validArtistDTO = service.map(validArtist);
		artist.add(validArtist);
		artistDTO.add(validArtistDTO);
	}
	
	@Test
	public void createArtistTest() {
		ArtistDTO newArtist = new ArtistDTO("test2");
		ArtistDTO expectedArtist = new ArtistDTO(validArtist.getId()+1, "test2", emptyList);
		assertThat(expectedArtist).isEqualTo(service.create(newArtist));
	}
	
	@Test
	public void readAllArtistsTest() {
		List<ArtistDTO> artistInDb = service.read();
		assertThat(artistDTO).isEqualTo(artistInDb);
	}
	
	@Test
	public void readArtistIdTest() {
		assertThat(validArtistDTO).isEqualTo(service.read(validArtist.getId()));
	}
	
	@Test
	public void readArtistNameTest() {
		assertThat(validArtistDTO).isEqualTo(service.read(validArtist.getName()));
	}
	
	@Test
	public void updateArtistTest() {
		ArtistDTO sentArtist = new ArtistDTO("updated");
		ArtistDTO responseArtist = new ArtistDTO(validArtist.getId(), "updated", emptyList);
		ArtistDTO updatedAritst = service.update(sentArtist, validArtist.getId());
		
		assertThat(responseArtist).isEqualTo(updatedAritst);
	}
	
	@Test
	public void deleteArtistTest() {
		assertThat(true).isEqualTo(service.delete(validArtist.getId()));
	}

}
