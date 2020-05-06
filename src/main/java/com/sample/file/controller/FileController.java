package com.sample.file.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileController {


	@PostMapping("/uploadFile")
	public ResponseEntity<String> uploadFile(@RequestParam String fileName, @RequestParam String filePath,
			@RequestParam("file") MultipartFile file) {
		boolean isValidFile = false;
		if (fileName != null && filePath != null) {

			File file2 = new File(filePath.concat("/").concat(fileName));
			InputStream inputStream = null;
			try {
				if(file2.createNewFile()) {
					inputStream = new FileInputStream(file2);
					inputStream.read(file.getBytes());
					isValidFile = true;
				}else {
					isValidFile = false;
				}

			} catch (IOException e) {
				isValidFile = false;
				e.printStackTrace();
			}
		}
		
		if (isValidFile) {
			return ResponseEntity.ok().body("success");
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/downloadFile")
	public ResponseEntity<Resource> downloadFile(@RequestParam String fileName, @RequestParam String filePath) {
		boolean isValidFile = false;
		File file = null;
		byte[] b = new byte[1024];
		if (fileName != null && filePath != null) {
			file = new File(filePath.concat("/").concat(fileName));
			if (file.exists() && file.canRead()) {
				InputStream inputStream = null;
				try {
					inputStream = new FileInputStream(file);
					inputStream.read(b);
					isValidFile = true;
				} catch (IOException e) {
					isValidFile = false;
					e.printStackTrace();
				}
			} else {
				isValidFile = false;
			}
		}
		if (isValidFile) {
			return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN)
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
					.body(new ByteArrayResource(b));
		} else {
			return ResponseEntity.notFound().build();
		}

	}

}
