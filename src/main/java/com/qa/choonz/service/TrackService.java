package com.qa.choonz.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.qa.choonz.exception.TrackNotFoundException;
import com.qa.choonz.persistence.domain.Track;
import com.qa.choonz.persistence.repository.TrackRepository;
import com.qa.choonz.rest.dto.TrackDTO;
import com.qa.choonz.utils.mappers.TrackMapper;

@Service
public class TrackService {

	private TrackRepository repo;
	private TrackMapper mapper;

	public TrackService(TrackRepository repo, TrackMapper mapper) {
		super();
		this.repo = repo;
		this.mapper = mapper;
	}

	public TrackDTO map(Track track) {
		// return this.mapper.map(track, TrackDTO.class);
		return mapper.mapToDTO(track);
	}

	public Track map(TrackDTO track) {
		// return this.mapper.map(track, TrackDTO.class);
		return mapper.mapFromDTO(track);
	}

	public TrackDTO create(TrackDTO track) {
		Track created = this.repo.save(map(track));
		return map(created);
	}

	public List<TrackDTO> read() {
		return this.repo.findAll().stream().map(this::map).collect(Collectors.toList());
	}

	public TrackDTO read(long id) {
		Track found = this.repo.findById(id).orElseThrow(TrackNotFoundException::new);
		return this.map(found);
	}

	public TrackDTO read(String name) {
		Track newFound = this.repo.getTrackByNameJPQL(name);
		return this.map(newFound);
	}

	public TrackDTO update(TrackDTO track, long id) {
		Track toUpdate = this.repo.findById(id).orElseThrow(TrackNotFoundException::new);
		toUpdate.setName(track.getName());
		toUpdate.setDuration(track.getDuration());
		toUpdate.setLyrics(track.getLyrics());
		Track updated = this.repo.save(toUpdate);
		return this.map(updated);
	}

	public TrackDTO setAlbumToNull(long id) {
		Track found = this.repo.findById(id).orElseThrow(TrackNotFoundException::new);
		found.setAlbum(null);
		Track updated = this.repo.save(found);
		return this.map(updated);
	}

	public TrackDTO setPlaylistToNull(long id) {
		Track found = this.repo.findById(id).orElseThrow(TrackNotFoundException::new);
		found.setPlaylist(null);
		Track updated = this.repo.save(found);
		return this.map(updated);
	}

	public boolean delete(long id) {
		if(!this.repo.existsById(id)) {
			throw new TrackNotFoundException();
		}
		this.repo.deleteById(id);
		boolean exists = this.repo.existsById(id);
		
		return !exists;
	}

}
