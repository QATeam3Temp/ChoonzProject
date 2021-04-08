package com.qa.choonz.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.qa.choonz.rest.dto.AlbumDTO;
import com.qa.choonz.rest.dto.ArtistDTO;
import com.qa.choonz.service.ArtistService;
import com.qa.choonz.utils.UserSecurity;

import net.bytebuddy.asm.Advice.This;

@RestController
@RequestMapping("/artists")
@CrossOrigin
public class ArtistController {

	private ArtistService service;

	private UserSecurity security;

	@Autowired
	public ArtistController(ArtistService service, UserSecurity security) {
		super();
		this.service = service;
		this.security = security;
	}

	@PostMapping("/create")
	public ResponseEntity<ArtistDTO> create(@RequestBody ArtistDTO artist, @RequestHeader("key") String userKey) {
		if (security.testKey(userKey)) {
		ArtistDTO created = this.service.create(artist);
		return new ResponseEntity<ArtistDTO>(created, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
	}

	@GetMapping("/read")
	public ResponseEntity<List<ArtistDTO>> read() {
		return new ResponseEntity<List<ArtistDTO>>(this.service.read(), HttpStatus.OK);
	}

	@GetMapping("/read/id/{id}")
	public ResponseEntity<ArtistDTO> read(@PathVariable("id") long id) {
		return new ResponseEntity<ArtistDTO>(this.service.read(id), HttpStatus.OK);
	}

	@GetMapping("/read/name/{name}")
	public ResponseEntity<ArtistDTO> read(@PathVariable("name") String name) {
		return new ResponseEntity<ArtistDTO>(this.service.read(name), HttpStatus.OK);
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<ArtistDTO> update(@RequestBody ArtistDTO artist, @PathVariable long id, @RequestHeader("key") String userKey) {
		if (security.testKey(userKey)) {
		return new ResponseEntity<ArtistDTO>(this.service.update(artist, id), HttpStatus.ACCEPTED);
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
