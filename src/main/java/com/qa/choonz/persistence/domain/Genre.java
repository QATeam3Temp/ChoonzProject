package com.qa.choonz.persistence.domain;

import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.qa.choonz.rest.dto.GenreDTO;

@Entity
@Table(name = "genre")
public class Genre {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@NotNull
	@Size(max = 100)
	@Column(unique = true)
	private String name;

	@NotNull
	@Size(max = 250)
	private String description;

	@OneToMany(mappedBy = "genre", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Album> albums;

	public Genre() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Genre(GenreDTO genreDTO) {
		super();
		this.id = genreDTO.getId();
		this.name = genreDTO.getName();
		this.description = genreDTO.getDescription();
	}

	public Genre(long id, @NotNull @Size(max = 100) String name, @NotNull @Size(max = 250) String description) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
	}
	
	public Genre(long id, @NotNull @Size(max = 100) String name, @NotNull @Size(max = 250) String description,
			List<Album> albums) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.albums = albums;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Genre [id=").append(id).append(", name=").append(name).append(", description=")
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
		if (!(obj instanceof Genre)) {
			return false;
		}
		Genre other = (Genre) obj;
		return Objects.equals(albums, other.albums) && Objects.equals(description, other.description) && id == other.id
				&& Objects.equals(name, other.name);
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public List<Album> getAlbums() {
		return albums;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setAlbums(List<Album> albums) {
		this.albums = albums;
	}

	public Genre orElseThrow(Object object) {
		// TODO Auto-generated method stub
		return null;
	}

}
