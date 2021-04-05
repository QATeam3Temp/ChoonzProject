package com.qa.choonz.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.qa.choonz.persistence.domain.Artist;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {
	
	@Query("SELECT a FROM Artist a WHERE a.name = ?1")
	public Artist getArtistByNameJPQL(String name);
	
}
