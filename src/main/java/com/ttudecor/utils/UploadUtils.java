package com.ttudecor.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class UploadUtils {
	public String uploadImage(MultipartFile file, String uploadPath, String filename) {
		File uploadDir = new File(uploadPath);
		
		if (!uploadDir.exists()) {
			uploadDir.mkdirs();
		}
		
		String oFileName = file.getOriginalFilename();
		
		if(oFileName != null && oFileName.length() > 0) {
			String fileExt = oFileName.substring(oFileName.lastIndexOf("."));
			String newFileName = filename + fileExt;
			
			try {
				File saveFile = new File(uploadDir.getAbsolutePath() + File.separator + newFileName);
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(saveFile));
				
				stream.write(file.getBytes());
				stream.close();
				
				System.out.println("Save file: " + saveFile);
				
				return newFileName;
			} catch (Exception e) {
				System.out.println("Error Write file: " + oFileName);
			}
		}
		
		return null;
	}
	
	public void deleteFolder(String folderPath) {
		File file = new File(folderPath);
		
		if(file.exists()) {
			String[] childs = file.list();
			for(String child : childs) {
				File childFile = new File(file, child);
				childFile.delete();
				System.out.println("Delete file: " + childFile);
			}
			
			file.delete();
			System.out.println("Delete folder: " + file);
		}
	}

}
