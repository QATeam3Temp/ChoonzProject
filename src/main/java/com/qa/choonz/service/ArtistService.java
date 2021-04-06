package com.qa.choonz.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.qa.choonz.exception.ArtistNotFoundException;
import com.qa.choonz.persistence.domain.Artist;
import com.qa.choonz.persistence.repository.ArtistRepository;
import com.qa.choonz.rest.dto.ArtistDTO;
import com.qa.choonz.utils.mappers.ArtistMapper;

@Service
public class ArtistService {

	private ArtistRepository repo;
	private ArtistMapper mapper;

	public ArtistService(ArtistRepository repo, ArtistMapper mapper) {
		super();
		this.repo = repo;
		this.mapper = mapper;
	}

	private ArtistDTO map(Artist artist) {
		return mapper.MapToDTO(artist);
	}

	private Artist map(ArtistDTO artist) {
		return mapper.MapFromDTO(artist);
	}

	public ArtistDTO create(ArtistDTO artist) {
		Artist created = this.repo.save(map(artist));
		return map(created);
	}

	public List<ArtistDTO> read() {
		return this.repo.findAll().stream().map(this::map).collect(Collectors.toList());
	}

	public ArtistDTO read(long id) {
		Artist found = this.repo.findById(id).orElseThrow(ArtistNotFoundException::new);
		return this.map(found);
	}

	public ArtistDTO read(String name) {
		Artist newFound = this.repo.getArtistByNameJPQL(name);
		return this.map(newFound);
	}

	public ArtistDTO update(ArtistDTO artist, long id) {
		Artist toUpdate = this.repo.findById(id).orElseThrow(ArtistNotFoundException::new);
		toUpdate.setName(artist.getName());
		Artist updated = this.repo.save(toUpdate);
		return this.map(updated);
	}

	public boolean delete(long id) {
		this.repo.deleteById(id);
		return !this.repo.existsById(id);
	}
}
