package com.example.question_bank.repository;

import com.example.question_bank.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    // 랜덤한 문제 하나를 가져오는 메서드
    @Query(value = "SELECT * FROM question ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Question findRandomQuestion();
}