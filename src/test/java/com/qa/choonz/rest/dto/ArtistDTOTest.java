package com.qa.choonz.rest.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

public class ArtistDTOTest {

	@Test
	void testEquals() {
		EqualsVerifier.simple().forClass(ArtistDTO.class).verify();
	}

	@Test
	void ConstructorTest() {
		ArtistDTO artist = new ArtistDTO(1, "test");
		Assertions.assertEquals(artist.toString(), ("ArtistDTO [id=1, name=test, albums=null]"));
	}

}
