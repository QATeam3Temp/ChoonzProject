package com.qa.choonz.utils.mappers;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qa.choonz.exception.TrackNotFoundException;
import com.qa.choonz.persistence.domain.Playlist;
import com.qa.choonz.persistence.domain.Track;
import com.qa.choonz.persistence.repository.TrackRepository;
import com.qa.choonz.rest.dto.PlaylistDTO;

@Service
public class PlaylistMapper {
	 private TrackRepository repo;
	    @Autowired
	    public PlaylistMapper(TrackRepository repo) {
	        super();
	        this.repo = repo;
	    }
	    public PlaylistDTO MapToDTO(Playlist playlist){
	    	PlaylistDTO playlistDTO =  new PlaylistDTO();
	    	playlistDTO.setId(playlist.getId());
			playlistDTO.setDescription(playlist.getDescription());
	    	playlistDTO.setName(playlist.getName());
			playlistDTO.setArtwork(playlist.getArtwork());
			ArrayList<Long> tracks = new ArrayList<Long>();
	        for (Track track : playlist.getTracks()) {
	        	track.setPlaylist(playlist);
	        	repo.save(track);
				tracks.add(track.getId());
			}
	        playlistDTO.setTracks(tracks);
			return playlistDTO;
		}
		
		public Playlist MapFromDTO(PlaylistDTO playlistDTO){
			Playlist playlist =  new Playlist(playlistDTO);
			playlist.setId(playlistDTO.getId());
			playlist.setName(playlistDTO.getName());
			playlist.setDescription(playlistDTO.getDescription());
			playlist.setArtwork(playlistDTO.getArtwork());
			ArrayList<Track> tracks = new ArrayList<Track>();
	        for (Long track : playlistDTO.getTracks()) {
	        	Track tra =(repo.findById(track).orElseThrow(TrackNotFoundException::new));
				tracks.add(tra);
			}
	        playlist.setTracks(tracks);
			return playlist;
		}
}
