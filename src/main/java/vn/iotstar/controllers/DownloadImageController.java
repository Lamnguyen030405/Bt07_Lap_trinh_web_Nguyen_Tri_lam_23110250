package vn.iotstar.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.utils.path.Constant;

@Controller
public class DownloadImageController {

	@GetMapping("/image")
	public void image(@RequestParam("fname") String fileName, HttpServletResponse response) {
	    try {
	        if (fileName == null || fileName.isBlank()) {
	            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "File name is empty");
	            return;
	        }
	        File file = new File(Constant.DIR + "/", fileName);
	        if (!file.exists()) {
	            response.sendError(HttpServletResponse.SC_NOT_FOUND, "File not found: " + file.getAbsolutePath());
	            return;
	        }
	        String contentType = Files.probeContentType(file.toPath());
	        response.setContentType(contentType != null ? contentType : "application/octet-stream");

	        try (FileInputStream in = new FileInputStream(file);
	             OutputStream out = response.getOutputStream()) {
	            byte[] buffer = new byte[1024];
	            int bytesRead;
	            while ((bytesRead = in.read(buffer)) != -1) {
	                out.write(buffer, 0, bytesRead);
	            }
	            out.flush();
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        try {
	            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error serving image: " + e.getMessage());
	        } catch (IOException ioException) {
	            ioException.printStackTrace();
	        }
	    }
	}
}
