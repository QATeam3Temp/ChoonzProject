package com.qa.choonz.utils.mappers;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qa.choonz.exception.GenreNotFoundException;
import com.qa.choonz.persistence.domain.Album;
import com.qa.choonz.persistence.domain.Genre;
import com.qa.choonz.persistence.repository.AlbumRepository;
import com.qa.choonz.rest.dto.GenreDTO;
@Service
public class GenreMapper {
	 private AlbumRepository repo;
	    @Autowired
	    public GenreMapper(AlbumRepository repo) {
	        super();
	        this.repo = repo;
	    }
	    
	    public GenreDTO MapToDTO(Genre genre){
	    	GenreDTO genreDTO =  new GenreDTO();
	    	genreDTO.setId(genre.getId());
	    	genreDTO.setName(genre.getName());
	    	genreDTO.setDescription(genre.getDescription());
			ArrayList<Long> albums = new ArrayList<Long>();
	        for (Album album : genre.getAlbums()) {
	        	album.setGenre(genre);
	        	repo.save(album);
	        	albums.add(album.getId());
			}
	        genreDTO.setAlbums(albums);
			return genreDTO;
		}
	    
	    public Genre MapFromDTO(GenreDTO genreDTO){
	    	
	    	Genre genre =  new Genre(genreDTO);
	    	genre.setId(genreDTO.getId());
	    	genre.setName(genreDTO.getName());
	    	genre.setDescription(genreDTO.getDescription());
			ArrayList<Album> albums = new ArrayList<Album>();
	        for (Long album : genreDTO.getAlbums()) {
	        	albums.add(repo.findById(album).orElseThrow(GenreNotFoundException::new));
			}
	        genre.setAlbums(albums);
			return genre;
		}
}
