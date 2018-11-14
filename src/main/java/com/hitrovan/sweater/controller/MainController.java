package com.hitrovan.sweater.controller;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.hitrovan.sweater.domain.Message;
import com.hitrovan.sweater.domain.User;
import com.hitrovan.sweater.repository.MessageRepository;

@Controller
public class MainController {
	@Autowired
	private MessageRepository messageRepo;
	
	@Value("${upload.path}")
	private String uploadPath;
	
    @GetMapping("/")
    public String greeting(Map<String, Object> model) {
        return "greeting";
    }
    
    @GetMapping("/main")
    public String main(@RequestParam(required = false) String senderName, Model model) {
    	Iterable<Message> messages = messageRepo.findAll();
    	
    	if (senderName != null && !senderName.isEmpty()) {
    		messages = messageRepo.findBySenderName(senderName);
    	}
    	
    	model.addAttribute("messages", messages);
    	model.addAttribute("senderName", senderName);
    	return "main";
    }
    
    @PostMapping("/main")
    public String addMessage(
    		@AuthenticationPrincipal User user,
    		@RequestParam String text, 
    		@RequestParam String senderName, 
    		Map<String, Object> model,
    		@RequestParam("file") MultipartFile file
    ) throws IOException {
    	Message message = new Message(text, senderName, user);
    	if (file != null && !file.getOriginalFilename().isEmpty()) {
    		File uploadDir = new File(uploadPath);
    		if (!uploadDir.exists()) {
    			uploadDir.mkdir();
    		}
    		
    		String uuidForFile = UUID.randomUUID().toString();
    		String resultFileName =  uuidForFile + "." + file.getOriginalFilename();
    		
    		file.transferTo(new File(uploadPath + "/" + resultFileName));
    		
    		message.setFilename(resultFileName);
    	}
    	messageRepo.save(message);
    	
    	Iterable<Message> messages = messageRepo.findAll();
    	model.put("messages", messages);
    	return "main";
    }
}