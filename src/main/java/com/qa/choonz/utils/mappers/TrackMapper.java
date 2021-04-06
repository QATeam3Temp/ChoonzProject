package com.qa.choonz.utils.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qa.choonz.persistence.domain.Track;
import com.qa.choonz.persistence.repository.AlbumRepository;
import com.qa.choonz.persistence.repository.PlaylistRepository;
import com.qa.choonz.rest.dto.TrackDTO;

@Service
public class TrackMapper {

    private AlbumRepository aRepo;
    private PlaylistRepository pRepo;
    @Autowired
    public TrackMapper(PlaylistRepository pRepo,AlbumRepository aRepo) {
        super();
        this.pRepo = pRepo;
        this.aRepo = aRepo;
    }
    
    public TrackDTO mapToDTO(Track track) {
    	TrackDTO trackDTO = new TrackDTO();
    	trackDTO.setId(track.getId());
    	trackDTO.setName(track.getName());
    	try{
    	trackDTO.setAlbum(track.getAlbum().getId());
    	}catch(Exception e) {
    		trackDTO.setAlbum(0L);
    	}
    	
    	try{
    		trackDTO.setPlaylist(track.getPlaylist().getId());
        	}catch(Exception e) {
        		trackDTO.setPlaylist(0L);
        	}
    	trackDTO.setDuration(track.getDuration());
    	trackDTO.setLyrics(track.getLyrics());
		return trackDTO;
    }
    
    public Track mapFromDTO(TrackDTO trackDTO) {
    	Track track = new Track();
    	track.setId(trackDTO.getId());
    	track.setName(trackDTO.getName());
    	try{
    		track.setAlbum(aRepo.findById(trackDTO.getAlbum()).get());
			
		}catch(Exception e) {
			track.setAlbum(null);
		}
    	try{
    		track.setPlaylist(pRepo.findById(trackDTO.getPlaylist()).get());
			
		}catch(Exception e) {
			track.setPlaylist(null);
		}
    	track.setDuration(trackDTO.getDuration());
    	track.setLyrics(trackDTO.getLyrics());
		return track;
    }
    
}
