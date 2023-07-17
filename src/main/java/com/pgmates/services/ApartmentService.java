package com.pgmates.services;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pgmates.dtos.ApartmentDTO;
import com.pgmates.dtos.ApartmentResponseDTO;
import com.pgmates.dtos.SearchDTO;
import com.pgmates.models.Apartment;
import com.pgmates.repository.ApartmentRepository;
import com.pgmates.utils.StorageService;

@Service
public class ApartmentService {
	@Autowired private ApartmentRepository arepo;
	@Autowired private OwnerService oservice;
	@Autowired private BookingService bservice;
	@Autowired private StorageService storageService;
	
	public void saveApartment(ApartmentDTO dto) {
		Apartment ap=new Apartment();
		BeanUtils.copyProperties(dto, ap);
		String pic1name=storageService.store(dto.getPic1());
		ap.setPhoto1(pic1name);
		String pic2name=storageService.store(dto.getPic2());
		ap.setPhoto2(pic2name);
		String pic3name=storageService.store(dto.getPic3());
		ap.setPhoto3(pic3name);
		String pic4name=storageService.store(dto.getPic4());
		ap.setPhoto4(pic4name);
		ap.setOwner(oservice.findById(dto.getOwnerid()));
		arepo.save(ap);
	}
	
	public List<Apartment> listAll(){
		return arepo.findByIsactive(true);
	}
	
	public List<Apartment> search(SearchDTO dto){
		if(dto.getGender().equals("All") && dto.getFurnish().equals("All") && dto.getAtype().equals("All"))			
			return arepo.findByIsactive(true);
		else if(dto.getGender().equals("All") && dto.getFurnish().equals("All") && !dto.getAtype().equals("All"))
			return arepo.findByFlattypeAndIsactive(dto.getAtype(),true);
		else if(dto.getGender().equals("All") && !dto.getFurnish().equals("All") && dto.getAtype().equals("All"))
			return arepo.findByFurnishtypeAndIsactive(dto.getFurnish(),true);
		else if(!dto.getGender().equals("All") && dto.getFurnish().equals("All") && dto.getAtype().equals("All"))
			return arepo.findByGenderAndIsactive(dto.getGender(),true);
		else if(!dto.getGender().equals("All") && !dto.getFurnish().equals("All") && dto.getAtype().equals("All"))
			return arepo.findByFurnishtypeAndGenderAndIsactive(dto.getFurnish(),dto.getGender(),true);
		else if(!dto.getGender().equals("All") && dto.getFurnish().equals("All") && !dto.getAtype().equals("All"))
			return arepo.findByGenderAndFlattypeAndIsactive(dto.getGender(),dto.getAtype(),true);
		else if(dto.getGender().equals("All") && !dto.getFurnish().equals("All") && !dto.getAtype().equals("All"))
			return arepo.findByFurnishtypeAndFlattypeAndIsactive(dto.getFurnish(), dto.getAtype(),true);
		else 
			return arepo.findByGenderAndFlattypeAndFurnishtypeAndIsactive(dto.getGender(), 
					dto.getAtype(), dto.getFurnish(),true);
	}
	
	public List<Apartment> listOwnerApartments(int id){
		return arepo.findByOwner(oservice.findById(id));
	}
	
	public ApartmentResponseDTO findById(int id) {
		Apartment ap=arepo.getReferenceById(id);
		long count=bservice.apartmentbookingscount(ap);
		ApartmentResponseDTO dto=new ApartmentResponseDTO();
		BeanUtils.copyProperties(ap, dto);
		dto.setBookings(count);
		return dto;
	}
	
	public void deleteById(int id) {
		Apartment apart=arepo.getReferenceById(id);
		apart.setIsactive(false);
		arepo.save(apart);
	}
	
	public void activateApartment(int id) {
		Apartment apart=arepo.getReferenceById(id);
		apart.setIsactive(true);
		arepo.save(apart);
	}
}
