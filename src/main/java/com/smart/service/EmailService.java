package com.smart.service;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

@Service 
//or @Controller
public class EmailService {
	
	public boolean sendEmail(String to , String subject , String message) {
		boolean flag = false;
		
		// from g-email account
	    String from1 = "smartcontact0203@gmail.com";
		//variable for gmail
		String host = "smtp.gmail.com";
		
		// step:1  get the system properties and setting the info
		Properties properties =  System.getProperties();
		System.out.println("system properties:"+(properties));
		// setting important information for property object
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.auth", "true");
		
	// step 2 : to get session object
		Session session = Session.getInstance(properties,new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("smartcontact0203@gmail.com" , "elptunjxtunkxwtn" );
			}
			
			
		});
		
		session.setDebug(true);
		
	
		
		try {
			//step 3 : compose the message text , media
			MimeMessage m = new MimeMessage(session);
			// from email
			m.setFrom(from1);
			
			//adding recipient to message
			m.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			
			// adding subject to message
			m.setSubject(subject);
			
			// adding text to message
			m.setText(message);
			
	// step 4 : send the messageusing transport class
			Transport.send(m);
			
			flag = true;
			
	//success message
			System.out.println("successfully your mail is send !");
			
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		
		
		return flag;
		
	}

}
