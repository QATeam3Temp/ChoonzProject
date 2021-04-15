package com.qa.choonz.persistence.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.qa.choonz.rest.dto.AlbumDTO;

@Entity
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Size(max = 100)
    @Column(unique = true)

    private String name ="";


    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL)
    private List<Track> tracks;


    @ManyToOne(targetEntity = Artist.class, fetch = FetchType.LAZY)
    private Artist artist;


    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
        })
    @JoinTable(name = "featured", 
    		joinColumns = @JoinColumn(name = "artist_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "album_id", referencedColumnName = "id"))
    private List<Artist> featuredArtists;
    
    @ManyToOne(targetEntity = Genre.class, fetch = FetchType.LAZY)
    private Genre genre;

    private String cover;

    public Album() {
        super();
        this.id =0;
        this.name = "";
        this.genre = new Genre();

    }
    
    public Album(AlbumDTO albumDTO) {
        super();
        this.id = albumDTO.getId();
        this.name = albumDTO.getName();
        this.tracks = new ArrayList<Track>();
        this.artist = new Artist();
        this.genre = new Genre();
        this.cover = albumDTO.getCover();

        this.featuredArtists=new ArrayList<Artist>();
    }
    
    public List<Artist> getFeaturedArtists() {
		return featuredArtists;
	}

	public void setFeaturedArtists(List<Artist> featuredArtists) {
		this.featuredArtists = featuredArtists;
	}
    public Album(long id, @NotNull @Size(max = 100) String name, List<Track> tracks, Artist artist, Genre genre,

            String cover) {
        super();
        this.id = id;
        this.name = name;
        this.tracks = tracks;
        this.artist = artist;
        this.genre = genre;
        this.cover = cover;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Album [id=").append(id).append(", name=").append(name).append(", tracks=").append(getTracksId())
                .append(", artist=").append(artist.getName()).append(", feat ").append(getFeaturedArtistIds()).append(", genre=").append(genre.getName()).append(", cover=").append(cover)

                .append("]");
        return builder.toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(artist, cover, genre, id, name, tracks);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Album)) {
            return false;
        }
        Album other = (Album) obj;
        return Objects.equals(artist, other.artist) && Objects.equals(cover, other.cover)
                && Objects.equals(genre, other.genre) && id == other.id && Objects.equals(name, other.name)
                && Objects.equals(tracks, other.tracks);
    }

	public List<Long> getTracksId() {
		List<Long> ids = new ArrayList<>();
		tracks.forEach(track -> {
			ids.add(track.getId());
		});
		return ids;
	}
	public ArrayList<Long> getFeaturedArtistIds() {
		ArrayList<Long> ids = new ArrayList<>();
		if(featuredArtists!=null) {
			featuredArtists.forEach(artist -> {
			ids.add(artist.getId());
		});}
		return ids;
	}


}
