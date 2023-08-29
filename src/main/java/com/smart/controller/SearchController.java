package com.smart.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.entities.contact;
import java.util.*;

@RestController
public class SearchController {
	@Autowired
	private ContactRepository conRepo;
	@Autowired
	private UserRepository userRepo;
      
	// search controller
	@GetMapping("/search/{query}")
	public ResponseEntity<?> handler(@PathVariable("query") String query , Principal principal){
		
		System.out.println(query);
		User user = this.userRepo.getUserByUserName(principal.getName());
		List<contact>  contacts  = this.conRepo.findByNameContainingAndUser(query, user);
		return ResponseEntity.ok(contacts);
	
		
		
	}
}
