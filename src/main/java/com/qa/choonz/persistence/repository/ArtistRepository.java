package com.qa.choonz.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.qa.choonz.persistence.domain.Artist;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {

	@Query("SELECT a FROM Artist a WHERE a.name = ?1")
	public Artist getArtistByNameJPQL(String name);
	@Query(value = "SELECT a.id,a.name FROM Artist a left outer join album l on l.artist_id=a.id where l.id = ?1",nativeQuery = true)
	public List<Artist> getArtistByArtistJPQL(Long album);
}
