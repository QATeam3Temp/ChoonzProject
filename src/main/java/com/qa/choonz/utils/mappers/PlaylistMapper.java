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

	public PlaylistDTO MapToDTO(Playlist playlist) {
		PlaylistDTO playlistDTO = new PlaylistDTO();
		playlistDTO.setId(playlist.getId());
		playlistDTO.setDescription(playlist.getDescription());
		playlistDTO.setName(playlist.getName());
		playlistDTO.setArtwork(playlist.getArtwork());
		playlistDTO.setTracks(playlist.getTracksId());
		ArrayList<Long> tracks = new ArrayList<Long>();
		for (Track track : playlist.getTracks()) {
			track.setPlaylist(playlist);
			tracks.add(track.getId());
		}
		repo.saveAll(playlist.getTracks());
		playlistDTO.setTracks(tracks);
		
		return playlistDTO;
	}

	public Playlist MapFromDTO(PlaylistDTO playlistDTO) {
		Playlist playlist = new Playlist(playlistDTO);
		playlist.setId(playlistDTO.getId());
		playlist.setName(playlistDTO.getName());
		playlist.setDescription(playlistDTO.getDescription());
		playlist.setArtwork(playlistDTO.getArtwork());
		playlist.setTracks((repo.getTrackByPlaylistSQL(playlist.getId())));
		return playlist;
	}
}
