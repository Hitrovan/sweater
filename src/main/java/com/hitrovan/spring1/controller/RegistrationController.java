package com.hitrovan.spring1.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.hitrovan.spring1.domain.User;
import com.hitrovan.spring1.service.UserService;

@Controller
public class RegistrationController {
	@Autowired
	private UserService userService;
	
	@GetMapping("/registration")
	public String registration() {
		return "registration";
	}
	
	@PostMapping("/registration")
	public String newUser(User user, Map<String, Object> model) {
		if(!userService.newUser(user)) {
			model.put("message", "User with this name already exists!");
			return "registration";
		}
		
		return "redirect:/login";
	}
	
	@GetMapping("/activate/{code}")
	public String activate(Model model, @PathVariable String code) {
		boolean isActivated = userService.activateUser(code);
		
		if (isActivated) {
			model.addAttribute("message", "Activation successeful!");
		}
		else {
			model.addAttribute("message", "Activation code is not found.");
		}
		
		return "login";
	}
}