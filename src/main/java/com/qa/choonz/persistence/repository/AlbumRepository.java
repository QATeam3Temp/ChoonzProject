package com.qa.choonz.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.qa.choonz.persistence.domain.Album;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {

	@Query("SELECT a FROM Album a WHERE a.name = ?1")
	public Album getAlbumByNameJPQL(String name);

	@Query("SELECT a FROM Album a WHERE a.id = ?1")
	public Album getAlbumByIdJPQL(long id);
	
	@Query(value = "SELECT * FROM Album WHERE Album.ARTIST_ID = ?1" , nativeQuery=true)
	public List<Album> getAlbumByArtistSQL(long id);
	@Query(value = "SELECT * FROM Album WHERE Album.GENRE_ID = ?1" , nativeQuery=true)
	public List<Album> getAlbumByGenreSQL(long id);
}