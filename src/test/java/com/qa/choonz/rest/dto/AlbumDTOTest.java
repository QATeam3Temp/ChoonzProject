package com.qa.choonz.rest.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

public class AlbumDTOTest {

//	@Test
//	void testEquals() {
//		EqualsVerifier.simple().forClass(AlbumDTO.class).verify();
//	}

	@Test
	void ConstructorTest() {
		
		AlbumDTO album = new AlbumDTO("test","test");
			Assertions.assertEquals(album.toString(), ("AlbumDTO [id=0, name=test, tracks=[], artist=0, genre=0, cover=test]"));
		

	}
}
