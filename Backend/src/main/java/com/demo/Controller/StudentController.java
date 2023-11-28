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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demo.Dto.StudentDTO;
import com.demo.Entity.DoubtRequest;
import com.demo.Entity.DoubtSubjectType;
import com.demo.Entity.MyUser;
import com.demo.Entity.TutorAvailability;
import com.demo.Entity.TutorExpertise;
import com.demo.Exceptions.NotFoundException;
import com.demo.Exceptions.SomethingWentWrongExceptions;
import com.demo.Service.DoubtService;
import com.demo.Service.MyUserService;

@RestController
@RequestMapping("/students")
public class StudentController {
  

    @Autowired
    private MyUserService myUserService;

    @Autowired
    private DoubtService doubtService;
    
    @Autowired
	private PasswordEncoder passwordEncoder;

    
    @PostMapping("/signin")
	public ResponseEntity<String> logInUserHandler(Authentication auth) throws NotFoundException {
		MyUser student = myUserService.findByEmail(auth.getName()).get();
		return new ResponseEntity<>(student.getEmail() + " Logged In Successfully", HttpStatus.ACCEPTED);
	}
    
    @PostMapping("/registerStudent")
    public ResponseEntity<MyUser> registerStudent(@RequestBody StudentDTO studentDTO) {
    	studentDTO.setPassword(passwordEncoder.encode(studentDTO.getPassword()));
        MyUser tutor = myUserService.registerStudent(studentDTO);
        return new ResponseEntity<>(tutor, HttpStatus.CREATED);
    }
	
    @GetMapping("/find-tutor-by-expertise/{expertise}")
    public ResponseEntity<?> findTutorsByExpertise(@PathVariable TutorExpertise expertise) {
        try {
            List<MyUser> tutors = myUserService.findTutorsByExpertise(expertise);
            return new ResponseEntity<List<MyUser>>(tutors, HttpStatus.ACCEPTED);
        } catch (Exception e) {
        	return new ResponseEntity<String>("Not found", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping("/createDoubt")
    public ResponseEntity<DoubtRequest> createDoubt(
           Authentication auth,
            @RequestParam("tutorId") int tutorId,
            @RequestParam("doubtSubjectType") DoubtSubjectType doubtSubjectType
    ) {
        try {
        	MyUser student = myUserService.findByEmail(auth.getName()).get();
        	
            DoubtRequest createdDoubt = myUserService.createDoubt(student.getMyUserId(), tutorId, doubtSubjectType);
            return new ResponseEntity<>(createdDoubt, HttpStatus.CREATED);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (SomethingWentWrongExceptions e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/find-available-tutor")
    public ResponseEntity<List<TutorAvailability>> getAllAvailableTutors() {
        List<TutorAvailability> availableTutors = myUserService.findAllAvailableTutors();
        return new ResponseEntity<>(availableTutors, HttpStatus.OK);
    }
    
   
    }

