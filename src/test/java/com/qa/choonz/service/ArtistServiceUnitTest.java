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
import com.qa.choonz.persistence.repository.ArtistRepository;
import com.qa.choonz.rest.dto.ArtistDTO;
import com.qa.choonz.utils.mappers.ArtistMapper;

@SpringBootTest
public class ArtistServiceUnitTest {
	
	@Autowired
	private ArtistService service;
	
	@MockBean
	private ArtistRepository repo;
	
	@MockBean
	private ArtistMapper mapper;
	
	private List<Long> emptyList = new ArrayList<Long>();
	private List<Album> albumList = new ArrayList<Album>();
	private List<Artist> artist;
	private List<ArtistDTO> artistDTO;
	
	private Artist validArtist;
	private ArtistDTO validArtistDTO;
	
	@BeforeEach
	public void init() {
		validArtist = new Artist(1, "test", albumList);
		validArtistDTO = new ArtistDTO(1, "test", emptyList);
		artist = new ArrayList<Artist>();
		artistDTO = new ArrayList<ArtistDTO>();
		albumList = new ArrayList<Album>();
		
		artist.add(validArtist);
		artistDTO.add(validArtistDTO);
	}
	
	@Test
	public void createArtistTest() {
		when(repo.save(Mockito.any(Artist.class))).thenReturn(validArtist);
		when(mapper.MapToDTO(Mockito.any(Artist.class))).thenReturn(validArtistDTO);
		when(mapper.MapFromDTO(Mockito.any(ArtistDTO.class))).thenReturn(validArtist);
		assertThat(validArtistDTO).isEqualTo(service.create(validArtistDTO));
		verify(repo, times(1)).save(Mockito.any(Artist.class));
		verify(mapper, times(1)).MapFromDTO(Mockito.any(ArtistDTO.class));
		verify(mapper, times(1)).MapToDTO(Mockito.any(Artist.class));
	}
	
	@Test
	public void readArtistTest() {
		when(repo.findAll()).thenReturn(artist);
		when(mapper.MapToDTO(Mockito.any(Artist.class))).thenReturn(validArtistDTO);
		assertThat(artistDTO).isEqualTo(service.read());
		verify(repo, times(1)).findAll();
		verify(mapper, times(1)).MapToDTO(Mockito.any(Artist.class));
	}
	
	@Test
	public void readArtistIdTest() {
		when(repo.findById(Mockito.anyLong())).thenReturn(Optional.of(validArtist));
		when(mapper.MapToDTO(Mockito.any(Artist.class))).thenReturn(validArtistDTO);
		assertThat(validArtistDTO).isEqualTo(service.read(validArtistDTO.getId()));
		verify(repo, times(1)).findById(validArtistDTO.getId());
		verify(mapper, times(1)).MapToDTO(Mockito.any(Artist.class));
	}

	@Test
	public void readArtistNameTest() {
		when(repo.getArtistByNameJPQL(validArtistDTO.getName())).thenReturn(validArtist);
		when(mapper.MapToDTO(Mockito.any(Artist.class))).thenReturn(validArtistDTO);
		assertThat(validArtistDTO).isEqualTo(service.read(validArtistDTO.getName()));
		verify(repo, times(1)).getArtistByNameJPQL(validArtistDTO.getName());
		verify(mapper, times(1)).MapToDTO(Mockito.any(Artist.class));
	}
	
	@Test
	public void deleteArtistTest() {
		when(repo.existsById(Mockito.anyLong())).thenReturn(true).thenReturn(false);
		
		assertThat(true).isEqualTo(service.delete(validArtist.getId()));
		
		verify(repo, times(2)).existsById(Mockito.anyLong());
		verify(repo, times(1)).deleteById(Mockito.anyLong());
	}
	
}
