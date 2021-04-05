package com.qa.choonz.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.qa.choonz.persistence.domain.Playlist;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {

	@Query("SELECT p FROM Playlist p WHERE p.name = ?1")
	public Playlist getPlaylistByNameJPQL(String name);
	
}
