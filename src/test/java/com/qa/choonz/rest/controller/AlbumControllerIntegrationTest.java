package com.qa.choonz.rest.controller;

import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import com.qa.choonz.rest.dto.ArtistDTO;
import com.qa.choonz.rest.dto.GenreDTO;
import com.qa.choonz.rest.dto.TrackDTO;
import com.qa.choonz.rest.dto.UserDTO;
import com.qa.choonz.service.AlbumService;
import com.qa.choonz.service.ArtistService;
import com.qa.choonz.service.GenreService;
import com.qa.choonz.service.TrackService;
import com.qa.choonz.service.UserService;
import com.qa.choonz.utils.TestWatch;
import com.qa.choonz.utils.UserSecurity;
import com.qa.choonz.utils.mappers.AlbumMapper;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.LogStatus;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(TestWatch.class)
@Sql(scripts = { "classpath:test-schema.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class AlbumControllerIntegrationTest {

	@Autowired
	MockMvc mvc;

	@Autowired
	AlbumService service;

	@Autowired
	TrackService tService;

	@Autowired
	GenreService gService;

	@Autowired
	ArtistService aService;

	@Autowired
	UserService uService;

	@Autowired
	AlbumMapper mapper;

	@Autowired
	ObjectMapper objectMapper;

	ExtentReports report = TestWatch.report;

	TrackDTO validTrackDTO = new TrackDTO();
	GenreDTO validGenreDTO = new GenreDTO("test", "test");
	ArtistDTO validArtistDTO = new ArtistDTO("test");
	AlbumDTO albumDTO = new AlbumDTO();
	List<AlbumDTO> albumDTOs = new ArrayList<AlbumDTO>();
	List<Long> emptyList = new ArrayList<Long>();
	UserDTO user = new UserDTO(1, "CowieJr", "password");
	String key = "";

	@BeforeEach
	void init() {

		if (key.isBlank()) {
			try {
				uService.create(user);
				byte[] salt = ByteBuffer.allocate(4).putInt(1).array();
				key = "CowieJr:" + UserSecurity.encrypt("CowieJr", salt);
			} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			}
		}

		validTrackDTO = new TrackDTO("test", 1000, "test");
		validTrackDTO = tService.create(validTrackDTO);
		validGenreDTO = new GenreDTO("test", "test");
		validGenreDTO = gService.create(validGenreDTO);
		validArtistDTO = new ArtistDTO("test");
		validArtistDTO = aService.create(validArtistDTO);
		albumDTO = new AlbumDTO(1, "test", List.of(1L), 1L, 1L, "test");
		albumDTO = service.create(albumDTO);
		albumDTOs = List.of(albumDTO);
	}

	@AfterAll
	static void Exit() {
		TestWatch.report.flush();
	}

	@Test
	void createAlbumTest() throws Exception {
		TestWatch.test = report.startTest("Create album test - controller integration");
		AlbumDTO albumToSave = new AlbumDTO("test", "test");
		AlbumDTO expectedAlbum = new AlbumDTO(albumDTO.getId() + 1, "test", emptyList, 0L, 0L, "test");
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.POST, "/albums/create");
		mockRequest.contentType(MediaType.APPLICATION_JSON);
		mockRequest.header("Key", key);
		mockRequest.content(objectMapper.writeValueAsString(albumToSave));
		mockRequest.accept(MediaType.APPLICATION_JSON);
		ResultMatcher statusMatcher = MockMvcResultMatchers.status().isCreated();
		ResultMatcher contentMatcher = MockMvcResultMatchers.content()
				.json(objectMapper.writeValueAsString(expectedAlbum));
		mvc.perform(mockRequest).andExpect(statusMatcher).andExpect(contentMatcher);
		TestWatch.test.log(LogStatus.PASS, "Ok");
		report.endTest(TestWatch.test);
	}

	@Test
	void badCreateRequestTest() throws Exception {
		TestWatch.test = report.startTest("Bad create album test");
		AlbumDTO badAlbumDTO = new AlbumDTO();

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.POST, "/albums/create");
		mockRequest.contentType(MediaType.APPLICATION_JSON);
		mockRequest.header("Key", key);
		mockRequest.content(objectMapper.writeValueAsString(badAlbumDTO));
		mockRequest.accept(MediaType.APPLICATION_JSON);
		ResultMatcher statusMatcher = MockMvcResultMatchers.status().isBadRequest();
		mvc.perform(mockRequest).andExpect(statusMatcher);
		TestWatch.test.log(LogStatus.PASS, "Ok");
	}

	@Test
	void readAlbumByGenreTest() throws Exception {
		TestWatch.test = report.startTest("Read album by genre test - controller integration");
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.GET,
				"/albums/read/genre/1");
		mockRequest.accept(MediaType.APPLICATION_JSON);
		ResultMatcher statusMatcher = MockMvcResultMatchers.status().isOk();
		ResultMatcher contentMatcher = MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(albumDTOs));
		mvc.perform(mockRequest).andExpect(statusMatcher).andExpect(contentMatcher);
		TestWatch.test.log(LogStatus.PASS, "Ok");
		report.endTest(TestWatch.test);
	}

	@Test
	void readAlbumByArtistTest() throws Exception {
		TestWatch.test = report.startTest("Read album by artist test - controller integration");
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.GET,
				"/albums/read/artist/1");
		mockRequest.accept(MediaType.APPLICATION_JSON);
		ResultMatcher statusMatcher = MockMvcResultMatchers.status().isOk();
		ResultMatcher contentMatcher = MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(albumDTOs));
		mvc.perform(mockRequest).andExpect(statusMatcher).andExpect(contentMatcher);
		TestWatch.test.log(LogStatus.PASS, "Ok");
		report.endTest(TestWatch.test);
	}

	@Test
	void readAlbumTest() throws Exception {
		TestWatch.test = report.startTest("Read albums test - controller integration");
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.GET, "/albums/read");
		mockRequest.accept(MediaType.APPLICATION_JSON);
		ResultMatcher statusMatcher = MockMvcResultMatchers.status().isOk();
		ResultMatcher contentMatcher = MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(albumDTOs));
		mvc.perform(mockRequest).andExpect(statusMatcher).andExpect(contentMatcher);
		TestWatch.test.log(LogStatus.PASS, "Ok");
		report.endTest(TestWatch.test);
	}

	@Test
	void readAlbumByIDTest() throws Exception {
		TestWatch.test = report.startTest("Read album by id test - controller integration");
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.GET, "/albums/read/id/1");
		mockRequest.accept(MediaType.APPLICATION_JSON);
		ResultMatcher statusMatcher = MockMvcResultMatchers.status().isOk();
		ResultMatcher contentMatcher = MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(albumDTO));
		mvc.perform(mockRequest).andExpect(statusMatcher).andExpect(contentMatcher);
		TestWatch.test.log(LogStatus.PASS, "Ok");
		report.endTest(TestWatch.test);
	}

	@Test
	void readAlbumByNameTest() throws Exception {
		TestWatch.test = report.startTest("Read album by name test - controller integration");
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.GET,
				"/albums/read/name/test");
		mockRequest.accept(MediaType.APPLICATION_JSON);
		ResultMatcher statusMatcher = MockMvcResultMatchers.status().isOk();
		ResultMatcher contentMatcher = MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(albumDTO));
		mvc.perform(mockRequest).andExpect(statusMatcher).andExpect(contentMatcher);
		TestWatch.test.log(LogStatus.PASS, "Ok");
		report.endTest(TestWatch.test);
	}

	@Test
	void updateAlbumTest() throws Exception {
		TestWatch.test = report.startTest("Update album test - controller integration");
		AlbumDTO albumToSave = new AlbumDTO("testaa", "testaa");
		AlbumDTO updatedAlbum = new AlbumDTO(albumDTO.getId(), "testaa", emptyList, 0L, 0L, "testaa");
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.PUT, "/albums/update/1");
		mockRequest.contentType(MediaType.APPLICATION_JSON);
		mockRequest.content(objectMapper.writeValueAsString(albumToSave));
		mockRequest.accept(MediaType.APPLICATION_JSON);
		mockRequest.header("Key", key);
		ResultMatcher statusMatcher = MockMvcResultMatchers.status().isAccepted();
		ResultMatcher contentMatcher = MockMvcResultMatchers.content()
				.json(objectMapper.writeValueAsString(updatedAlbum));
		mvc.perform(mockRequest).andExpect(statusMatcher).andExpect(contentMatcher);
		TestWatch.test.log(LogStatus.PASS, "Ok");
		report.endTest(TestWatch.test);
	}

	@Test
	void deleteAlbumTest() throws Exception {
		TestWatch.test = report.startTest("Delete album test - controller integration");
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.DELETE,
				"/albums/delete/1");
		mockRequest.contentType(MediaType.APPLICATION_JSON);
		mockRequest.header("Key", key);
		ResultMatcher statusMatcher = MockMvcResultMatchers.status().isNoContent();
		mvc.perform(mockRequest).andExpect(statusMatcher);
		TestWatch.test.log(LogStatus.PASS, "Ok");
		report.endTest(TestWatch.test);
	}

}
