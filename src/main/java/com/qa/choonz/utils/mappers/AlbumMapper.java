package com.qa.choonz.utils.mappers;

import java.util.ArrayList;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qa.choonz.exception.TrackNotFoundException;
import com.qa.choonz.persistence.domain.Album;
import com.qa.choonz.persistence.domain.Track;
import com.qa.choonz.persistence.repository.AlbumRepository;
import com.qa.choonz.persistence.repository.TrackRepository;
import com.qa.choonz.rest.dto.AlbumDTO;

@Service
public class AlbumMapper {

    private TrackRepository repo;
    @Autowired
    public AlbumMapper(TrackRepository repo) {
        super();
        this.repo = repo;
    }
    
	public AlbumDTO MapToDTO(Album album){
		AlbumDTO albumDTO =  new AlbumDTO();
		albumDTO.setId(album.getId());
		albumDTO.setName(album.getName());
		albumDTO.setGenre(album.getGenre());
		albumDTO.setCover(album.getCover());
		ArrayList<Long> tracks = new ArrayList<Long>();
        for (Track track : album.getTracks()) {
        	track.setAlbum(album);
        	repo.save(track);
			tracks.add(track.getId());
		}
        albumDTO.setTracks(tracks);
		return albumDTO;
	}
	
	public Album MapFromDTO(AlbumDTO albumDTO){
		Album album =  new Album();
		album.setId(albumDTO.getId());
		album.setName(albumDTO.getName());
		album.setGenre(albumDTO.getGenre());
		album.setCover(albumDTO.getCover());
		ArrayList<Track> tracks = new ArrayList<Track>();
        for (Long track : albumDTO.getTracks()) {
        	Track tra =(repo.findById(track).orElseThrow(TrackNotFoundException::new));
			tracks.add(tra);
		}
        album.setTracks(tracks);
		return album;
	}
}
