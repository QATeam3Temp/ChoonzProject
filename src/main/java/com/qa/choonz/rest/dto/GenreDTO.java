package com.qa.choonz.rest.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.qa.choonz.persistence.domain.Genre;

public class GenreDTO {

	private long id;
	private String name;
	private String description;
	private List<Long> albums;

	public GenreDTO() {
		super();
	}

	public GenreDTO(Genre genre) {
		super();
		this.id = genre.getId();
		this.name = genre.getName();
		this.description = genre.getDescription();
	}

	public GenreDTO(long id, String name, String description, List<Long> albums) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.albums = albums;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Long> getAlbums() {
		return albums;
	}

	public void setAlbums(ArrayList<Long> albums2) {
		this.albums = albums2;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("GenreDTO [id=").append(id).append(", name=").append(name).append(", description=")
				.append(description).append(", albums=").append(albums).append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		return Objects.hash(albums, description, id, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof GenreDTO)) {
			return false;
		}
		GenreDTO other = (GenreDTO) obj;
		return Objects.equals(albums, other.albums) && Objects.equals(description, other.description) && id == other.id
				&& Objects.equals(name, other.name);
	}

}
