package com.qa.choonz.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qa.choonz.exception.AlbumNotFoundException;
import com.qa.choonz.persistence.domain.Album;
import com.qa.choonz.persistence.repository.AlbumRepository;
import com.qa.choonz.rest.dto.AlbumDTO;
import com.qa.choonz.utils.mappers.AlbumMapper;

@Service
public class AlbumService {

    private AlbumRepository repo;
    private AlbumMapper mapper;
    @Autowired
    public AlbumService(AlbumRepository repo, AlbumMapper mapper) {
        super();
        this.repo = repo;
        this.mapper = mapper;
    }

    private AlbumDTO map(Album album) {
    	return mapper.MapToDTO(album);
    	//return new AlbumDTO(album);
       // return this.mapper.map(album, AlbumDTO.class);
    }
    private Album map(AlbumDTO album) {
    	return mapper.MapFromDTO(album);

    }
    public AlbumDTO create(AlbumDTO album) {
        Album created = this.repo.save(map(album));
        return map(created);
    }
/*
    public List<AlbumDTO> read() {
    	List<Album> album = repo.findAll();
    	List<AlbumDTO> albumDTO = new ArrayList<AlbumDTO>();
    	album.forEach(a -> albumDTO.add(mapper.mapToDTO(a)));
    	return albumDTO;
    }
    
    public AlbumDTO read(long id) {
    	Album album = repo.getAlbumByIdJPQL(id);
    	return mapper.mapToDTO(album);
    }
    */
    
    public List<AlbumDTO> read() {
        return this.repo.findAll().stream().map(this::map).collect(Collectors.toList());
    }

    public AlbumDTO read(long id) {
        Album found = this.repo.findById(id).orElseThrow(AlbumNotFoundException::new);
        return this.map(found);
    }
    
    public AlbumDTO read(String name) {
        Album newFound = this.repo.getAlbumByNameJPQL(name);
        return this.map(newFound);
    }

    public AlbumDTO update(Album album, long id) {
        Album toUpdate = this.repo.findById(id).orElseThrow(AlbumNotFoundException::new);
        toUpdate.setName(toUpdate.getName());
        toUpdate.setTracks(toUpdate.getTracks());
        toUpdate.setArtist(toUpdate.getArtist());
        toUpdate.setCover(toUpdate.getCover());
        Album updated = this.repo.save(toUpdate);
        return this.map(updated);
    }

    public boolean delete(long id) {
        this.repo.deleteById(id);
        return !this.repo.existsById(id);
    }

}
