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

    @GetMapping(value = "/tracks")
    public String tracks() {
        return "tracks.html";
    }
    @GetMapping(value = "/artists")
    public String artists() {
        return "artists.html";
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
}
