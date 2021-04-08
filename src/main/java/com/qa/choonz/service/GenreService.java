package com.qa.choonz.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.qa.choonz.exception.GenreNotFoundException;
import com.qa.choonz.persistence.domain.Genre;
import com.qa.choonz.persistence.repository.GenreRepository;
import com.qa.choonz.rest.dto.GenreDTO;
import com.qa.choonz.utils.mappers.GenreMapper;

@Service
public class GenreService {

	private GenreRepository repo;
	private GenreMapper mapper;

	public GenreService(GenreRepository repo, GenreMapper mapper) {
		super();
		this.repo = repo;
		this.mapper = mapper;
	}

	public GenreDTO map(Genre genre) {
		return this.mapper.MapToDTO(genre);
	}

	public Genre map(GenreDTO genre) {
		return this.mapper.MapFromDTO(genre);
	}

	public GenreDTO create(GenreDTO genre) {
		Genre created = this.repo.save(map(genre));
		return map(created);
	}

	public List<GenreDTO> read() {
		return this.repo.findAll().stream().map(this::map).collect(Collectors.toList());
	}

	public GenreDTO read(long id) {
		Genre found = this.repo.findById(id).orElseThrow(GenreNotFoundException::new);
		return this.map(found);
	}

	public GenreDTO read(String name) {
		Genre newFound = this.repo.getGenreByNameJPQL(name);
		return this.map(newFound);
	}

	public GenreDTO update(GenreDTO genre, long id) {
		Genre toUpdate = this.repo.findById(id).orElseThrow(GenreNotFoundException::new);
		toUpdate.setName(genre.getName());
		toUpdate.setDescription(genre.getDescription());
		toUpdate.setAlbums(map(genre).getAlbums());
		Genre updated = this.repo.save(toUpdate);
		return this.map(updated);
	}

	public boolean delete(long id) {
		this.repo.deleteById(id);
		return !this.repo.existsById(id);
	}

}
