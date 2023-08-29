package com.smart.controller;

import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.helper.Message;
import com.smart.service.EmailService;


@Controller
public class ForgetController {
	
	Random random = new Random(1000);
	@Autowired
	private UserRepository userRepo ;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private BCryptPasswordEncoder bcrypt;
	
	//to redirect forget page where you have to send email
	@RequestMapping("/forget")
	public String forgetHandler() {
		return "forget";
	}
	
	// to redirect send otp page where you enter otp 
	@PostMapping("/send-otp")
	public String sendOTPHandler(@RequestParam("email") String email , HttpSession session) {
		System.out.println(email);
		// generating 4 digit otp
		
		int otp = random.nextInt(999999);
		System.out.println(otp);
		
		//sending email
		String subject = "OTP from SCM";
		String message = otp+"";
				
				
		String to = email;
		
		boolean flag = this.emailService.sendEmail(to, subject, message);
		if(flag) {
			session.setAttribute("myOtp", otp);
			session.setAttribute("email", email);
			return "verify_otp";
		}
		else {
			session.setAttribute("message" , new Message("Check your email !!" , "alert-danger"));
			return "forget";
		}
		
	}
	
	// to redirect the change password page where u enter new password 
	@PostMapping("/verify-otp")
	public String verifyOTP(@RequestParam("otp") int otp , HttpSession session) {
		
		int myotp = (int) session.getAttribute("myOtp");
		String email = (String) session.getAttribute("email");
		
		if(myotp == otp) {
			
			// change password form
			 User user = this.userRepo.getUserByUserName(email);
			 if(user==null) {
				 //send error message
				 session.setAttribute("message" , new Message("user does not exists with this  email !" , "alert-danger"));
					
					return "forget";
			 }
			 else {
				 // send change password form
				 return "/change_password";
				 
			 }
			
			
		}
		else {
			
			session.setAttribute("message" , new Message("Enter correct OTP" , "alert-danger"));
			
			return "/verify_otp";
		}
		
		
	}
	
	//to redirect the home page of user 
	@PostMapping("/change_password")
	public String newPasswordHandler(@RequestParam("newpassword") String newpassword , HttpSession session) {
		
		String email = (String) session.getAttribute("email");
		
		User user = this.userRepo.getUserByUserName(email);
		
		user.setPassword(this.bcrypt.encode(newpassword));
		
		this.userRepo.save(user);
		
		return "redirect:/signin?change = Password changed successfully";
		
		
		
		
	}

}
