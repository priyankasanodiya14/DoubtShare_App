package com.demo.Entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DoubtRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long doubtId;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private MyUser student;

    @ManyToOne
    @JoinColumn(name = "tutor_id")
    private MyUser tutor;

    @Enumerated(EnumType.STRING)
    private DoubtSubjectType doubtSubject;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private DoubtStatus doubtStatus;
}
