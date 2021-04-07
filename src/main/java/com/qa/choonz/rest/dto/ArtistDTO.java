package com.qa.choonz.rest.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.qa.choonz.persistence.domain.Artist;

public class ArtistDTO {

	private long id;
	private String name;
	private List<Long> albums;

	public ArtistDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ArtistDTO(Artist artist) {
		super();
		this.id = artist.getId();
		this.name = artist.getName();
	}

	public ArtistDTO(long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public ArtistDTO(long id, String name, List<Long> albums) {
		super();
		this.id = id;
		this.name = name;
		this.albums = albums;
	}

	public ArtistDTO(String name) {
		this.name = name;
		this.albums = new ArrayList<Long>();
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

	public List<Long> getAlbums() {
		return albums;
	}

	public void setAlbums(List<Long> albums) {
		this.albums = albums;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ArtistDTO [id=").append(id).append(", name=").append(name).append(", albums=").append(albums)
				.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		return Objects.hash(albums, id, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ArtistDTO)) {
			return false;
		}
		ArtistDTO other = (ArtistDTO) obj;
		return Objects.equals(albums, other.albums) && id == other.id && Objects.equals(name, other.name);
	}

}
