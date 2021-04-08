package com.qa.choonz.rest.controller;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qa.choonz.rest.dto.AlbumDTO;
import com.qa.choonz.rest.dto.PlaylistDTO;
import com.qa.choonz.rest.dto.TrackDTO;
import com.qa.choonz.rest.dto.UserDTO;
import com.qa.choonz.service.AlbumService;
import com.qa.choonz.service.PlaylistService;
import com.qa.choonz.service.TrackService;
import com.qa.choonz.service.UserService;
import com.qa.choonz.utils.mappers.TrackMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = { "classpath:test-schema.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class TrackControllerIntegrationTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	TrackService service;

	@Autowired
	AlbumService aService;

	@Autowired
	PlaylistService pService;

	@Autowired
	UserService uService;

	@Autowired
	TrackMapper mapper;

	@Autowired
	private ObjectMapper objectMapper;

	private TrackDTO trackDTO = new TrackDTO(1, "test", 1000, "test");

	private List<TrackDTO> validTrackDTO = List.of(trackDTO);

	private Long validTrack = 0L;
	private List<Long> track = new ArrayList<Long>();

	private UserDTO user = new UserDTO("cowiejr", "password");
	private String key = "";

	private AlbumDTO validAlbum = new AlbumDTO(1, "Greatest Hits", track, 1L, 1L, "A large 2");
	private PlaylistDTO validPlaylist = new PlaylistDTO(1, "Running songs", "Primarily eurobeat",
			"A pair of legs moving", track);

	@BeforeEach
	void init() {
		if (key.isBlank()) {
			try {
				uService.create(user);
				key = "1000:00000001:7f1d6351d49e0bb872d4642ecec60ee3";
			} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		pService.create(validPlaylist);
		aService.create(validAlbum);
		trackDTO = new TrackDTO(1, "test", 1L, 1L, 1000, "test");
		trackDTO = service.create(trackDTO);
		validTrackDTO = List.of(trackDTO);
	}

	@Test
	public void createTrackTest() throws Exception {
		TrackDTO trackToSave = new TrackDTO("test", 1000, "test");
		TrackDTO expectedTrack = new TrackDTO(trackDTO.getId() + 1, "test", 0L, 0L, 1000, "test");
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.POST, "/tracks/create");
		mockRequest.contentType(MediaType.APPLICATION_JSON);
		mockRequest.header("Key", key);
		mockRequest.content(objectMapper.writeValueAsString(trackToSave));
		mockRequest.accept(MediaType.APPLICATION_JSON);
		ResultMatcher statusMatcher = MockMvcResultMatchers.status().isCreated();
		ResultMatcher contentMatcher = MockMvcResultMatchers.content()
				.json(objectMapper.writeValueAsString(expectedTrack));
		mvc.perform(mockRequest).andExpect(statusMatcher).andExpect(contentMatcher);
	}

	@Test
	public void readAllTest() throws Exception {
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.GET, "/tracks/read");
		mockRequest.accept(MediaType.APPLICATION_JSON);

		ResultMatcher statusMatcher = MockMvcResultMatchers.status().isOk();
		ResultMatcher contentMatcher = MockMvcResultMatchers.content()
				.json(objectMapper.writeValueAsString(validTrackDTO));

		mvc.perform(mockRequest).andExpect(statusMatcher).andExpect(contentMatcher);
	}

	@Test
	public void readAlbumTest() throws Exception {
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.GET,
				"/tracks/read/playlist/1");
		mockRequest.accept(MediaType.APPLICATION_JSON);

		ResultMatcher statusMatcher = MockMvcResultMatchers.status().isOk();
		ResultMatcher contentMatcher = MockMvcResultMatchers.content()
				.json(objectMapper.writeValueAsString(validTrackDTO));

		mvc.perform(mockRequest).andExpect(statusMatcher).andExpect(contentMatcher);
	}

	@Test
	public void readPlaylistTest() throws Exception {
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.GET,
				"/tracks/read/album/1");
		mockRequest.accept(MediaType.APPLICATION_JSON);

		ResultMatcher statusMatcher = MockMvcResultMatchers.status().isOk();
		ResultMatcher contentMatcher = MockMvcResultMatchers.content()
				.json(objectMapper.writeValueAsString(validTrackDTO));

		mvc.perform(mockRequest).andExpect(statusMatcher).andExpect(contentMatcher);
	}

	@Test
	public void getTrackByIdTest() throws Exception {
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.GET,
				"/tracks/read/id/" + trackDTO.getId());
		mockRequest.accept(MediaType.APPLICATION_JSON);

		ResultMatcher statusMatcher = MockMvcResultMatchers.status().isOk();
		ResultMatcher contentMatcher = MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(trackDTO));

		mvc.perform(mockRequest).andExpect(statusMatcher).andExpect(contentMatcher);
	}

	@Test
	public void getTrackByNameTest() throws Exception {
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.GET,
				"/tracks/read/name/" + trackDTO.getName());
		mockRequest.accept(MediaType.APPLICATION_JSON);

		ResultMatcher statusMatcher = MockMvcResultMatchers.status().isOk();
		ResultMatcher contentMatcher = MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(trackDTO));

		mvc.perform(mockRequest).andExpect(statusMatcher).andExpect(contentMatcher);
	}

	@Test
	public void updateTrackTest() throws Exception {
		TrackDTO updatedTrack = new TrackDTO("update test", 1000, "test");
		TrackDTO expectedTrack = new TrackDTO(trackDTO.getId(), "update test", 1L, 1L, 1000, "test");
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.PUT, "/tracks/update/1");
		mockRequest.contentType(MediaType.APPLICATION_JSON);
		mockRequest.content(objectMapper.writeValueAsString(updatedTrack));
		mockRequest.accept(MediaType.APPLICATION_JSON);
		mockRequest.header("Key", key);
		ResultMatcher statusMatcher = MockMvcResultMatchers.status().isAccepted();
		ResultMatcher contentMatcher = MockMvcResultMatchers.content()
				.json(objectMapper.writeValueAsString(expectedTrack));

		mvc.perform(mockRequest).andExpect(statusMatcher).andExpect(contentMatcher);
	}

	@Test
	public void deletePlaylistTrackTest() throws Exception {
		TrackDTO testTrack = new TrackDTO("test", 1000, "test");
		TrackDTO expectedPlaylistNullTrack = new TrackDTO(trackDTO.getId(), "test", 1L, 0L, 1000, "test");

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.PUT,
				"/tracks/update/playlist/1");
		mockRequest.contentType(MediaType.APPLICATION_JSON);
		mockRequest.content(objectMapper.writeValueAsString(testTrack));
		mockRequest.accept(MediaType.APPLICATION_JSON);
		mockRequest.header("Key", key);

		ResultMatcher statusMatcher = MockMvcResultMatchers.status().isAccepted();
		ResultMatcher contentMatcher = MockMvcResultMatchers.content()
				.json(objectMapper.writeValueAsString(expectedPlaylistNullTrack));

		mvc.perform(mockRequest).andExpect(statusMatcher).andExpect(contentMatcher);
	}
	

	@Test
	public void deleteAlbumTrackTest() throws Exception {
		TrackDTO testTrack = new TrackDTO("test", 1000, "test");
		TrackDTO expectedAlbumNullTrack = new TrackDTO(trackDTO.getId(), "test", 0L, 1L, 1000, "test");

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.PUT,
				"/tracks/update/album/1");
		mockRequest.contentType(MediaType.APPLICATION_JSON);
		mockRequest.content(objectMapper.writeValueAsString(testTrack));
		mockRequest.accept(MediaType.APPLICATION_JSON);
		mockRequest.header("Key", key);

		ResultMatcher statusMatcher = MockMvcResultMatchers.status().isAccepted();
		ResultMatcher contentMatcher = MockMvcResultMatchers.content()
				.json(objectMapper.writeValueAsString(expectedAlbumNullTrack));

		mvc.perform(mockRequest).andExpect(statusMatcher).andExpect(contentMatcher);
	}

	@Test
	public void deleteTrackTest() throws Exception {
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.DELETE,
				"/tracks/delete/1");
		mockRequest.contentType(MediaType.APPLICATION_JSON);
		mockRequest.header("Key", key);
		ResultMatcher statusMatcher = MockMvcResultMatchers.status().isNoContent();
		mvc.perform(mockRequest).andExpect(statusMatcher);
	}

}
