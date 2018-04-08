package com.lgh.springboot.file.upload.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class FileUploadController {

	@RequestMapping(value = "file", method = RequestMethod.GET)
	public String file() {
		return "/file";
	}

	@RequestMapping(value = "fileUpload", method = RequestMethod.POST)
	@ResponseBody
	public String fileUpload(@RequestParam("fileName") MultipartFile file) {
		if (file.isEmpty()) {
			return "false";
		}
		String fileName = file.getOriginalFilename();
		int size = (int) file.getSize();
		System.out.println(fileName + "-->" + size);

		String path = "D:/Users/liugh22/app/test_file_upload";
		File dest = new File(path + "/" + fileName);
		if (!dest.getParentFile().exists()) {
			dest.getParentFile().mkdir();
		}
		try {
			file.transferTo(dest);
			return "true";
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "false";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "false";
		}
	}

	@RequestMapping(value = "multifile", method = RequestMethod.GET)
	public String multifile() {
		return "/multifile";
	}

	@RequestMapping(value = "multifileUpload", method = RequestMethod.POST)
	@ResponseBody
	public String multifileUpload(@RequestParam("fileName") List<MultipartFile> files) {
		// public String multifileUpload(HttpServletRequest request) {
		// List<MultipartFile> files = ((MultipartHttpServletRequest)
		// request).getFiles("fileName");

		if (files.isEmpty()) {
			return "false";
		}

		String path = "D:/Users/liugh22/app/test_file_upload";

		for (MultipartFile file : files) {
			String fileName = file.getOriginalFilename();
			int size = (int) file.getSize();
			System.out.println(fileName + "-->" + size);

			if (file.isEmpty()) {
				return "false";
			} else {
				File dest = new File(path + "/" + fileName);
				if (!dest.getParentFile().exists()) {
					dest.getParentFile().mkdir();
				}
				try {
					file.transferTo(dest);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return "false";
				}
			}
		}
		return "true";
	}

	@RequestMapping(value = "download", method = RequestMethod.GET)
	public String downLoad(HttpServletResponse response) {
		String filename = "1.png";
		String filePath = "D:/Users/liugh22/app/test_file_upload";
		File file = new File(filePath + "/" + filename);
		if (file.exists()) {
			response.setContentType("application/force-download");
			response.setHeader("Content-Disposition", "attachment;fileName=" + filename);

			byte[] buffer = new byte[1024];
			FileInputStream fis = null;
			BufferedInputStream bis = null;
			OutputStream os = null;

			try {
				os = response.getOutputStream();
				fis = new FileInputStream(file);
				bis = new BufferedInputStream(fis);
				int i = bis.read(buffer);
				while (i != -1) {
					os.write(buffer);
					i = bis.read(buffer);
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("----------file download" + filename);
			try {
				bis.close();
				fis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
}
