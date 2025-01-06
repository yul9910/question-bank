// 정답 보기 기능
function showAnswer(element) {
    const answerSpan = element.querySelector('span:not(.d-none)');
    const hiddenAnswer = element.querySelector('.d-none');
    if (answerSpan.textContent === '정답 보기') {
        answerSpan.textContent = hiddenAnswer.textContent;
        element.style.backgroundColor = '#e9ecef';
    }
}