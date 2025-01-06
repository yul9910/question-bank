package com.example.question_bank.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QuestionProgress {
    private int current;
    private int total;
}