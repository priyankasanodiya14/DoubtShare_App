package com.demo.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties.Admin;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.demo.Dto.StudentDTO;
import com.demo.Dto.TutorDTO;
import com.demo.Entity.DoubtRequest;
import com.demo.Entity.DoubtStatus;
import com.demo.Entity.DoubtSubjectType;
import com.demo.Entity.MyUser;
import com.demo.Entity.TutorAvailability;
import com.demo.Entity.TutorAvailabilityStatus;
import com.demo.Entity.TutorExpertise;
import com.demo.Entity.UserType;
import com.demo.Exceptions.NotFoundException;
import com.demo.Exceptions.SomethingWentWrongExceptions;
import com.demo.Repository.DoubtRepository;
import com.demo.Repository.MyUserRepository;
import com.demo.Repository.TutorAvailabilityRepository;

@Service
public class MyUserServiceImpl implements MyUserService {

	@Autowired
	private DoubtRepository doubtRepository;

	@Autowired
	private MyUserRepository myUserRepository;

	@Autowired
	private TutorAvailabilityRepository tutorAvailabilityRepository;

	@Override
	public MyUser registerStudent(StudentDTO studentDTO) {
		try {
			MyUser student = new MyUser();
			student.setName(studentDTO.getName());
			student.setUserType(UserType.STUDENT);
			student.setLanguage(studentDTO.getLanguage());
			student.setClassGrade(studentDTO.getClassGrade());
			student.setEmail(studentDTO.getEmail());
			student.setPassword(studentDTO.getPassword());
			student.setRole(studentDTO.getRole());

			return myUserRepository.save(student);
		} catch (Exception e) {
			e.getStackTrace();
			throw new SomethingWentWrongExceptions("Error occurred during student registration.");
		}
	}

	@Override
	public MyUser registerTutor(TutorDTO tutorDTO) {
		try {

			MyUser tutor = new MyUser();
			tutor.setName(tutorDTO.getName());
			tutor.setUserType(UserType.TUTOR);
			tutor.setLanguage(tutorDTO.getLanguage());
			tutor.setClassGrade(tutorDTO.getClassGrade());
			tutor.setTutorExpertise(tutorDTO.getTutorExpertise());
			tutor.setOnline(tutorDTO.isOnline());
			tutor.setEmail(tutorDTO.getEmail());
			tutor.setPassword(tutorDTO.getPassword());
			tutor.setRole(tutorDTO.getRole());

			MyUser savedTutor = myUserRepository.save(tutor);

			TutorAvailability tutorAvailability = new TutorAvailability();
			tutorAvailability.setMyUser(savedTutor);
			tutorAvailability.setOnline(tutorDTO.isOnline());
			tutorAvailability.setAvailabilityStatus(
					tutorDTO.isOnline() ? TutorAvailabilityStatus.ONLINE : TutorAvailabilityStatus.OFFLINE);

			tutorAvailabilityRepository.save(tutorAvailability);

			return savedTutor;
		} catch (Exception e) {
			throw new SomethingWentWrongExceptions("Error occurred during tutor registration.");
		}
	}

	@Override
	public List<MyUser> findTutorsByExpertise(TutorExpertise expertise) {
		try {
			return myUserRepository.findTutorsByExpertise(expertise);
		} catch (Exception e) {
			throw new SomethingWentWrongExceptions("Error occurred while finding tutors by expertise.");
		}
	}

	@Override
	public DoubtRequest createDoubt(int studentId, int tutorId, DoubtSubjectType doubtSubjectType)
			throws SomethingWentWrongExceptions, NotFoundException {
		try {
			MyUser student = myUserRepository.findById(studentId)
					.orElseThrow(() -> new NotFoundException("Student not found with ID: " + studentId));

			MyUser tutor = myUserRepository.findById(tutorId)
					.orElseThrow(() -> new NotFoundException("Tutor not found with ID: " + tutorId));

			DoubtRequest doubt = new DoubtRequest();
			doubt.setStudent(student);
			doubt.setTutor(tutor);
			doubt.setDoubtStatus(DoubtStatus.OPEN);
			doubt.setCreatedAt(LocalDateTime.now());
			doubt.setDoubtSubject(doubtSubjectType);

			DoubtRequest savedDoubt = doubtRepository.save(doubt);
			updateTutorStatus(tutor, false);
			return savedDoubt;
		} catch (Exception e) {
			throw new SomethingWentWrongExceptions("Error occurred during doubt creation.");
		}
	}

	@Override
	public void tutorSolvesDoubt(long doubtId, int tutorId) throws SomethingWentWrongExceptions, NotFoundException {
		try {
			DoubtRequest doubt = doubtRepository.findById(doubtId)
					.orElseThrow(() -> new NotFoundException("Doubt not found with ID: " + doubtId));

			MyUser tutor = myUserRepository.findById(tutorId)
					.orElseThrow(() -> new NotFoundException("Tutor not found with ID: " + tutorId));

			doubt.setDoubtStatus(DoubtStatus.CLOSED);

			updateTutorStatus(tutor, true);
			doubtRepository.save(doubt);
			myUserRepository.save(tutor);
		} catch (Exception e) {
			throw new SomethingWentWrongExceptions("Error occurred while processing tutor's doubt resolution.");
		}
	}

	private void updateTutorStatus(MyUser tutor, boolean isOnline) {
		try {

			TutorAvailability tutorAvailability = tutorAvailabilityRepository.findByMyUser(tutor).orElseThrow(
					() -> new NotFoundException("TutorAvailability not found for tutor: " + tutor.getMyUserId()));

			tutorAvailability.setOnline(isOnline);
			tutorAvailability
					.setAvailabilityStatus(isOnline ? TutorAvailabilityStatus.ONLINE : TutorAvailabilityStatus.OFFLINE);

			tutorAvailabilityRepository.save(tutorAvailability);
		} catch (Exception e) {
			throw new SomethingWentWrongExceptions("Error occurred while updating tutor's availability status.");
		}
	}

	@Override
	@Scheduled(fixedRate = 3000)
	public List<TutorAvailability> findAllAvailableTutors() {
		return tutorAvailabilityRepository.findByIsOnlineTrue();
	}

	@Override
	public Optional<MyUser> findByEmail(String Email) {
		Optional<MyUser> user = myUserRepository.findByEmail(Email);
		if (user.isEmpty())
			throw new NotFoundException("No admin found");
		return user;
	}

}
