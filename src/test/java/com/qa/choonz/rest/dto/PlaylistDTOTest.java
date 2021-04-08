package com.qa.choonz.rest.dto;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

public class PlaylistDTOTest {

	@Test
	void testEquals() {
		EqualsVerifier.simple().forClass(PlaylistDTO.class).verify();
	}
	
}
