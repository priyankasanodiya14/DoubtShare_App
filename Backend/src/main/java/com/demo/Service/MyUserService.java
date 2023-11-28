package com.demo.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.query.Param;

import com.demo.Dto.StudentDTO;
import com.demo.Dto.TutorDTO;
import com.demo.Entity.DoubtRequest;
import com.demo.Entity.DoubtSubjectType;
import com.demo.Entity.MyUser;
import com.demo.Entity.TutorAvailability;
import com.demo.Entity.TutorExpertise;
import com.demo.Exceptions.NotFoundException;
import com.demo.Exceptions.SomethingWentWrongExceptions;

public interface MyUserService {

	MyUser registerStudent(StudentDTO studentDTO) throws SomethingWentWrongExceptions;
	MyUser registerTutor(TutorDTO tutorDTO)  throws SomethingWentWrongExceptions;
	
	DoubtRequest createDoubt(int studentId, int tutorId, DoubtSubjectType doubtSubjectType) throws SomethingWentWrongExceptions, NotFoundException;
	
	 List<MyUser> findTutorsByExpertise(@Param("expertise") TutorExpertise expertise);
	 
	 void tutorSolvesDoubt(long doubtId, int tutorId) throws SomethingWentWrongExceptions, NotFoundException;
	 List<TutorAvailability> findAllAvailableTutors();
	 Optional<MyUser> findByEmail(String Email);
	
}
