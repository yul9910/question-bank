package com.example.question_bank.service;

import com.example.question_bank.dto.QuestionProgress;
import com.example.question_bank.model.Question;
import com.example.question_bank.repository.QuestionRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private static final String SOLVED_QUESTIONS_KEY = "solvedQuestionIds";
    private static final String TARGET_COUNT_KEY = "targetQuestionCount";

    private final QuestionRepository questionRepository;
    private final HttpSession session;

    // 전체 문제 수 조회
    public long getTotalQuestionCount() {
        return questionRepository.count();
    }

    // 문제 풀기 세션 시작
    public void startQuestionSet(int questionCount) {
        long totalQuestions = getTotalQuestionCount();

        if (questionCount <= 0 || questionCount > totalQuestions) {
            throw new IllegalArgumentException("유효하지 않은 문제 수입니다.");
        }

        session.setAttribute(SOLVED_QUESTIONS_KEY, new HashSet<Long>());
        session.setAttribute(TARGET_COUNT_KEY, questionCount);
    }

    // 문제 저장
    @Transactional
    public void saveQuestion(Question question) {
        questionRepository.save(question);
    }

    // 풀지 않은 랜덤 문제 가져오기
    @Transactional(readOnly = true)
    public Question getRandomQuestion() {
        Set<Long> solvedQuestionIds = getSolvedQuestionIds();
        Integer targetCount = (Integer) session.getAttribute(TARGET_COUNT_KEY);

        // 목표 문제 수를 다 풀었으면 null 반환
        if (targetCount == null || solvedQuestionIds.size() >= targetCount) {
            return null;
        }

        // 최대 시도 횟수 설정
        int maxAttempts = 100;
        int attempts = 0;
        Question question;

        do {
            question = questionRepository.findRandomQuestion();
            attempts++;
            // 무한 루프 방지
            if (attempts >= maxAttempts) {
                return null;
            }
        } while (question != null && solvedQuestionIds.contains(question.getId()));

        if (question != null) {
            solvedQuestionIds.add(question.getId());
            session.setAttribute(SOLVED_QUESTIONS_KEY, solvedQuestionIds);
        }

        return question;
    }

    // 현재 진행 상황 가져오기
    public QuestionProgress getProgress() {
        Set<Long> solvedQuestionIds = getSolvedQuestionIds();
        Integer targetCount = (Integer) session.getAttribute(TARGET_COUNT_KEY);

        if (targetCount == null) {
            targetCount = 0;
        }

        return new QuestionProgress(solvedQuestionIds.size(), targetCount);
    }

    // 새로운 문제 세트 시작
    public void startNewQuestionSet() {
        session.setAttribute(SOLVED_QUESTIONS_KEY, new HashSet<Long>());
    }

    // 풀었던 문제 ID 목록 가져오기
    @SuppressWarnings("unchecked")
    private Set<Long> getSolvedQuestionIds() {
        Set<Long> solvedQuestionIds = (Set<Long>) session.getAttribute(SOLVED_QUESTIONS_KEY);
        if (solvedQuestionIds == null) {
            solvedQuestionIds = new HashSet<>();
            session.setAttribute(SOLVED_QUESTIONS_KEY, solvedQuestionIds);
        }
        return solvedQuestionIds;
    }
}