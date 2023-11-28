package com.demo.Dto;

import com.demo.Entity.UserType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {

    private String name;
    
    @JsonIgnore
    private UserType userType= UserType.STUDENT;

    private String language;

    private String classGrade;
    
    private String email;
    
    private String password;
    
    
    @JsonIgnore
    private String role = "STUDENT";
    
//    private DoubtSubjectType doubtSubjectType;
}
