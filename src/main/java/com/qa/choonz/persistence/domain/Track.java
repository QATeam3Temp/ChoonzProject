package com.qa.choonz.persistence.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.qa.choonz.rest.dto.TrackDTO;

@Entity
@Table(name = "track")
public class Track {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@NotNull
	@Size(max = 100)
	@Column(unique = true)
	private String name;

	@ManyToOne(targetEntity = Album.class, fetch = FetchType.EAGER)
	private Album album;

	@ManyToOne(targetEntity = Playlist.class, fetch = FetchType.EAGER)
	private Playlist playlist;

	// in seconds
	private int duration;

	private String lyrics;

	public Track() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Track(TrackDTO trackDTO) {
		super();
		this.name = trackDTO.getName();
		this.duration = trackDTO.getDuration();
		this.lyrics = trackDTO.getLyrics();
	}

	public Track(long id, @NotNull @Size(max = 100) String name, Album album, Playlist playlist, int duration,
			String lyrics) {
		super();
		this.id = id;
		this.name = name;
		this.album = album;
		this.playlist = playlist;
		this.duration = duration;
		this.lyrics = lyrics;
	}
	public Track(@NotNull @Size(max = 100) String name, Album album, Playlist playlist, int duration,
			String lyrics) {
		super();
		this.name = name;
		this.album = album;
		this.playlist = playlist;
		this.duration = duration;
		this.lyrics = lyrics;
	}
	public Track(int id, String name, int duration, String lyrics) {
		super();
		this.id = id;
		this.name = name;
		this.album = new Album();
		this.playlist = new Playlist();
		this.duration = duration;
		this.lyrics = lyrics;
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

	public Album getAlbum() {
		return album;
	}

	public void setAlbum(Album album) {
		this.album = album;
	}

	public Playlist getPlaylist() {
		return playlist;
	}

	public void setPlaylist(Playlist playlist) {
		this.playlist = playlist;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getLyrics() {
		return lyrics;
	}

	public void setLyrics(String lyrics) {
		this.lyrics = lyrics;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Track [id=").append(id).append(", name=").append(name).append(", album=").append(album)
				.append(", playlist=").append(playlist).append(", duration=").append(duration).append(", lyrics=")
				.append(lyrics).append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		return Objects.hash(album, duration, id, lyrics, name, playlist);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Track)) {
			return false;
		}
		Track other = (Track) obj;
		return Objects.equals(album, other.album) && duration == other.duration && id == other.id
				&& Objects.equals(lyrics, other.lyrics) && Objects.equals(name, other.name)
				&& Objects.equals(playlist, other.playlist);
	}

}
