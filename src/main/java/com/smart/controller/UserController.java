package com.smart.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.entities.contact;
import com.smart.helper.Message;

import org.springframework.core.io.Resource;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private ContactRepository contactRepo;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	
	
	// common method for all pages
	@ModelAttribute
	public void addCommonMethod(Model m , Principal principal) {
		String username  = principal.getName();
		User user = userRepo.getUserByUserName(username);
		m.addAttribute("person", user);
		
   }
	
	// open user page
	@RequestMapping("/dashboard")
	public String dashboard() {
		
								
	    return "user/user_dashboard";
	} 
	
	// open add contact form
	@RequestMapping("/add-contact")
	public String addContactHandler(Model m) {
		m.addAttribute("title", "Add Contact");
		m.addAttribute("contact", new contact());
		return "user/addcontact";
	}
	
	// redirect to process form of add contact form
	@PostMapping("/process-data")
	public String processData(@ModelAttribute contact contact1 , Model m ,@RequestParam("profileImage") MultipartFile file  ,Principal principal , HttpSession session) {
		
		//after submission , new form shows automatically
		m.addAttribute("contact", new contact());
		
		try {
			
		// database  to save contact details in db and in user table
		String name  = principal.getName();
		User user1 = userRepo.getUserByUserName(name);
		
		// uploading the image file to folder
		if(file.isEmpty()) {
			// return a user friendly message 
			System.out.println("file is empty");
			// default image
			contact1.setImage("contact.png");
		}
		else {
			//update name in database of image field
			contact1.setImage(file.getOriginalFilename());
			
		
			
			//upload file in folder static/images
			File saveFile = new ClassPathResource("static/images").getFile();
			Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
			Files.copy(file.getInputStream(), path,StandardCopyOption.REPLACE_EXISTING);
		}
		//these 2  below lines are important 
		user1.getContacts().add(contact1);
		contact1.setUser(user1);
		// saving the data in db
		this.userRepo.save(user1);		
		// printing data after successful add
		System.out.println("contact is added");
		System.out.println("Contact data :" + contact1);
		System.out.println("User data :" + user1);
		
		session.setAttribute("message" , new Message("Your contact is added ! Add more.." , "alert-success"));
		
		}catch(Exception e) {
			e.printStackTrace();
			session.setAttribute("message" , new Message("Something went wrong ! try again.." , "alert-danger"));
		}
				
		return "user/addcontact";
	}
	
	// show contacts
	// per page 5[n] , current page 0 index
	@GetMapping("/show-contacts/{page}")
	public String showContactsHandler(@PathVariable("page") Integer page, Model m , Principal principal) {
		//contact ki list bhejni h
		String name = principal.getName();
		User user = this.userRepo.getUserByUserName(name);
		//pagination
		Pageable pageable = PageRequest.of(page,5);		
		Page<contact> contacts = this.contactRepo.findContactsdByUser(user.getId(),pageable);
		// sending dynamically values
		m.addAttribute("contacts", contacts);
		m.addAttribute("currentP", page);
		m.addAttribute("totalP", contacts.getTotalPages());
		return "user/show_contacts";
	}
	
    //show contact details
	@RequestMapping("/{id}/contact-details")
	public String showContactDetailsHandler(@PathVariable("id") Integer id , Model m , Principal principal) {		
		System.out.println(id);
		
		Optional<contact> contactop = this.contactRepo.findById(id);
		contact contact = contactop.get();
		
		String name = principal.getName();
		User userName = this.userRepo.getUserByUserName(name);
		
		if(userName.getId()==contact.getUser().getId()) {
			m.addAttribute("info", contact);
		}

		return "/user/contact_details";
	}
	
	// delete controller 
	@RequestMapping("/delete/{cId}")
	public String deleteContoller(@PathVariable("cId") Integer cId , Model m ,HttpSession session , Principal principal) throws IOException {
		String name = principal.getName();
		User user = this.userRepo.getUserByUserName(name);
		contact con = this.contactRepo.findById(cId).get();
		
		if(user.getId()==con.getUser().getId()) {
			con.setUser(null);

			
			this.contactRepo.delete(con);
		}

		session.setAttribute("message",new Message("contact deleted successfully ..","alert-success"));
		
		return "redirect:/user/show-contacts/{cId}";
	}
	
	
	
	//update contact form 
	@PostMapping("/update-contact/{id}")
	public String updateContactHandler(@PathVariable("id") Integer id ,@ModelAttribute("contact") contact contact , Model m , Principal principal) {
		// fetching contact info from contact id
		String name = principal.getName();
		User user = this.userRepo.getUserByUserName(name);
		contact con = this.contactRepo.findById(id).get();

		// sending contact object to form
		m.addAttribute("contact", con);
		
		return "/user/update_contact";
	}
	
	// update form processsing
	@PostMapping("/process-update")
	public String processUpdateHandler(@ModelAttribute("contact") contact contact ,@RequestParam("profileImage") MultipartFile file , HttpSession session , Principal principal) {
		try {
			 contact oldc = this.contactRepo.findById(contact.getcId()).get();

		        if (!file.isEmpty()) {
		            // Delete old file
		        	/*File deleteFile = new ClassPathResource("static/images").getFile();
		        	File getDelete  = new File(deleteFile , oldc.getImage());
		        	
		        	
		        	getDelete.delete(); */

		

		          //upload file in folder static/images
					File saveFile = new ClassPathResource("static/images").getFile();
					Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
					Files.copy(file.getInputStream(), path,StandardCopyOption.REPLACE_EXISTING);
					
			      // Update image information
		            contact.setImage(file.getOriginalFilename());
		        }
			// agr file empty h to purane ko hi naye m daldo
			else {
				contact.setImage(oldc.getImage());
				
				Resource saveFileResource = new ClassPathResource("static/images/" + oldc.getImage());
	            Files.copy(file.getInputStream(), saveFileResource.getFile().toPath(), StandardCopyOption.REPLACE_EXISTING);
		
			}
		
			User user = this.userRepo.getUserByUserName(principal.getName());
		    contact.setUser(user);
			this.contactRepo.save(contact);
			
			session.setAttribute("message",new Message("Your contact is updated successfully" , "alert-success"));
			
		}
		catch(Exception e) {
			e.printStackTrace();
			session.setAttribute("message",new Message("Something went wrong..try again !" , "alert-danger"));
		}
		// After updating the contact, redirect to the contact listing page
		return "redirect:/user/show-contacts/0"; // Redirect to the first page of contacts

	}
	
	//profile page
	@GetMapping("/profile")
	public String profilHandler(Model m ) {
		return "/user/profile";
	}
	
	// settings page
	@RequestMapping("/settings")
	public String settingsHandler() {
		
		return "/user/settings";
		 
	}
	//change password proces form
	@PostMapping("/change-password")
	public String changePasswordHandler(@RequestParam("old") String oldPassword ,@RequestParam("new1") String newPassword , Model m , HttpSession session , Principal principal) {
		
		String name = principal.getName();
		User user = this.userRepo.getUserByUserName(name);
		if(this.passwordEncoder.matches(oldPassword, user.getPassword())) {
			user.setPassword(this.passwordEncoder.encode(newPassword));
		    this.userRepo.save(user);
		    session.setAttribute("message", new Message("Your password has been successfully changed .." , "alert-success"));
		}else {
			session.setAttribute("message", new Message("Enter correct old password" , "alert-danger"));
			return "user/settings";
		}
		
		
		return "/user/user_dashboard";
		 
	}
}
