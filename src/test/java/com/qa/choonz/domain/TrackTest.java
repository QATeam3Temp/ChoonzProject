package com.qa.choonz.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.qa.choonz.persistence.domain.Album;
import com.qa.choonz.persistence.domain.Playlist;
import com.qa.choonz.persistence.domain.Track;

import nl.jqno.equalsverifier.EqualsVerifier;

public class TrackTest {

	static Track track;

	@BeforeEach
	void setup() {
		track = new Track(1L, "test", null, null, 1000, "la");
	}

	@Test
	void testEquals() {
		EqualsVerifier.simple().forClass(Track.class).withIgnoredAnnotations(javax.persistence.Id.class)
				.withPrefabValues(Album.class, new Album(1, "Test", null, null, null, "Test"),
						new Album(2, "Test2", null, null, null, "Test2"))
				.withPrefabValues(Playlist.class, new Playlist(1, "test", "test", "test", null),
						new Playlist(2, "test2", "test2", "test2", null))
				.verify();
	}

}
