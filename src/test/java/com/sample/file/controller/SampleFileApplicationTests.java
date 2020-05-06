package com.sample.file.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import com.sample.file.SampleFileApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SampleFileApplication.class)
@WebAppConfiguration
public class SampleFileApplicationTests {


	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Before
	public void setUp() throws NoSuchFieldException, IllegalAccessException {
		mockMvc = webAppContextSetup(webApplicationContext).build();
	}

	@Test
	public void successUpload() throws Exception {
		final InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("sample2.txt");
		final MockMultipartFile avatar = new MockMultipartFile("file", "sample2.txt", "txt/html", inputStream);
		@SuppressWarnings("deprecation")
		final MvcResult result = mockMvc.perform(fileUpload("/uploadFile").file(avatar).param("fileName", "sample2.txt")
				.param("filePath", "C:/Users/lokesh")).andExpect(status().isOk()).andReturn();

	}
	@Test
	public void failureUpload() throws Exception {
		final InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("sample.txt");
		final MockMultipartFile avatar = new MockMultipartFile("file", "sample.txt", "txt/html", inputStream);
		@SuppressWarnings("deprecation")
		final MvcResult result = mockMvc.perform(fileUpload("/uploadFile").file(avatar).param("fileName", "sample.txt")
				.param("filePath", "C:/Users/lokesh")).andExpect(status().isNotFound()).andReturn();

	}

	@Test
	public void successDownload() throws Exception {
		final MvcResult result = mockMvc
				.perform(get("/downloadFile").param("fileName", "sample.txt").param("filePath", "C:/Users/lokesh"))
				.andExpect(status().isOk()).andReturn();

	}

	@Test
	public void failureDownload() throws Exception {
		final MvcResult result = mockMvc
				.perform(get("/downloadFile").param("fileName", "sample1.txt").param("filePath", "C:/Users/lokesh"))
				.andExpect(status().isNotFound()).andReturn();

	}
}
