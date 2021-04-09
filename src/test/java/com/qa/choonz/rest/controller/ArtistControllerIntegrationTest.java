package com.qa.choonz.rest.controller;

import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

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
import com.qa.choonz.rest.dto.ArtistDTO;
import com.qa.choonz.rest.dto.UserDTO;
import com.qa.choonz.service.ArtistService;
import com.qa.choonz.service.UserService;
import com.qa.choonz.utils.TestWatch;
import com.qa.choonz.utils.UserSecurity;
import com.qa.choonz.utils.mappers.GenreMapper;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.LogStatus;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(TestWatch.class)
@Sql(scripts = { "classpath:test-schema.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class ArtistControllerIntegrationTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	ArtistService service;

	@Autowired
	UserService uService;

	@Autowired
	GenreMapper mapper;

	@Autowired
	ObjectMapper objectMapper;

	ExtentReports report = TestWatch.report;

	ArtistDTO validArtistDTO = new ArtistDTO("test");
	private UserDTO user = new UserDTO("CowieJr", "password");
	private String key = "";
	ArrayList<ArtistDTO> validArtistDTOs = new ArrayList<ArtistDTO>();
	ArrayList<Long> emptyList = new ArrayList<Long>();

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
		validArtistDTO = service.create(validArtistDTO);
		validArtistDTOs.add(validArtistDTO);
	}

	@AfterAll
	static void Exit() {
		TestWatch.report.flush();
	}

	@Test
	void createArtistTest() throws Exception {
		TestWatch.test = report.startTest("Create artist test - controller integration");
		ArtistDTO artistToSave = new ArtistDTO("test2");
		ArtistDTO expectedArtist = new ArtistDTO(validArtistDTO.getId() + 1, "test2", emptyList);
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.POST, "/artists/create");
		mockRequest.contentType(MediaType.APPLICATION_JSON);
		mockRequest.header("Key", key);
		mockRequest.content(objectMapper.writeValueAsString(artistToSave));
		mockRequest.accept(MediaType.APPLICATION_JSON);
		ResultMatcher statusMatcher = MockMvcResultMatchers.status().isCreated();
		ResultMatcher contentMatcher = MockMvcResultMatchers.content()
				.json(objectMapper.writeValueAsString(expectedArtist));
		mvc.perform(mockRequest).andExpect(statusMatcher).andExpect(contentMatcher);
		TestWatch.test.log(LogStatus.PASS, "Ok");
		report.endTest(TestWatch.test);
	}
	
	@Test
	public void badCreateArtistRequestTest() throws Exception {
		test = report.startTest("Bad Create artist request test");
		
		ArtistDTO badArtist = new ArtistDTO();
		
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.POST, "/artists/create");
		mockRequest.contentType(MediaType.APPLICATION_JSON);
		mockRequest.header("Key", key);
		mockRequest.content(objectMapper.writeValueAsString(badArtist));
		mockRequest.accept(MediaType.APPLICATION_JSON);
		ResultMatcher statusMatcher = MockMvcResultMatchers.status().isBadRequest();
		mvc.perform(mockRequest).andExpect(statusMatcher);
		
		test.log(LogStatus.PASS, "Ok");
	}

	@Test
	void readArtistTest() throws Exception {
		TestWatch.test = report.startTest("Read artist test - controller integration");
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.GET, "/artists/read");
		mockRequest.accept(MediaType.APPLICATION_JSON);
		ResultMatcher statusMatcher = MockMvcResultMatchers.status().isOk();
		ResultMatcher contentMatcher = MockMvcResultMatchers.content()
				.json(objectMapper.writeValueAsString(validArtistDTOs));
		mvc.perform(mockRequest).andExpect(statusMatcher).andExpect(contentMatcher);
		TestWatch.test.log(LogStatus.PASS, "Ok");
		report.endTest(TestWatch.test);
	}

	@Test
	void readArtistByIdTest() throws Exception {
		TestWatch.test = report.startTest("Read artist by id test - controller integration");
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.GET,
				"/artists/read/id/" + validArtistDTO.getId());
		mockRequest.accept(MediaType.APPLICATION_JSON);
		ResultMatcher statusMatcher = MockMvcResultMatchers.status().isOk();
		ResultMatcher contentMatcher = MockMvcResultMatchers.content()
				.json(objectMapper.writeValueAsString(validArtistDTO));
		mvc.perform(mockRequest).andExpect(statusMatcher).andExpect(contentMatcher);
		TestWatch.test.log(LogStatus.PASS, "Ok");
		report.endTest(TestWatch.test);
	}

	@Test
	void readArtistByNameTest() throws Exception {
		TestWatch.test = report.startTest("Read artist by name test - controller integration");
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.GET,
				"/artists/read/name/" + validArtistDTO.getName());
		mockRequest.accept(MediaType.APPLICATION_JSON);
		ResultMatcher statusMatcher = MockMvcResultMatchers.status().isOk();
		ResultMatcher contentMatcher = MockMvcResultMatchers.content()
				.json(objectMapper.writeValueAsString(validArtistDTO));
		mvc.perform(mockRequest).andExpect(statusMatcher).andExpect(contentMatcher);
		TestWatch.test.log(LogStatus.PASS, "Ok");
		report.endTest(TestWatch.test);
	}

	@Test
	void updateArtistTest() throws Exception {
		TestWatch.test = report.startTest("Update artist test - controller integration");
		ArtistDTO artistToSave = new ArtistDTO("testaa");
		ArtistDTO expectedArtist = new ArtistDTO(validArtistDTO.getId(), "testaa", emptyList);
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.PUT, "/artists/update/1");
		mockRequest.contentType(MediaType.APPLICATION_JSON);
		mockRequest.content(objectMapper.writeValueAsString(artistToSave));
		mockRequest.accept(MediaType.APPLICATION_JSON);
		mockRequest.header("Key", key);
		ResultMatcher statusMatcher = MockMvcResultMatchers.status().isAccepted();
		ResultMatcher contentMatcher = MockMvcResultMatchers.content()
				.json(objectMapper.writeValueAsString(expectedArtist));
		mvc.perform(mockRequest).andExpect(statusMatcher).andExpect(contentMatcher);
		TestWatch.test.log(LogStatus.PASS, "Ok");
		report.endTest(TestWatch.test);
	}

	@Test
	void deleteArtistTest() throws Exception {
		TestWatch.test = report.startTest("Delete artist test - controller integration");
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.DELETE,
				"/artists/delete/" + validArtistDTO.getId());
		mockRequest.contentType(MediaType.APPLICATION_JSON);
		mockRequest.header("Key", key);
		ResultMatcher statusMatcher = MockMvcResultMatchers.status().isNoContent();
		mvc.perform(mockRequest).andExpect(statusMatcher);
		TestWatch.test.log(LogStatus.PASS, "Ok");
		report.endTest(TestWatch.test);
	}
}
