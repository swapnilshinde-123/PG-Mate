package com.pgmates.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pgmates.dtos.FeedbackDTO;
import com.pgmates.exceptions.ResourceNotFoundException;
import com.pgmates.models.Apartment;
import com.pgmates.models.Customer;
import com.pgmates.models.Feedback;
import com.pgmates.repository.FeedbackRepository;

@Service
public class FeedbackService {

	@Autowired private FeedbackRepository repo;
	@Autowired private CustomerService csrv;
	@Autowired private BookingService bsrv;
	
	public void saveFeedback(FeedbackDTO dto) throws ResourceNotFoundException{
		Customer cust=csrv.findById(dto.getUserid());
		Apartment ap=bsrv.currentBooking(dto.getUserid()).getApartment();
		System.out.println(ap);
		Feedback fb=new Feedback();
		fb.setApartment(ap);
		fb.setCustomer(cust);
		fb.setDescr(dto.getDescr());
		repo.save(fb);
	}
	
	public List<Feedback> listAll(){
		return repo.findAll();
	}
}
