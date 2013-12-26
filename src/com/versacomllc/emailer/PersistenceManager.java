package com.versacomllc.emailer;

import java.io.File;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class PersistenceManager {

	public static void saveConfiguration(ConfigurationData conf)
			throws IOException {

		Properties properties = new Properties();
		
		properties.setProperty("email.server", conf.getMailServer());
		properties.setProperty("email.user", conf.getSenderUser());
		properties.setProperty("email.password", conf.getSenderPass());
		properties.setProperty("email.address", conf.getSenderEmail());
		properties.setProperty("email.subject", conf.getSubject());
		properties.setProperty("email.greeting", conf.getGreeting());
		properties.setProperty("email.port", String.valueOf(conf.getPort()));

		File file = new File("application.properties");

		FileOutputStream fileOut = new FileOutputStream(file);
		properties.store(fileOut, "Application properties");
		fileOut.close();
		System.out.println(file.getAbsolutePath());

	}

	public static ConfigurationData getConfiguration() throws FileNotFoundException, IOException {
		Properties properties = new Properties();

		properties.load(new FileInputStream("application.properties"));

		final String user = (String) properties.get("email.user");
		final String password = (String) properties.get("email.password");
		final String email = (String) properties.get("email.address");
		final String subject = (String) properties.get("email.subject");
		final String greeting = (String) properties.get("email.greeting");
		final String server = (String) properties.get("email.server");
		final String port = (String) properties.get("email.port");
		ConfigurationData conf = new ConfigurationData();
		conf.setGreeting(greeting);
		conf.setSubject(subject);
		conf.setSenderEmail(email);
		conf.setSenderPass(password);
		conf.setSenderUser(user);
		conf.setMailServer(server);
		try{
			conf.setPort(Integer.parseInt(port));
		}
		catch(Exception ex)
		{
			
		}
	
		return conf;
	}
}
