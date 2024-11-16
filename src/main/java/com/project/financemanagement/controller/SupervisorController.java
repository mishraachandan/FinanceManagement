package com.project.financemanagement.controller;

import java.util.List;

import com.project.financemanagement.request.NotificationRequest;
import com.project.financemanagement.responseVo.Supervisor;
import com.project.financemanagement.service.SupervisorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//import com.supervisor.entity.NotificationRequest;
//import com.supervisor.entity.Supervisor;
//import com.supervisor.service.SupervisorService;

import jakarta.validation.Valid;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("api")
@RestController
@Validated
public class SupervisorController {

	private SupervisorService supervisorService;

	public SupervisorController(SupervisorService supervisorService) {
		this.supervisorService = supervisorService;
	}
	
	@GetMapping("supervisors")
	public ResponseEntity<List<Supervisor>> getAllSupervisors(){
		return new ResponseEntity<>(supervisorService.getAllSupervisors(),HttpStatus.OK);
	}
	
	@PostMapping("submit")
	public ResponseEntity<String> submitNotification(@Validated @RequestBody NotificationRequest notificationRequest, BindingResult result) {
		if (result.hasErrors()) {
			// Get the first error message from the validation errors
			String errorMessage = result.getAllErrors().get(0).getDefaultMessage();
			return ResponseEntity.badRequest().body("Validation error: " + errorMessage);
		}

		// If no errors, proceed with your logic
		System.out.println(notificationRequest.getRequest());
		return ResponseEntity.noContent().build();
	}
}
