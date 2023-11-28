package com.demo.Dto;

import com.demo.Entity.TutorExpertise;
import com.demo.Entity.UserType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TutorDTO {

    private String name;
    
    @JsonIgnore
    private UserType userType = UserType.TUTOR;

    private String language;

    private String classGrade;
    
  private String email;
    
    private String password;  
    
    
    @JsonIgnore
    private String role = "TUTOR";

    private TutorExpertise tutorExpertise;

    private boolean online;
}
