package com.demo.Entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class MyUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int myUserId;

    @Column(nullable = false)
    private String name;
     
    @NotEmpty(message = "Email should not be Empty!!")
    @Email(message = "Email should be formatted!!")
    private String email;


    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    
    
    @Enumerated(EnumType.STRING)
    private UserType userType;

    @Column(nullable = false)
    private String language;

    @Column(nullable = false)
    private String classGrade;
    
    @JsonIgnore
    private String role;
    

    @JsonIgnore
    @Enumerated(EnumType.STRING)
    private TutorExpertise tutorExpertise;
    
    @JsonIgnore
    @Column(nullable = false)
    private boolean isOnline;

    @JsonIgnore
    @OneToMany(mappedBy = "tutor", cascade = CascadeType.ALL)
    private List<DoubtRequest> doubts;

}
