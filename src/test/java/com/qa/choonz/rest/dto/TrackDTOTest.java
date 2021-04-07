package com.qa.choonz.rest.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.qa.choonz.persistence.domain.User;

import nl.jqno.equalsverifier.EqualsVerifier;

public class TrackDTOTest {

	
	@Test
	void testEquals() {
		EqualsVerifier.simple().forClass(TrackDTO.class).verify();
	}
	@Test
	void ConstructorTest() {
		
		TrackDTO track = new TrackDTO("test",100, "test");
			Assertions.assertEquals(track.toString(), ("TrackDTO [id=0, name=test, album=null, playlist=null, duration=100, lyrics=test]"));
		

	}

}
