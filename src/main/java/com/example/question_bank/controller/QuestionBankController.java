package com.example.question_bank.controller;

import com.example.question_bank.dto.QuestionProgress;
import com.example.question_bank.model.Question;
import com.example.question_bank.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class QuestionBankController {

    private final QuestionService questionService;

    // 메인 페이지에서 문제 풀기를 시작할 때 새로운 세트 시작
    @GetMapping("/")
    public String index() {
        questionService.startNewQuestionSet();
        return "index";
    }

    // 문제 풀기 버튼 클릭 시
    @GetMapping("/question/select-count")
    public String selectCount(Model model) {
        long totalQuestions = questionService.getTotalQuestionCount();
        model.addAttribute("totalQuestions", totalQuestions);
        return "select-count";
    }

    // 문제 수 선택 후 시작
    @PostMapping("/question/start")
    public String startQuestions(@RequestParam("questionCount") int questionCount) {
        questionService.startQuestionSet(questionCount);
        return "redirect:/question/solve";
    }

    // 문제 등록 페이지
    @GetMapping("/question/register")
    public String registerForm() {
        return "register";
    }

    // 문제 등록 처리
    @PostMapping("/question/register")
    public String register(Question question) {
        questionService.saveQuestion(question);
        return "redirect:/";
    }

    // 문제 풀기 페이지
    @GetMapping("/question/solve")
    public String solve(Model model) {
        Question randomQuestion = questionService.getRandomQuestion();

        if (randomQuestion == null) {
            return "complete";
        }

        QuestionProgress progress = questionService.getProgress();
        model.addAttribute("question", randomQuestion);
        model.addAttribute("progress", progress);
        return "solve";
    }
}