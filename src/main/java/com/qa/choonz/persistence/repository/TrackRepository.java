package com.qa.choonz.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.qa.choonz.persistence.domain.Album;
import com.qa.choonz.persistence.domain.Track;

@Repository
public interface TrackRepository extends JpaRepository<Track, Long> {

	@Query("SELECT t FROM Track t WHERE t.name = ?1")
	public Track getTrackByNameJPQL(String name);
	
	@Query(value = "SELECT * FROM Track WHERE Track.ALBUM_ID = ?1" , nativeQuery=true)
	public List<Track> getTrackByAlbumSQL(long id);
	
	@Query(value = "SELECT * FROM Track WHERE Track.Playlist_ID = ?1" , nativeQuery=true)
	public List<Track> getTrackByPlaylistSQL(long id);
}
