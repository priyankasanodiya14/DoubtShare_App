package com.demo.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.Dto.TutorDTO;
import com.demo.Entity.DoubtRequest;
import com.demo.Entity.MyUser;
import com.demo.Exceptions.NotFoundException;
import com.demo.Exceptions.SomethingWentWrongExceptions;
import com.demo.Service.DoubtService;
import com.demo.Service.MyUserService;

@RestController
@RequestMapping("/tutors")
public class TutorController {
	
	@Autowired
	private MyUserService myUserService;
	
   @Autowired
   private DoubtService doubtService;
   
   @Autowired
  	private PasswordEncoder passwordEncoder;

   
   @PostMapping("/signin")
 	public ResponseEntity<String> logInUserHandler(Authentication auth) throws NotFoundException {
 		MyUser tutor = myUserService.findByEmail(auth.getName()).get();
 		return new ResponseEntity<>(tutor.getEmail() + " Logged In Successfully", HttpStatus.ACCEPTED);
 	}

	   @PostMapping("/registerTutor")
	    public ResponseEntity<MyUser> registerTutor(@RequestBody TutorDTO tutotDTO) {
		   tutotDTO.setPassword(passwordEncoder.encode(tutotDTO.getPassword()));
	        MyUser tutor = myUserService.registerTutor(tutotDTO);
	        return new ResponseEntity<>(tutor, HttpStatus.CREATED);
	    }
	
	   @PostMapping("/tutor-solve-doubt/{doubtId}/")
	    public ResponseEntity<String> solveDoubt(@PathVariable long doubtId, Authentication auth) {
	        try {
	        	MyUser tutor = myUserService.findByEmail(auth.getName()).get();
	        	
	            myUserService.tutorSolvesDoubt(doubtId, tutor.getMyUserId());
	            return new ResponseEntity<>("Doubt resolved successfully", HttpStatus.OK);
	        } catch (SomethingWentWrongExceptions | NotFoundException e) {
	            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }
	   @GetMapping("/open-doubts")
	    public ResponseEntity<List<DoubtRequest>> getAllOpenDoubts() {
	        try {
	            List<DoubtRequest> openDoubts = doubtService.getAllOpenDoubts();
	            return new ResponseEntity<>(openDoubts, HttpStatus.OK);
	        } catch (NotFoundException e) {
	            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	        }
}
}
