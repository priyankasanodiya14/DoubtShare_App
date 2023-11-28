package com.demo.Service;

import java.util.List;

import com.demo.Entity.DoubtRequest;
import com.demo.Entity.DoubtSubjectType;
import com.demo.Exceptions.NotFoundException;

public interface DoubtService {
   
	List<DoubtRequest> getAllOpenDoubts() throws NotFoundException;
}
