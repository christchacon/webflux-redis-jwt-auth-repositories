package com.tutorial.tutorialwebflux.messages;

import java.io.Serializable;
import java.util.List;

import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tutorial.tutorialwebflux.models.ContactEntity;

//@Configuration
@Component
//@RegisterReflectionForBinding(MessageContactResponse.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageContactResponse implements Serializable {
	private String message;
	private String status;
	private ContactEntity contact;
	private List<ContactEntity> listContacts;

	@JsonProperty
	public ContactEntity getContact() {
		return contact;
	}
	public void setContact(ContactEntity contact) {
		this.contact = contact;
	}
	@JsonProperty
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@JsonProperty
	public String getStatus() {
		return status;
	}
	@JsonProperty
	public void setStatus(String status) {
		this.status = status;
	}
	@JsonProperty
	public List<ContactEntity> getListContacts() {
		return listContacts;
	}
	public void setListContacts(List<ContactEntity> listContacts) {
		this.listContacts = listContacts;
	}
	
}
