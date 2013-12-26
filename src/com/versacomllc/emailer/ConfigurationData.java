package com.versacomllc.emailer;

public class ConfigurationData {

	private String subject;
	
	private String greeting;
	
	private int port;
	
	private String senderPass;
	
	private String senderEmail;
	
	private String senderUser;
	
	private String mailServer;
	
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getGreeting() {
		return greeting;
	}
	public void setGreeting(String greeting) {
		this.greeting = greeting;
	}

	
	public String getMailServer() {
		return mailServer;
	}

	public void setMailServer(String mailServer) {
		this.mailServer = mailServer;
	}


	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}


	public String getSenderUser() {
		return senderUser;
	}

	public void setSenderUser(String senderUser) {
		this.senderUser = senderUser;
	}

	public String getSenderPass() {
		return senderPass;
	}

	public void setSenderPass(String senderPass) {
		this.senderPass = senderPass;
	}

	public String getSenderEmail() {
		return senderEmail;
	}

	public void setSenderEmail(String senderEmail) {
		this.senderEmail = senderEmail;
	}
	

}
