package com.qa.choonz.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.qa.choonz.exception.ArtistNotFoundException;
import com.qa.choonz.persistence.domain.Artist;
import com.qa.choonz.persistence.repository.ArtistRepository;
import com.qa.choonz.rest.dto.ArtistDTO;

@Service
public class ArtistService {

    private ArtistRepository repo;
    private ModelMapper mapper;

    public ArtistService(ArtistRepository repo, ModelMapper mapper) {
        super();
        this.repo = repo;
        this.mapper = mapper;
    }

    private ArtistDTO mapToDTO(Artist artist) {
        return this.mapper.map(artist, ArtistDTO.class);
    }

    public ArtistDTO create(ArtistDTO artist) {
        Artist created = this.repo.save(new Artist(artist));
        return new ArtistDTO(created);
    }

    public List<ArtistDTO> read() {
        return this.repo.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public ArtistDTO read(long id) {
        Artist found = this.repo.findById(id).orElseThrow(ArtistNotFoundException::new);
        return this.mapToDTO(found);
    }
    
    public ArtistDTO read(String name) {
        Artist newFound = this.repo.getArtistByNameJPQL(name);
        return this.mapToDTO(newFound);
    }

    public ArtistDTO update(ArtistDTO artist, long id) {
        Artist toUpdate = this.repo.findById(id).orElseThrow(ArtistNotFoundException::new);
        toUpdate.setName(artist.getName());
        toUpdate.setAlbums(artist.getAlbums());
        Artist updated = this.repo.save(toUpdate);
        return this.mapToDTO(updated);
    }

    public boolean delete(long id) {
        this.repo.deleteById(id);
        return !this.repo.existsById(id);
    }
}
