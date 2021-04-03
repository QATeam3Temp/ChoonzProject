package com.qa.choonz.rest.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qa.choonz.persistence.domain.Playlist;
import com.qa.choonz.rest.dto.PlaylistDTO;
import com.qa.choonz.service.PlaylistService;

@RestController
@RequestMapping("/playlists")
@CrossOrigin
public class PlaylistController {

    private PlaylistService service;

    public PlaylistController(PlaylistService service) {
        super();
        this.service = service;
    }

    @PostMapping("/create")
    public ResponseEntity<PlaylistDTO> create(@RequestBody PlaylistDTO playlist) {
    	PlaylistDTO created = this.service.create(playlist);
    	System.out.println(created);
        return new ResponseEntity<PlaylistDTO>(created, HttpStatus.CREATED);
    }

    @GetMapping("/read")
    public ResponseEntity<List<PlaylistDTO>> read() {
        return new ResponseEntity<List<PlaylistDTO>>(this.service.read(), HttpStatus.OK);
    }

    @GetMapping("/read/id/{id}")
    public ResponseEntity<PlaylistDTO> read(@PathVariable long id) {
        return new ResponseEntity<PlaylistDTO>(this.service.read(id), HttpStatus.OK);
    }
    
    @GetMapping("/read/name/{name}")
    public ResponseEntity<PlaylistDTO> getPlaylistByName(@PathVariable("name") String name) {
        return new ResponseEntity<PlaylistDTO>(this.service.read(name), HttpStatus.OK);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<PlaylistDTO> update(@RequestBody Playlist playlist, @PathVariable long id) {
        return new ResponseEntity<PlaylistDTO>(this.service.update(playlist, id), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<PlaylistDTO> delete(@PathVariable long id) {
        return this.service.delete(id) ? new ResponseEntity<PlaylistDTO>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<PlaylistDTO>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
