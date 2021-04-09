package com.qa.choonz.utils.mappers;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.qa.choonz.persistence.domain.Album;
import com.qa.choonz.persistence.domain.Artist;
import com.qa.choonz.persistence.domain.Genre;
import com.qa.choonz.persistence.domain.Track;
import com.qa.choonz.persistence.repository.ArtistRepository;
import com.qa.choonz.persistence.repository.GenreRepository;
import com.qa.choonz.persistence.repository.TrackRepository;
import com.qa.choonz.rest.dto.AlbumDTO;

@SpringBootTest
public class AlbumMapperTest {
	@Autowired
	private AlbumMapper albumMapper;

	@MockBean
	private TrackRepository tRepo;
	@MockBean
	private ArtistRepository aRepo;
	@MockBean
	private GenreRepository gRepo;

	Track validTrack = new Track();
	Genre validGenre = new Genre();
	Artist validArtist = new Artist();
	List<Track> validTracks = List.of(validTrack);
	AlbumDTO validAlbumDTO = new AlbumDTO(1, "Under The Covers", List.of(0L), 0L, 0L, "Man in douvet");
	Album validAlbum = new Album(1, "Under The Covers", validTracks, null, null, "Man in douvet");

	@Test
	void mapToDTOTest() {
		Assertions.assertEquals(validAlbumDTO, albumMapper.MapToDTO(validAlbum));

	}

	@Test
	void mapFromDTOTest() {
		when(aRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(validArtist));
		when(gRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(validGenre));
		when(tRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(validTrack));
		Assertions.assertEquals(validAlbum, albumMapper.MapFromDTO(validAlbumDTO));
		verify(tRepo, times(1)).findById(Mockito.anyLong());
	}
}
