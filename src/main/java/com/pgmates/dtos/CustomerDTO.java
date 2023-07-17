package com.pgmates.dtos;

import org.springframework.web.multipart.MultipartFile;

import com.pgmates.models.Customer;

public class CustomerDTO extends Customer {

	private MultipartFile photo;

	public MultipartFile getPhoto() {
		return photo;
	}

	public void setPhoto(MultipartFile photo) {
		this.photo = photo;
	}
	
	
}
