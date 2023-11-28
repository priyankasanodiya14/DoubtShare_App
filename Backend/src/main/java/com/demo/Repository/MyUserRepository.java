package com.demo.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.demo.Entity.DoubtSubjectType;
import com.demo.Entity.MyUser;
import com.demo.Entity.TutorExpertise;
import com.demo.Entity.UserType;

@Repository
public interface MyUserRepository extends JpaRepository<MyUser, Integer> {

	 @Query("SELECT tutor FROM MyUser tutor " +
	           "WHERE tutor.userType = 'TUTOR' " +
	           "AND tutor.isOnline = true " +
	           "AND tutor.classGrade = :classGrade " +
	           "AND tutor.language = :language " +
	           "AND EXISTS (SELECT 1 FROM tutor.doubts d WHERE d.doubtSubject = :subjectType)")
	    List<MyUser> findAvailableTutorsBySubjectAndCriteria(
	            @Param("subjectType") DoubtSubjectType subjectType,
	            @Param("classGrade") String classGrade,
	            @Param("language") String language
	    );

	 @Query("SELECT tu FROM MyUser tu WHERE tu.userType = 'TUTOR' AND tu.tutorExpertise = :expertise")
	    List<MyUser> findTutorsByExpertise(@Param("expertise") TutorExpertise expertise);

	    List<MyUser> findByIsOnlineAndUserType(boolean isOnline, UserType userType);
       
	    Optional<MyUser> findByEmail(String Email);
	    
	    
	    
}