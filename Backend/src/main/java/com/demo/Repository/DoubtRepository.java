package com.demo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.demo.Entity.DoubtRequest;
import com.demo.Entity.DoubtStatus;

@Repository
public interface DoubtRepository extends JpaRepository<DoubtRequest, Long> {
	
	
//	@Query("SELECT d FROM Doubt d WHERE d.doubtStatus = :status")
//    List<DoubtRequest> findAllOpenDoubts(DoubtStatus status);
	List<DoubtRequest> findAllByDoubtStatus(DoubtStatus status);
	
}