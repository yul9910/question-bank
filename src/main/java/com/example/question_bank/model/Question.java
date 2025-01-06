package com.example.question_bank.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;  // 문제 내용

    private String answerA;
    private String answerB;
    private String answerC;
    private String answerD;
    private String answerE;
}