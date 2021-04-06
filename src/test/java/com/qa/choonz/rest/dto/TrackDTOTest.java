package com.qa.choonz.rest.dto;

import org.junit.jupiter.api.BeforeEach;

public class TrackDTOTest {

	static TrackDTO trackDTO;

	@BeforeEach
	void setup() {
		trackDTO = new TrackDTO(1, "test", 0L, 0L, 1000, "la");
	}
	
}
