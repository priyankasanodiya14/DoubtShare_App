package com.demo.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.Entity.DoubtRequest;
import com.demo.Entity.DoubtStatus;
import com.demo.Entity.DoubtSubjectType;
import com.demo.Exceptions.NotFoundException;
import com.demo.Repository.DoubtRepository;

@Service
public class DoubtServiceImpl implements DoubtService {

    @Autowired
    private DoubtRepository doubtRepository;

    @Override
    public List<DoubtRequest> getAllOpenDoubts() throws NotFoundException {
        return doubtRepository.findAllByDoubtStatus(DoubtStatus.OPEN);
    }

}
