package com.qa.choonz.rest.controller;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterAll;
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
import com.qa.choonz.rest.dto.PlaylistDTO;
import com.qa.choonz.rest.dto.UserDTO;
import com.qa.choonz.service.PlaylistService;
import com.qa.choonz.service.UserService;
import com.qa.choonz.utils.mappers.GenreMapper;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = { "classpath:test-schema.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class PlaylistControllerIntegrationTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	PlaylistService service;

	@Autowired
	UserService uService;

	@Autowired
	GenreMapper mapper;

	@Autowired
	ObjectMapper objectMapper;

	static ExtentReports report = new ExtentReports("Documentation/reports/Playlist_Controller_Integration_Report.html",
			true);
	static ExtentTest test;

	PlaylistDTO validPlaylistDTO = new PlaylistDTO("test", "test", "test");
	private UserDTO user = new UserDTO("cowiejr", "password");
	private String key = "";
	ArrayList<PlaylistDTO> validPlaylistDTOs = new ArrayList<PlaylistDTO>();
	ArrayList<Long> emptyList = new ArrayList<Long>();

	@BeforeEach
	void init() {

		if (key.isBlank()) {
			try {
				uService.create(user);
				key = "CowieJr:1000:00000001:7f1d6351d49e0bb872d4642ecec60ee3";
				
			} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {

			}
		}
		validPlaylistDTO = service.create(validPlaylistDTO);
		validPlaylistDTOs.add(validPlaylistDTO);

	}

	@AfterAll
	static void Exit() {
		report.flush();
	}

	@Test
	public void createPlaylistTest() throws Exception {
		test = report.startTest("Create playlist test");
		PlaylistDTO playlistToSave = new PlaylistDTO("test2", "test2", "test2");
		PlaylistDTO expectedPlaylist = new PlaylistDTO(validPlaylistDTO.getId() + 1, "test2", "test2", "test2",
				emptyList);
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.POST,
				"/playlists/create");
		mockRequest.contentType(MediaType.APPLICATION_JSON);
		mockRequest.header("Key", key);
		mockRequest.content(objectMapper.writeValueAsString(playlistToSave));
		mockRequest.accept(MediaType.APPLICATION_JSON);
		ResultMatcher statusMatcher = MockMvcResultMatchers.status().isCreated();
		ResultMatcher contentMatcher = MockMvcResultMatchers.content()
				.json(objectMapper.writeValueAsString(expectedPlaylist));
		mvc.perform(mockRequest).andExpect(statusMatcher).andExpect(contentMatcher);
		test.log(LogStatus.PASS, "Ok");
	}

	@Test
	public void readPlaylistTest() throws Exception {
		test = report.startTest("Read playlists test");
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.GET, "/playlists/read");
		mockRequest.accept(MediaType.APPLICATION_JSON);
		ResultMatcher statusMatcher = MockMvcResultMatchers.status().isOk();
		ResultMatcher contentMatcher = MockMvcResultMatchers.content()
				.json(objectMapper.writeValueAsString(validPlaylistDTOs));
		mvc.perform(mockRequest).andExpect(statusMatcher).andExpect(contentMatcher);
		test.log(LogStatus.PASS, "Ok");
	}

	@Test
	public void readPlaylistByIdTest() throws Exception {
		test = report.startTest("Read playlist by id test");
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.GET,
				"/playlists/read/id/" + validPlaylistDTO.getId());
		mockRequest.accept(MediaType.APPLICATION_JSON);
		ResultMatcher statusMatcher = MockMvcResultMatchers.status().isOk();
		ResultMatcher contentMatcher = MockMvcResultMatchers.content()
				.json(objectMapper.writeValueAsString(validPlaylistDTO));
		mvc.perform(mockRequest).andExpect(statusMatcher).andExpect(contentMatcher);
		test.log(LogStatus.PASS, "Ok");
	}

	@Test
	public void readPlaylistByNameTest() throws Exception {
		test = report.startTest("Read playlist by name test");
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.GET,
				"/playlists/read/name/" + validPlaylistDTO.getName());
		mockRequest.accept(MediaType.APPLICATION_JSON);
		ResultMatcher statusMatcher = MockMvcResultMatchers.status().isOk();
		ResultMatcher contentMatcher = MockMvcResultMatchers.content()
				.json(objectMapper.writeValueAsString(validPlaylistDTO));
		mvc.perform(mockRequest).andExpect(statusMatcher).andExpect(contentMatcher);
		test.log(LogStatus.PASS, "Ok");
	}

	@Test
	public void updatePlaylistTest() throws Exception {
		test = report.startTest("Update playlist test");
		PlaylistDTO playlistToSave = new PlaylistDTO("test2q", "test2q", "test2q");
		PlaylistDTO expectedPlaylist = new PlaylistDTO(validPlaylistDTO.getId(), "test2q", "test2q", "test2q",
				emptyList);
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.PUT,
				"/playlists/update/" + validPlaylistDTO.getId());
		mockRequest.contentType(MediaType.APPLICATION_JSON);
		mockRequest.content(objectMapper.writeValueAsString(playlistToSave));
		mockRequest.accept(MediaType.APPLICATION_JSON);
		mockRequest.header("Key", key);
		ResultMatcher statusMatcher = MockMvcResultMatchers.status().isAccepted();
		ResultMatcher contentMatcher = MockMvcResultMatchers.content()
				.json(objectMapper.writeValueAsString(expectedPlaylist));
		mvc.perform(mockRequest).andExpect(statusMatcher).andExpect(contentMatcher);
		test.log(LogStatus.PASS, "Ok");
	}

	@Test
	public void deletePlaylistTest() throws Exception {
		test = report.startTest("Delete playlist test");
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.DELETE,
				"/playlists/delete/" + validPlaylistDTO.getId());
		mockRequest.contentType(MediaType.APPLICATION_JSON);
		mockRequest.header("Key", key);
		ResultMatcher statusMatcher = MockMvcResultMatchers.status().isNoContent();
		mvc.perform(mockRequest).andExpect(statusMatcher);
		test.log(LogStatus.PASS, "Ok");
	}

}
