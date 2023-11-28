package com.demo.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.Entity.MyUser;
import com.demo.Entity.TutorAvailability;

public interface TutorAvailabilityRepository extends JpaRepository<TutorAvailability, Long> {
	 Optional<TutorAvailability> findByMyUser(MyUser myUser);
	 List<TutorAvailability> findByIsOnlineTrue();
}
