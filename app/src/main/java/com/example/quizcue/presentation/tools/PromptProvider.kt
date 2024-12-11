package com.example.quizcue.presentation.tools

class PromptProvider {
    fun generateAnswerPrompt(questionText: String): String {
        return "Напиши ответ на вопрос: $questionText"
    }

    fun generateHintPrompt(answer: String): String {
        return "Напиши подсказку для запоминания ответа: $answer не более 20 слов"
    }

    fun generationFailed(): String {
        return "Не удалось создать"
    }

    fun generationError(): String {
        return "Ошибка генерации"
    }

    fun correctAnswerPrompt(questionText: String): String {
        return "Напиши один правильный ответ в официальном стиле на вопрос: \"$questionText\", используя не более 20 слов."
    }

    fun incorrectAnswerPrompt(questionText: String): String {
        return "Напиши один неправильный ответ в официальном стиле на вопрос: \"$questionText\", используя не более 20 слов."
    }
}
