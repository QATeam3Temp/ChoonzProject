package com.qa.choonz.rest.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qa.choonz.rest.dto.TrackDTO;
import com.qa.choonz.service.TrackService;

@RestController
@RequestMapping("/tracks")
@CrossOrigin
public class TrackController {

	private TrackService service;

	public TrackController(TrackService service) {
		super();
		this.service = service;
	}

	@PostMapping("/create")
	public ResponseEntity<TrackDTO> create(@RequestBody TrackDTO track) {
		TrackDTO created = this.service.create(track);
		return new ResponseEntity<TrackDTO>(created, HttpStatus.CREATED);
	}

	@GetMapping("/read")
	public ResponseEntity<List<TrackDTO>> read() {
		return new ResponseEntity<List<TrackDTO>>(this.service.read(), HttpStatus.OK);
	}

	@GetMapping("/read/id/{id}")
	public ResponseEntity<TrackDTO> read(@PathVariable("id") long id) {
		return new ResponseEntity<TrackDTO>(this.service.read(id), HttpStatus.OK);
	}

	@GetMapping("/read/name/{name}")
	public ResponseEntity<TrackDTO> getTrackByName(@PathVariable("name") String name) {
		return new ResponseEntity<TrackDTO>(this.service.read(name), HttpStatus.OK);
	}

	@GetMapping("/read/album/{id}")
	public  ResponseEntity<List<TrackDTO>> getTrackByAlbum(@PathVariable("id") long id) {
		return new ResponseEntity<>(this.service.readByAlbum(id), HttpStatus.OK);
	}
	
	@GetMapping("/read/playlist/{id}")
	public  ResponseEntity<List<TrackDTO>> getTrackByPlaylist(@PathVariable("id") long id) {
		return new ResponseEntity<>(this.service.readByPlaylist(id), HttpStatus.OK);
	}
	@PutMapping("/update/{id}")
	public ResponseEntity<TrackDTO> update(@RequestBody TrackDTO track, @PathVariable long id) {
		return new ResponseEntity<TrackDTO>(this.service.update(track, id), HttpStatus.ACCEPTED);
	}

	@PutMapping("/update/album/{id}")
	public ResponseEntity<TrackDTO> setAlbumToNull(@PathVariable long id) {
		return new ResponseEntity<TrackDTO>(this.service.setAlbumToNull(id), HttpStatus.ACCEPTED);
	}

	@PutMapping("/update/playlist/{id}")
	public ResponseEntity<TrackDTO> setPlaylistToNull(@PathVariable long id) {
		return new ResponseEntity<TrackDTO>(this.service.setPlaylistToNull(id), HttpStatus.ACCEPTED);
	}

	@DeleteMapping("delete/{id}")
	public ResponseEntity<TrackDTO> delete(@PathVariable long id) {
		return this.service.delete(id) ? new ResponseEntity<TrackDTO>(HttpStatus.NO_CONTENT)
				: new ResponseEntity<TrackDTO>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
