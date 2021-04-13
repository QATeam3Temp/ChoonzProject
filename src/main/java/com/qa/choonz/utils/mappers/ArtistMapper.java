package com.qa.choonz.utils.mappers;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qa.choonz.exception.AlbumNotFoundException;
import com.qa.choonz.exception.GenreNotFoundException;
import com.qa.choonz.persistence.domain.Album;
import com.qa.choonz.persistence.domain.Artist;
import com.qa.choonz.persistence.repository.AlbumRepository;
import com.qa.choonz.rest.dto.ArtistDTO;

@Service
public class ArtistMapper {
	private AlbumRepository repo;

	@Autowired
	public ArtistMapper(AlbumRepository repo) {
		super();
		this.repo = repo;
	}

	public ArtistDTO MapToDTO(Artist artist) {
		ArtistDTO artistDTO = new ArtistDTO();
		artistDTO.setId(artist.getId());
		artistDTO.setName(artist.getName());
		ArrayList<Long> albums = new ArrayList<Long>();
		for (Album album : artist.getAlbums()) {
			albums.add(album.getId());
		}
		for (Album album : artist.getFeaturedAlbums()) {
			if(!albums.contains(album.getId())) {
			albums.add(album.getId());
			}
		}
		artistDTO.setAlbums(albums);
		return artistDTO;
	}

	public Artist MapFromDTO(ArtistDTO artistDTO) {

		Artist artist = new Artist(artistDTO);
		artist.setId(artistDTO.getId());
		artist.setName(artistDTO.getName());
		ArrayList<Album> albums = new ArrayList<Album>();
		for (Long album : artistDTO.getAlbums()) {
			albums.add(repo.findById(album).orElseThrow(AlbumNotFoundException::new));
		}
		artist.setAlbums(albums);

		return artist;
	}
}
