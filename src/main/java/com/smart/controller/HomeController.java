package com.smart.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.helper.Message;



@Controller
public class HomeController {
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	// creating db interface object
	@Autowired
	private UserRepository userRepo;
	
	// home page
	@RequestMapping("/")
	public String homeHandler() {
	
		System.out.println("Home Handler Is Running");
		return "home";
		
	}
	
	// about page
	@RequestMapping("/about2")
	public String aboutHandler() {
	
		System.out.println("About Handler Is Running");
		return "about2";
		
	}
	
	//signup page
	@RequestMapping("/signup")
	public String signupHandler(Model m) {
	
		System.out.println("signup Handler Is Running");
		m.addAttribute("user", new User());
		return "signup";		
	}
	
	// signup success page do_register
	@RequestMapping(path="/do_register" , method = RequestMethod.POST)
	public String doRegisterHandler( @Valid @ModelAttribute("user") User user ,BindingResult result  ,@RequestParam(value="agreement" , defaultValue="false") boolean agreement  ,
			Model m) { //
	    
		//checking validation for active
		try {
			if( !agreement ||  result.hasErrors() ) { 
				throw new Exception("You have to click on the checkbox !!");
			}
			// checking server side validation
			if(result.hasErrors()) {
				m.addAttribute("user", user);
				return "signup";
			}
			//adding other fields in db
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImageUrl("/images/demo.png");
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			
          	// saving the updated object						
			User res = this.userRepo.save(user);
			
			// adding new fresh signup page after successfull signup
			 m.addAttribute("user",new User());
			 // showing message of successfull form submission
			 m.addAttribute("message", new Message("Successfully Registered!! ", "alert-success"));
			 return "signup";
		
		}catch(Exception e) {
			e.printStackTrace();
			//adding previous save object details
			m.addAttribute("user",user);
			// showing errors in the register page
		    m.addAttribute("message", new Message("Something went wrong !" + e.getMessage(), "alert-danger"));
			return "signup";
		}
	}
	
	
	//login page
	@RequestMapping("/signin")
	public String customloginHandler(Model m) {
		m.addAttribute("login","this is login page !" );
		System.out.println("login Handler Is Running");
		return "login";
		
		} 

}
