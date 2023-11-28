package com.demo.Utility;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties.Admin;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.demo.Entity.MyUser;
import com.demo.Entity.UserType;
import com.demo.Repository.MyUserRepository;


@Service
public class ManualUserDetailsService implements UserDetailsService {
    
	@Autowired
	private MyUserRepository myUserRepository;
	
	public boolean isStudent(String email) {
	   Optional<MyUser> student = myUserRepository.findByEmail(email);
		if(student.isPresent() && student.get().getUserType().equals(UserType.STUDENT)) return true;
		else return false;
	}
	
	
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		if(isStudent(email)) {
			Optional<MyUser> studeOptional = myUserRepository.findByEmail(email);
			 
			 if(studeOptional.isEmpty()) throw new UsernameNotFoundException("Student not found");
			 MyUser us = studeOptional.get();
			
			 
			List<GrantedAuthority> authorities = new ArrayList<>() ;
			SimpleGrantedAuthority autho = new SimpleGrantedAuthority("ROLE_"+us.getRole().toUpperCase()) ;
			authorities.add(autho) ;
			User secUser = new User(us.getEmail(), us.getPassword(),  authorities) ;
			return secUser ;
		}else {
			
			Optional<MyUser> myUser = myUserRepository.findByEmail(email);
				 
				 if(myUser.isEmpty()) throw new UsernameNotFoundException("User not found");
				 MyUser us = myUser.get();
				 
				 
				List<GrantedAuthority> authorities = new ArrayList<>() ;
				SimpleGrantedAuthority autho = new SimpleGrantedAuthority("ROLE_"+us.getRole().toUpperCase()) ;
				authorities.add(autho) ;
				User secUser = new User(us.getEmail(), us.getPassword(),  authorities) ;
				return secUser ;

				
			}
		}


}
