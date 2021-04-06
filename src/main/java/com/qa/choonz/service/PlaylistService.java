package com.qa.choonz.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.qa.choonz.exception.PlaylistNotFoundException;
import com.qa.choonz.persistence.domain.Playlist;
import com.qa.choonz.persistence.repository.PlaylistRepository;
import com.qa.choonz.rest.dto.PlaylistDTO;
import com.qa.choonz.utils.mappers.PlaylistMapper;

@Service
public class PlaylistService {

    private PlaylistRepository repo;
    private PlaylistMapper mapper;

    public PlaylistService(PlaylistRepository repo, PlaylistMapper mapper) {
        super();
        this.repo = repo;
        this.mapper = mapper;
    }

    private PlaylistDTO map(Playlist playlist) {
        return this.mapper.MapToDTO(playlist);
    }
    private Playlist map(PlaylistDTO playlist) {
        return this.mapper.MapFromDTO(playlist);
    }
    public PlaylistDTO create(PlaylistDTO playlist) {
        Playlist created = this.repo.save(map(playlist));
        return map(created);
    }

    public List<PlaylistDTO> read() {
        return this.repo.findAll().stream().map(this::map).collect(Collectors.toList());
    }
    
    public PlaylistDTO read(String name) {
        Playlist newFound = this.repo.getPlaylistByNameJPQL(name);
        return this.map(newFound);
    }

    public PlaylistDTO read(long id) {
        Playlist found = this.repo.findById(id).orElseThrow(PlaylistNotFoundException::new);
        return this.map(found);
    }

    public PlaylistDTO update(Playlist playlist, long id) {
        Playlist toUpdate = this.repo.findById(id).orElseThrow(PlaylistNotFoundException::new);
        toUpdate.setName(toUpdate.getName());
        toUpdate.setDescription(toUpdate.getDescription());
        toUpdate.setArtwork(toUpdate.getArtwork());
        toUpdate.setTracks(toUpdate.getTracks());
        Playlist updated = this.repo.save(toUpdate);
        return this.map(updated);
    }

    public boolean delete(long id) {
        this.repo.deleteById(id);
        return !this.repo.existsById(id);
    }

}
