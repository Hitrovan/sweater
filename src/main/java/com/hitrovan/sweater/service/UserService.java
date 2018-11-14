package com.hitrovan.sweater.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.hitrovan.sweater.domain.Role;
import com.hitrovan.sweater.domain.User;
import com.hitrovan.sweater.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private MailSenderService mailSender;
	@Autowired
	private PasswordEncoder passwordEncoder;

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepo.findByUsername(username);
	}
	
	public boolean newUser(User user) {
        User userFromDb = userRepo.findByUsername(user.getUsername());
		
		if(userFromDb != null) {
			return false;
		}
		
		user.setActive(true);
		user.setRoles(Collections.singleton(Role.USER));
		user.setActivationCode(UUID.randomUUID().toString());
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userRepo.save(user);
		
		sendMessage(user);
		
		return true;
	}
	
	public boolean activateUser(String code) {
		User user = userRepo.findByActivationCode(code);
		
		if (user == null) {
			return false;
		}
		
		user.setActivationCode(null);
		userRepo.save(user);
		
		return true;
	}
	
	public List<User> findAll() {
		return userRepo.findAll();
	}
	
	public void saveUser(User user, String username, Map<String, String> form) {
		user.setUsername(username);
		
		Set<String> roles = Arrays.stream(Role.values())
				.map(Role::name)
				.collect(Collectors.toSet());
		
		user.getRoles().clear();
		for (String key : form.keySet()) {
			if (roles.contains(key)) {
				user.getRoles().add(Role.valueOf(key));
			}
		}
		
		userRepo.save(user);
	}

	public void updateProfile(User user, String password, String email) {
		String userEmail = user.getEmail();
		
		boolean isEmailChanged = (email != null && !email.equals(userEmail)) || 
				            (userEmail != null && !userEmail.equals(email));
		if (isEmailChanged) {
			user.setEmail(email);
			if (!StringUtils.isEmpty(email)) {
				user.setActivationCode(UUID.randomUUID().toString());
			}
		}
		
		if (!StringUtils.isEmpty(password)) {
			user.setPassword(password);
		}
		
		userRepo.save(user);
		if (isEmailChanged) {
			sendMessage(user);
		}
	}
	
	private void sendMessage(User user) {
		if(!StringUtils.isEmpty(user.getEmail())) {
			String message = String.format(
				"Hello, %s! \n" +
			    "Welcome to NorthernStar. Please, visit next link to verify your E-mail address: " +
				"http://localhost:8080/activate/%s",
				user.getUsername(), 
				user.getActivationCode()
			);
			mailSender.send(user.getEmail(), "Activation code", message);
		}
	}
}