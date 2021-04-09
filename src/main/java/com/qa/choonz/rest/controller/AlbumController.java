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
import com.qa.choonz.service.AlbumService;
import com.qa.choonz.utils.UserSecurity;

@RestController
@RequestMapping("/albums")
@CrossOrigin
public class AlbumController {

	private AlbumService service;

	private UserSecurity security;
	

	@Autowired
	public AlbumController(AlbumService service, UserSecurity security) {

		super();
		this.service = service;
		this.security = security;
	}

	@PostMapping("/create")
	public ResponseEntity<AlbumDTO> create(@RequestBody AlbumDTO album, @RequestHeader("key") String userKey) {
		if(album.getTracks() == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		if (security.testKey(userKey)) {
			return new ResponseEntity<AlbumDTO>(this.service.create(album), HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
	}

	@GetMapping("/read")
	public ResponseEntity<List<AlbumDTO>> read() {
		return new ResponseEntity<List<AlbumDTO>>(this.service.read(), HttpStatus.OK);
	}

	@GetMapping("/read/id/{id}")
	public ResponseEntity<AlbumDTO> read(@PathVariable("id") long id) {
		return new ResponseEntity<AlbumDTO>(this.service.read(id), HttpStatus.OK);
	}

	@GetMapping("/read/artist/{id}")
	public ResponseEntity<List<AlbumDTO>> readByArtist(@PathVariable("id") long id) {
		return new ResponseEntity<>(this.service.readByArtist(id), HttpStatus.OK);
	}

	@GetMapping("/read/genre/{id}")
	public ResponseEntity<List<AlbumDTO>> readByGenre(@PathVariable("id") long id) {
		return new ResponseEntity<>(this.service.readByGenre(id), HttpStatus.OK);
	}

	@GetMapping("/read/name/{name}")
	public ResponseEntity<AlbumDTO> getAlbumByName(@PathVariable("name") String name) {
		return new ResponseEntity<AlbumDTO>(this.service.read(name), HttpStatus.OK);
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<AlbumDTO> update(@RequestBody AlbumDTO album, @PathVariable long id,
			@RequestHeader("key") String userKey) {
		if (security.testKey(userKey)) {
			return new ResponseEntity<AlbumDTO>(this.service.update(album, id), HttpStatus.ACCEPTED);
		} else {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
	}
	
	
	@PutMapping("/update/artist/{id}")
	public ResponseEntity<List<AlbumDTO>> removeArtist(@PathVariable long id, @RequestHeader("key") String userKey) {
		if (security.testKey(userKey)) {
		return new ResponseEntity<>(this.service.readByArtist(id), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
	}

	@PutMapping("/update/genre/{id}")
	public ResponseEntity<List<AlbumDTO>> removeGenre(@PathVariable("id") long id, @RequestHeader("key") String userKey) {
		if (security.testKey(userKey)) {
		return new ResponseEntity<>(this.service.readByGenre(id), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
	}
	
	
	@DeleteMapping("delete/{id}")
	public ResponseEntity<Boolean> delete(@PathVariable long id, @RequestHeader("key") String userKey) {
		if (security.testKey(userKey)) {
			return new ResponseEntity<Boolean>(this.service.delete(id), HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

	}

}
