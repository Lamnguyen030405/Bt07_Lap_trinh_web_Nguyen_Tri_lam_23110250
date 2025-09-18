package vn.iotstar.utils;

import java.io.File;
import java.io.IOException;

import jakarta.servlet.http.Part;

public class UploadUtils {

	public static String processUploadFile(Part part, String uploadDir) throws IOException {
	    if (part == null || part.getSize() == 0) {
	        System.err.println("No file uploaded or file is empty");
	        return null;
	    }

	    String fileName = System.currentTimeMillis() + "_" + extractFileName(part);
	    File dir = new File(uploadDir);
	    if (!dir.exists()) {
	        if (!dir.mkdirs()) {
	            throw new IOException("Failed to create directory: " + uploadDir);
	        }
	    }

	    File file = new File(uploadDir, fileName);
	    part.write(file.getAbsolutePath());

	    // Kiểm tra xem tệp đã được lưu thành công chưa
	    if (!file.exists()) {
	        throw new IOException("Failed to save file: " + file.getAbsolutePath());
	    }

	    return new File(uploadDir).getName() + "/" + fileName;
	}

    private static String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        for (String token : contentDisp.split(";")) {
            if (token.trim().startsWith("filename")) {
                return token.substring(token.indexOf("=") + 2, token.length() - 1).replace("\\", "/");
            }
        }
        return null;
    }
}
