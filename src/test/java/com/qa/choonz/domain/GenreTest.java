package com.qa.choonz.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.qa.choonz.persistence.domain.Genre;

public class GenreTest {
	
	static Genre genre;
	
	@BeforeEach
	void setup() {
		genre = new Genre(1L, "test", "test", null);
	}
	
	@Test
	void ConstructorTest() {		
		Genre genre = new Genre(1, "test", "test");
			Assertions.assertEquals(genre.toString(), ("Genre [id=1, name=test, description=test, albums=null]"));		
	}

}
