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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.qa.choonz.rest.dto.AlbumDTO;

@Entity
@Table(name = "album")
public class Album {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@NotNull
	@Size(max = 100)
	@Column(unique = true)
	private String name;

	@OneToMany(mappedBy = "album", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Track> tracks;

	@ManyToOne(targetEntity = Artist.class, fetch = FetchType.LAZY)
	private Artist artist;

	@ManyToOne(targetEntity = Genre.class, fetch = FetchType.LAZY)
	private Genre genre;

	private String cover;

	public Album() {
		super();
		this.id = 0L;
		this.name ="";
		this.tracks = new ArrayList<Track>();
		this.cover = "";
		this.genre = new Genre();
		this.artist = new Artist();
	}

	public Album(AlbumDTO albumDTO) {
		super();
		this.id = albumDTO.getId();
		this.name = albumDTO.getName();
		this.tracks = new ArrayList<Track>();
		this.cover = albumDTO.getCover();
		this.genre = new Genre();
		this.artist = new Artist();
	}

	public Album(long id, @NotNull @Size(max = 100) String name, String cover) {
		super();
		this.id = id;
		this.name = name;
		this.cover = cover;
		this.genre = new Genre();
		this.artist = new Artist();
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
		List<String> tracknames = new ArrayList<String>();
		if(tracks.size()>0) {
		for (Track track : tracks) {
			tracknames.add(track.getName());
		}
		}
		return "Album [id=" + id + ", name=" + name + ", tracks=" + tracknames.toString() + ", artist=" + artist.getName() + ", genre=" + genre.getName()
				+ ", cover=" + cover + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((artist == null) ? 0 : artist.hashCode());
		result = prime * result + ((cover == null) ? 0 : cover.hashCode());
		result = prime * result + ((genre == null) ? 0 : genre.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((tracks == null) ? 0 : tracks.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Album other = (Album) obj;
		if (artist == null) {
			if (other.artist != null)
				return false;
		} else if (!artist.equals(other.artist))
			return false;
		if (cover == null) {
			if (other.cover != null)
				return false;
		} else if (!cover.equals(other.cover))
			return false;
		if (genre == null) {
			if (other.genre != null)
				return false;
		} else if (!genre.equals(other.genre))
			return false;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (tracks == null) {
			if (other.tracks != null)
				return false;
		} else if (!tracks.equals(other.tracks))
			return false;
		return true;
	}

	public List<Long> getTracksId() {
		List<Long> ids = new ArrayList<>();
		tracks.forEach(track -> {
			ids.add(track.getId());
		});
		return ids;
	}

}
