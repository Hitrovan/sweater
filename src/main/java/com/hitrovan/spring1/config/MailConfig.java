package com.hitrovan.spring1.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {
	@Value("${spring.mail.host}")
	private String host;
	
	@Value("${spring.mail.port}")
	private int port;

	@Value("${spring.mail.protocol}")
	private String protocol;
	
	@Value("${spring.mail.username}")
	private String username;
	
	@Value("${spring.mail.password}")
	private String password;
	
	@Value("${spring.mail.properties.mail.smtp.auth}")
	private String auth;
	
	@Value("${spring.mail.properties.mail.smtp.quitwait}")
	private String quitwait;

	@Value("${spring.mail.properties.mail.smtp.starttls.enable}")
	private String enable;
	
	@Value("${spring.mail.properties.mail.smtp.socketFactory.fallback}")
	private String fallback;

	@Value("${mail.debug}")
	private String debug;
	
	@Bean 
	public JavaMailSender getMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(host);
		mailSender.setPort(port);
		mailSender.setProtocol(protocol);
		mailSender.setUsername(username);
		mailSender.setPassword(password);
		
		Properties properties = mailSender.getJavaMailProperties();
		properties.setProperty("mail.smtp.auth", auth);
		properties.setProperty("mail.smtp.quitwait", quitwait);
		properties.setProperty("mail.smtp.starttls.enable", enable);
		properties.setProperty("mail.smtp.socketFactory.fallback", fallback);
		properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		properties.setProperty("mail.debug", debug);
		
		return mailSender;
	};
}