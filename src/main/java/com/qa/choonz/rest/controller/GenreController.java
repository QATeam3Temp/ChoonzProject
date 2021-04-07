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

import com.qa.choonz.rest.dto.GenreDTO;
import com.qa.choonz.service.GenreService;
import com.qa.choonz.utils.UserSecurity;

@RestController
@RequestMapping("/genres")
@CrossOrigin
public class GenreController {

	private GenreService service;

	private UserSecurity security;

	@Autowired
	public GenreController(GenreService service, UserSecurity security) {
		super();
		this.service = service;
		this.security = security;
	}

	@PostMapping("/create")
	public ResponseEntity<GenreDTO> create(@RequestBody GenreDTO genre, @RequestHeader("key") String userKey) {
		if (security.testKey(userKey)) {
			GenreDTO created = this.service.create(genre);
			return new ResponseEntity<GenreDTO>(created, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
	}

	@GetMapping("/read")
	public ResponseEntity<List<GenreDTO>> read() {
		return new ResponseEntity<List<GenreDTO>>(this.service.read(), HttpStatus.OK);
	}

	@GetMapping("/read/id/{id}")
	public ResponseEntity<GenreDTO> read(@PathVariable("id") long id) {
		return new ResponseEntity<GenreDTO>(this.service.read(id), HttpStatus.OK);
	}

	@GetMapping("/read/name/{name}")
	public ResponseEntity<GenreDTO> getGenreByName(@PathVariable("name") String name) {
		return new ResponseEntity<GenreDTO>(this.service.read(name), HttpStatus.OK);
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<GenreDTO> update(@RequestBody GenreDTO genre, @PathVariable long id,
			@RequestHeader("key") String userKey) {
		if (security.testKey(userKey)) {
			return new ResponseEntity<GenreDTO>(this.service.update(genre, id), HttpStatus.ACCEPTED);
		} else {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<GenreDTO> delete(@PathVariable long id, @RequestHeader("key") String userKey) {
		if (security.testKey(userKey)) {
			return this.service.delete(id) ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
					: new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

	}

}
