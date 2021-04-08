package com.qa.choonz.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.qa.choonz.persistence.domain.Album;
import com.qa.choonz.persistence.domain.Artist;
import com.qa.choonz.persistence.domain.Genre;
import com.qa.choonz.rest.dto.AlbumDTO;

import nl.jqno.equalsverifier.EqualsVerifier;

public class AlbumTest {

	static Album album;

	@BeforeEach
	void setup() {
		album = new Album(1, "test", null, null, null, "test");
	}

	@Test
	void ConstructorTest() {		
		Album album = new Album(1, "test","test");
			Assertions.assertEquals(album.toString(), ("Album [id=1, name=test, tracks=null, artist=null, genre=null, cover=test]"));		
	}
	
//	@Test
//	void testEquals() {
//		EqualsVerifier.simple().forClass(Album.class)
//				.withPrefabValues(Artist.class, new Artist(1, "test", null), new Artist(2, "test2", null))
//				.withPrefabValues(Genre.class, new Genre(1, "test", "test", null), new Genre(2, "test2", "test2", null))
//				.verify();
//	}

}
