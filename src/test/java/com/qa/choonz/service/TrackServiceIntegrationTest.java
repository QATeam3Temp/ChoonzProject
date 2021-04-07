package com.qa.choonz.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.qa.choonz.persistence.domain.Album;
import com.qa.choonz.persistence.domain.Playlist;
import com.qa.choonz.persistence.domain.Track;
import com.qa.choonz.persistence.repository.AlbumRepository;
import com.qa.choonz.persistence.repository.ArtistRepository;
import com.qa.choonz.persistence.repository.PlaylistRepository;
import com.qa.choonz.persistence.repository.TrackRepository;
import com.qa.choonz.rest.dto.AlbumDTO;
import com.qa.choonz.rest.dto.TrackDTO;

@SpringBootTest
public class TrackServiceIntegrationTest {

	@Autowired
	private TrackService service;

	@Autowired
	private TrackRepository repo;
	
	@Autowired
	private AlbumRepository aRepo;
	
	@Autowired
	private PlaylistRepository pRepo;
	
	private List<Track> track;
	private List<TrackDTO> trackDTO;

	private Track validTrack;
	private TrackDTO validTrackDTO;
	private Album validAlbum = new Album(1,"Greatest Hits",track,null,null,"A large 2");
	private Playlist validPlaylist = new Playlist(1,"Running songs","Primarily eurobeat","A pair of legs moving",track);

	@BeforeEach
	public void init() {
		validAlbum = aRepo.save(validAlbum);
		validPlaylist = pRepo.save(validPlaylist);
		validTrack = new Track(1, "test", validAlbum, validPlaylist, 1000, "test");
		track = new ArrayList<Track>();
		trackDTO = new ArrayList<TrackDTO>();
		repo.deleteAll();
		validTrack = repo.save(validTrack);
		validTrackDTO = service.map(validTrack);
		track.add(validTrack);
		trackDTO.add(validTrackDTO);
	}

	@Test
	public void createTest() {
		TrackDTO newTrack = new TrackDTO("test2", 1000, "test2");
		TrackDTO expectedTrack = new TrackDTO(validTrack.getId() + 1, "test2", 0L, 0L, 1000, "test2");
		assertThat(expectedTrack).isEqualTo(service.create(newTrack));

	}

	@Test
	public void readAllTest() {
		List<TrackDTO> trackInDb = service.read();
		assertThat(trackDTO).isEqualTo(trackInDb);
	}

	@Test
	public void readIdTest() {
		assertThat(validTrackDTO).isEqualTo(service.read(validTrack.getId()));
	}

	@Test
	public void readNameTest() {
		assertThat(validTrackDTO).isEqualTo(service.read(validTrack.getName()));
	}

	@Test
	public void readByAlbumTest() {
		assertThat(trackDTO).isEqualTo(service.readByAlbum(validTrackDTO.getAlbum()));
	}
	
	@Test
	public void readByPlaylistTest() {
		assertThat(trackDTO).isEqualTo(service.readByPlaylist(validTrackDTO.getPlaylist()));
	}
	
	@Test
	public void deleteTrackTest() {
		boolean deleteTrack = service.delete(validTrack.getId());
		
		assertThat(deleteTrack).isEqualTo(true);
	}
	

	@Test
	public void updateTrackTest() {
		TrackDTO sentTrack = new TrackDTO("updateTest", 5000, "heheheh");
		TrackDTO responseTrack = new TrackDTO(validTrack.getId(), "updateTest", 1L, 1L, 5000, "heheheh");
		TrackDTO updatedTrack = service.update(sentTrack, validTrack.getId());
		
		assertThat(responseTrack).isEqualTo(updatedTrack);
	}
	
	@Test
	public void setPlaylistToNullTest() {
		TrackDTO expectedTrack = new TrackDTO(validTrack.getId(), "test", 1L, 0L, 1000, "test");
		TrackDTO responseTrack = service.setPlaylistToNull(validTrack.getId());
		
		assertThat(responseTrack).isEqualTo(expectedTrack);
		
	}
	
}
