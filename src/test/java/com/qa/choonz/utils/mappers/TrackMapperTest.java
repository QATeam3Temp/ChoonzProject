package com.qa.choonz.utils.mappers;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.qa.choonz.persistence.domain.Album;
import com.qa.choonz.persistence.domain.Playlist;
import com.qa.choonz.persistence.domain.Track;
import com.qa.choonz.persistence.repository.AlbumRepository;
import com.qa.choonz.persistence.repository.PlaylistRepository;
import com.qa.choonz.rest.dto.TrackDTO;

@SpringBootTest
public class TrackMapperTest {

	@Autowired
	private TrackMapper trackMapper;
	
	@MockBean
	private AlbumRepository aRepo;
	
	@MockBean
    private PlaylistRepository pRepo;
	Album validAlbum = new Album();
	Playlist validPlaylist = new Playlist();
	TrackDTO validTrackDTO = new TrackDTO(1,"Boom Boom Boom Boom",0L,0L,240,"I want you in my room");
	Track validTrack = new Track(1,"Boom Boom Boom Boom",validAlbum,validPlaylist,240,"I want you in my room");
	@Test
	void mapToDTOTest() {
		Assertions.assertEquals(validTrackDTO, trackMapper.mapToDTO(validTrack));
		
	}
	@Test
	void mapFromDTOTest() {
	when(aRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(validAlbum));
	when(pRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(validPlaylist));
		Assertions.assertEquals(validTrack, trackMapper.mapFromDTO(validTrackDTO));
		verify(aRepo, times(1)).findById(Mockito.anyLong());
		verify(pRepo, times(1)).findById(Mockito.anyLong());
	}
	
}
