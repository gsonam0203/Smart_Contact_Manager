package com.smart.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;





@Entity
@Table(name="USER")
public  class User {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	@NotNull(message="You have to mention your name !")
	@Size(min=2 , max = 25 , message=" Size should be greater than 2 and less than 25")
	private String name;
	@Column(unique=true)
	@Email
	private String email;
	@Pattern(regexp="^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$" , message="a password must be eight characters including one uppercase letter, one special character and alphanumeric characters")
	private String password;
	@Column(length=500)
	private String about;
	private boolean enabled;
	private String role;
	private String imageUrl;
	@javax.validation.constraints.AssertTrue(message="must be agree with terms !")
	private boolean agreement;

	@OneToMany(cascade = CascadeType.ALL , mappedBy = "user" , fetch = FetchType.LAZY )
	private List<contact> contacts = new ArrayList<>();
	
	
	public User() {
		super();
		
	}

	public User(int id, String name, String email, String password, String about, boolean enabled, String role,
			String imageUrl, List<contact> contacts ,boolean agreement) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.about = about;
		this.enabled = enabled;
		this.role = role;
		this.imageUrl = imageUrl;
		this.contacts = contacts;
		this.agreement = agreement;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<contact> getContacts() {
		return contacts;
	}

	public void setContacts(List<contact> contacts) {
		this.contacts = contacts;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public boolean isAgreement() {
		return agreement;
	}

	public void setAgreement(boolean agreement) {
		this.agreement = agreement;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", about=" + about
				+ ", enabled=" + enabled + ", role=" + role + ", imageUrl=" + imageUrl + ", agreement=" + agreement
				+ ", contacts=" + contacts + "]";
	}

	

	
	
	
	

}
