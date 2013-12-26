package com.versacomllc.emailer;

import java.util.List;

public class EmployeeInfo {

	private String firstName;
	
	private String lastName;
	
	private String phone;
	
	private String email;
	
	private boolean sent;

	private String messageContent;
	
	private List<String> filePaths;
	
	private String address;
	
	public EmployeeInfo(String firstName, String lastName, String phone,
			String email, boolean sent, String messageContent,String address) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.email = email;
		this.sent = sent;
		this.messageContent = messageContent;
		this.address = address;
	}

	public String getMessageContent() {
		return messageContent;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isSent() {
		return sent;
	}

	public void setSent(boolean sent) {
		this.sent = sent;
	}

	@Override
	public String toString() {
		return "EmployeeInfo [firstName=" + firstName + ", lastName="
				+ lastName + ", phone=" + phone + ", email=" + email
				+ ", sent=" + sent + "]";
	}
	
	public boolean isValid(){
		return !email.isEmpty();
	}

	public List<String> getFilePaths() {
		return filePaths;
	}

	public void setFilePaths(List<String> filePaths) {
		this.filePaths = filePaths;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
