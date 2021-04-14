package com.qa.choonz.rest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class RouteContoller {
	@GetMapping(value = "/")
	public String index() {
		return "index.html";
	}

	@GetMapping(value = "/home")
	public String home() {
		return "index.html";
	}

	@GetMapping(value = "/search")
	public String search() {
		return "Search.html";
	}
	
    @GetMapping(value = "/tracks")
    public String tracks() {
        return "tracks.html";
    }
    
    @GetMapping(value = "/track")
    public String track() {
        return "track.html";
    }
    
    @GetMapping(value = "/artists")
    public String artists() {
        return "artists.html";
    }
    
    @GetMapping(value = "/artist")
    public String artist() {
        return "artist.html";
    }
    
    @GetMapping(value = "/playlist")
    public String playlist() {
        return "playlists.html";
    }
    
    
    @GetMapping(value = "/playlists")
    public String playlists() {
        return "topplaylists.html";
    }
    
    @GetMapping(value = "/album")
    public String album() {
        return "album.html";
    }
    
    @GetMapping(value = "/genre")
    public String genre() {
        return "genre.html";
    }
    
    @GetMapping(value = "/signup")
    public String signup() {
        return "signup.html";
    }
    
    @GetMapping(value = "/login")
    public String login() {
        return "login.html";
    }
    
    @GetMapping(value = "/createtrack")
    public String createTracks() {
    	return "createtrack.html";
    }
    
    @GetMapping(value = "/creategenre")
    public String createGenres() {
    	return "creategenre.html";
    }
    
    @GetMapping(value = "/createplaylist")
    public String createPlaylists() {
    	return "createplaylist.html";
    }


    @GetMapping(value = "/delete")
    public String delete() {
        return "DeleteForm.html";
    }
    @GetMapping(value = "/update")
    public String update() {
        return "UpdateForm.html";
    }
    @GetMapping(value = "/updatetracks")
    public String updateTracks() {
        return "Updatetracks.html";
    }
    @GetMapping(value = "/updateartists")
    public String updateArtists() {
        return "Updateartists.html";
    }
    @GetMapping(value = "/updategenres")
    public String updateGenres() {
        return "Updategenres.html";
    }
    @GetMapping(value = "/updateplaylists")
    public String updatePlaylists() {
        return "Updateplaylists.html";
    }
    @GetMapping(value = "/updatealbums")
    public String updateAlbums() {
        return "Updatealbums.html";
    }
}
