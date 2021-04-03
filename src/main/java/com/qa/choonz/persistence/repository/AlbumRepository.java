package com.qa.choonz.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.qa.choonz.persistence.domain.Album;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
	
	@Query("SELECT a FROM Album a WHERE a.name = ?1")
	public Album getAlbumByNameJPQL(String name);

}
