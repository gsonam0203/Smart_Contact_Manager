package com.smart.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smart.entities.User;
import com.smart.entities.contact;
public interface ContactRepository extends JpaRepository<contact,Integer>{
	 
	// pagination
	@Query("from contact as c where c.user.id = :userId")
	// use Page , Pageable
	public Page<contact> findContactsdByUser(@Param("userId") int userId ,Pageable pageable );
	
	// search 
	public List<contact> findByNameContainingAndUser(String name , User user);

}
