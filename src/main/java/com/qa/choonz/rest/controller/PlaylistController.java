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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qa.choonz.rest.dto.PlaylistDTO;
import com.qa.choonz.service.PlaylistService;
import com.qa.choonz.utils.UserSecurity;

@RestController
@RequestMapping("/playlists")
@CrossOrigin
public class PlaylistController {

	private PlaylistService service;

	private UserSecurity security;

	public PlaylistController(PlaylistService service, UserSecurity security) {
		super();
		this.service = service;
		this.security = security;
	}

	@PostMapping("/create")
	public ResponseEntity<PlaylistDTO> create(@RequestBody PlaylistDTO playlist, @RequestHeader("key") String userKey) {
		if (security.testKey(userKey)) {
			PlaylistDTO created = this.service.create(playlist);
			return new ResponseEntity<PlaylistDTO>(created, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
	}

	@GetMapping("/read")
	public ResponseEntity<List<PlaylistDTO>> read() {
		return new ResponseEntity<List<PlaylistDTO>>(this.service.read(), HttpStatus.OK);
	}

	@GetMapping("/read/id/{id}")
	public ResponseEntity<PlaylistDTO> read(@PathVariable("id") long id) {
		return new ResponseEntity<PlaylistDTO>(this.service.read(id), HttpStatus.OK);
	}

	@GetMapping("/read/name/{name}")
	public ResponseEntity<PlaylistDTO> getPlaylistByName(@PathVariable("name") String name) {
		return new ResponseEntity<PlaylistDTO>(this.service.read(name), HttpStatus.OK);
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<PlaylistDTO> update(@RequestBody PlaylistDTO playlist, @PathVariable long id,
			@RequestHeader("key") String userKey) {
		if (security.testKey(userKey)) {
			return new ResponseEntity<PlaylistDTO>(this.service.update(playlist, id), HttpStatus.ACCEPTED);
		} else {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Boolean> delete(@PathVariable long id, @RequestHeader("key") String userKey) {
		if (security.testKey(userKey)) {
			return this.service.delete(id) ? new ResponseEntity<>(this.service.delete(id), HttpStatus.NO_CONTENT)
					: new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
	}

}
