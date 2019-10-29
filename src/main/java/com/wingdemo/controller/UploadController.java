package com.wingdemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wingdemo.model.Booking;
import com.wingdemo.model.PO;
import com.wingdemo.model.Result;
import com.wingdemo.service.AnalysisService;
import com.wingdemo.service.ImportService;
import com.wingdemo.utils.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Controller
public class UploadController {
	// Save the uploaded file to this folder
	@Autowired
	private ImportService importService;
	@Autowired
	private AnalysisService analysisService;

	@GetMapping("/")
	public String index() {
		return "upload";
	}

	@PostMapping("/upload")
	public String singleFileUpload(@RequestParam("file") MultipartFile[] files,  RedirectAttributes redirectAttributes) {
		if (files.length!=2) {
			redirectAttributes.addFlashAttribute("message", "Please select files to upload");
			return "redirect:uploadStatus";
		} 
		MultipartFile bookingsData = files[0];
		MultipartFile posData = files[1];
        if(bookingsData.isEmpty()||posData.isEmpty())
        {
        	redirectAttributes.addFlashAttribute("message", "Please select valid file to upload");
			return "redirect:uploadStatus";
        } 
        
		try {
			InputStream inputStream = bookingsData.getInputStream();
			List<List<Object>> list = importService.getBankListByExcel(inputStream, bookingsData.getOriginalFilename());
			Utils.memoryMonitor();
			inputStream.close();
			Map<String, Map<String, Booking>> bookings = analysisService.convertDataToBookings(list); 
			Utils.memoryMonitor();

			
			inputStream = posData.getInputStream();
			list = importService.getBankListByExcel(inputStream, posData.getOriginalFilename());
			Utils.memoryMonitor();
			inputStream.close();
			Map<String, Map<String, PO>> pos = analysisService.convertDataToPOs(list); 
			Utils.memoryMonitor();
			List<Result> results=analysisService.compareBookingWithPO(pos, bookings);
			 

			redirectAttributes.addFlashAttribute("message",
					"You successfully uploaded files, start analysing.");
		} catch (Exception e) {
			e.printStackTrace();
		}
 
		return "redirect:uploadStatus";
	}

	@GetMapping("/uploadStatus")
	public String uploadStatus() {
		return "uploadStatus";
	}

}