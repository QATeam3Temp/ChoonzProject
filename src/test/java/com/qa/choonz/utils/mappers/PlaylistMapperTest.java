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

import com.qa.choonz.persistence.domain.Playlist;
import com.qa.choonz.persistence.domain.Track;
import com.qa.choonz.persistence.repository.TrackRepository;
import com.qa.choonz.rest.dto.PlaylistDTO;

@SpringBootTest
public class PlaylistMapperTest {

	@Autowired
	private PlaylistMapper mapper;

	@MockBean
	private TrackRepository tRepo;
	Track validTrack = new Track();
	Playlist validPlaylist = new Playlist(1, "Running songs", "Mostly Eurobeat", "a picture of a treadmill",
			List.of(validTrack));
	PlaylistDTO validPlaylistDTO = new PlaylistDTO(1, "Running songs", "Mostly Eurobeat", "a picture of a treadmill",
			List.of(0L));

	@Test
	void mapToDTOTest() {
		Assertions.assertEquals(validPlaylistDTO, mapper.MapToDTO(validPlaylist));

	}

	@Test
	void mapFromDTOTest() {

		when(tRepo.getTrackByPlaylistSQL(Mockito.anyLong())).thenReturn(List.of(validTrack));
		Assertions.assertEquals(validPlaylist, mapper.MapFromDTO(validPlaylistDTO));

		verify(tRepo, times(1)).getTrackByPlaylistSQL(Mockito.anyLong());
	}

}
