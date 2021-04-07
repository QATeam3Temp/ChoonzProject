package com.qa.choonz.service;

import java.util.List;
import java.util.stream.Collectors;

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

	public AlbumDTO map(Album album) {
		return mapper.MapToDTO(album);
		// return new AlbumDTO(album);
		// return this.mapper.map(album, AlbumDTO.class);
	}

	public Album map(AlbumDTO album) {
		return mapper.MapFromDTO(album);

	}

	public AlbumDTO create(AlbumDTO album) {
		Album created = this.repo.save(map(album));
		return map(created);
	}

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

	public AlbumDTO update(AlbumDTO album, long id) {
		Album toUpdate = this.repo.findById(id).orElseThrow(AlbumNotFoundException::new);
		toUpdate.setName(album.getName());
		toUpdate.setTracks(map(album).getTracks());
		toUpdate.setArtist(map(album).getArtist());
		toUpdate.setCover(album.getCover());
		Album updated = this.repo.save(toUpdate);
		return this.map(updated);
	}

	public boolean delete(long id) {
		this.repo.deleteById(id);
		return !this.repo.existsById(id);
	}

	public List<AlbumDTO> readByArtist(long id) {
		 return this.repo.getAlbumByArtistSQL(id).stream().map(this::map).collect(Collectors.toList());
	}

}
