package com.tutorial.tutorialwebflux.models;

import java.io.Serializable;

import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tutorial.tutorialwebflux.messages.MessageFileResponse;


/**
 * The persistent class for the dto_contact database table.
 * 
 */
@Component
// @Table(name = "tbl_contact", schema = "test")
// @NamedQueries(value = { 
// 	@NamedQuery(name = "Contact.findAll", query = "SELECT c FROM DtoContact c")
	//})
//@NamedQuery(name="DtoContact.findAll", query="SELECT d FROM DtoContact d")
//@RegisterReflectionForBinding(ContactEntity.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@org.springframework.data.relational.core.mapping.Table(name = "tbl_contact", schema = "test")
public class ContactEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@org.springframework.data.annotation.Id
	
	@org.springframework.data.relational.core.mapping.Column(value="id")
	private Long id;

	@org.springframework.data.relational.core.mapping.Column(value="address")
	private String address;

	@org.springframework.data.relational.core.mapping.Column(value="email")
	private String email;

	@org.springframework.data.relational.core.mapping.Column(value="first_name")
	private String firstName;

	@org.springframework.data.relational.core.mapping.Column(value ="gender")
	private String gender;

	@org.springframework.data.relational.core.mapping.Column(value="last_name")
	private String lastName;

		@org.springframework.data.relational.core.mapping.Column(value="phone_number")
	private String phoneNumber;

	public ContactEntity() {
	}
	@JsonProperty
	public Long getId() {
		return this.id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@JsonProperty
	public String getAddress() {
		return this.address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	@JsonProperty
	public String getEmail() {
		return this.email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@JsonProperty
	public String getFirstName() {
		return this.firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	@JsonProperty
	public String getGender() {
		return this.gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	@JsonProperty
	public String getLastName() {
		return this.lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	@JsonProperty
	public String getPhoneNumber() {
		return this.phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	@Override
	public String toString() {
		return "DtoContact [id=" + id + ", address=" + address + ", email=" + email + ", firstName=" + firstName
				+ ", gender=" + gender + ", lastName=" + lastName + ", phoneNumber=" + phoneNumber + "]";
	}

	

}