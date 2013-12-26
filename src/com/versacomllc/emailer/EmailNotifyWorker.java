package com.versacomllc.emailer;


import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;

public class EmailNotifyWorker implements Runnable {

	private final EmailerData data;

	public EmailNotifyWorker(EmailerData data) {
		super();
		this.data = data;
	}

	@Override
	public void run() {

		try {
			sendEmail();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void sendEmail() throws MalformedURLException, EmailException {

		String messageBody = data.getConfig().getGreeting() + " "
				+ data.getReceiver().getFirstName() + " "
				+ data.getReceiver().getLastName() + ",";
		
		messageBody = messageBody + "\n\n"
				+ data.getReceiver().getMessageContent();

		if(data.getReceiver().getAddress() != null && !data.getReceiver().getAddress().isEmpty()){
			messageBody = messageBody + "\n\n\n----------------Address to be verified----------------------\n"
					+ data.getReceiver().getAddress();
		}
	    HtmlEmail email = new HtmlEmail();
		email.setSmtpPort(data.getConfig().getPort());
		email.setAuthenticator(new DefaultAuthenticator(data.getConfig()
				.getSenderUser(), data.getConfig().getSenderPass()));
		email.setDebug(false);
		
		email.setHostName(data.getConfig().getMailServer());
		email.setFrom(data.getConfig().getSenderEmail());
		email.setSubject(data.getConfig().getSubject());

		email.setMsg(messageBody);
		email.addTo(data.getReceiver().getEmail());

		List<EmailAttachment> attachments = getEmailWithAttachment(data.getReceiver().getFilePaths());
		for(EmailAttachment attachment: attachments){
			email.attach(attachment);
		}
		email.send();
	}
	
	private List<EmailAttachment> getEmailWithAttachment(List<String> files){
	
		List<EmailAttachment> attachments = new ArrayList<EmailAttachment>();
		
		if(files == null) return attachments;
		
		for(String filePath: files){
			File f = new File(filePath);
			EmailAttachment attachment = new EmailAttachment();
			attachment.setPath(filePath);
			attachment.setDisposition(EmailAttachment.ATTACHMENT);
			attachment.setDescription(f.getName());
			attachment.setName(f.getName());
			attachments.add(attachment);
		}
		
		return attachments;
	}

}
