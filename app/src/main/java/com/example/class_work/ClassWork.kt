package com.example.figma

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.material3.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuizApp()
        }
    }
}

enum class Level { EASY, MEDIUM, HARD }
@Composable
fun QuizApp() {
    // Создаем список вопросов с указанием уровня сложности
    val easyQuestions = listOf(
        Question("Какая страна самая крупная?", listOf("Китай", "Россия", "США"), "Россия", Level.EASY),
        Question("Какой сейчас год?", listOf("2025", "2000", "2008"), "2025", Level.EASY)
    )

    val mediumQuestions = listOf(
        Question("Какая медаль за первое место?", listOf("Бронзовая", "Серебряная", "Золотая"), "Золотая", Level.MEDIUM),
        Question("Столица Франции?", listOf("Париж", "Лондон", "Берлин"), "Париж", Level.MEDIUM)
    )

    val hardQuestions = listOf(
        Question("Самый большой океан на Земле?", listOf("Тихий", "Атлантический", "Индийский"), "Тихий", Level.HARD),
        Question("Самая высокая гора в мире?", listOf("Эверест", "Килиманджаро", "Монблан"), "Эверест", Level.HARD)
    )


    data class Question(val text: String, val options: List<String>, val correctAnswer: String, val level: Level)

    var selectedLevel by remember { mutableStateOf(Level.EASY) }
    var currentQuestion by remember { mutableStateOf(0) }
    var selectedAnswer by remember { mutableStateOf("") }
    var score by remember { mutableStateOf(0) }
    var quizFinished by remember { mutableStateOf(false) }
    var enabledButton by remember { mutableStateOf(false) }

    val questions = when(selectedLevel) {
        Level.EASY -> easyQuestions
        Level.MEDIUM -> mediumQuestions
        Level.HARD -> hardQuestions
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        if (!quizFinished) {
            Column(modifier = Modifier.weight(1f).fillMaxWidth()) {
                Text(text = "Выберите уровень сложности:")
                Spacer(modifier = Modifier.height(12.dp))

                Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                    Button(onClick = { selectedLevel = Level.EASY }) {
                        Text(text = "Легко")
                    }
                    Button(onClick = { selectedLevel = Level.MEDIUM }) {
                        Text(text = "Нормально")
                    }
                    Button(onClick = { selectedLevel = Level.HARD }) {
                        Text(text = "Сложно")
                    }
                }
            }

            if (selectedLevel != null && !questions.isEmpty()) {
                Column(modifier = Modifier.weight(4f).fillMaxWidth()) {
                    Text(text = "Вопрос: ${questions[currentQuestion].text}")
                    Spacer(modifier = Modifier.height(12.dp))

                    questions[currentQuestion].options.forEach { option ->
                        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = selectedAnswer == option,
                                onClick = {
                                    selectedAnswer = option
                                    questions[currentQuestion].options.forEach { opt ->
                                        if (selectedAnswer == opt) {
                                            enabledButton = true
                                        }
                                    }
                                }
                            )
                            Text(text = option)
                        }
                    }
                }
            }
        }

        if (enabledButton) {
            Button(onClick = {
                if (selectedAnswer == questions[currentQuestion].correctAnswer) {
                    score++
                }
                if (currentQuestion < questions.size - 1) {
                    currentQuestion++
                } else {
                    quizFinished = true
                }
                selectedAnswer = ""
                enabledButton = false
            }, enabled = enabledButton) {
                Text(text = if (currentQuestion < questions.size - 1) "Следующий вопрос" else "Закончить")
            }
        }

        if (quizFinished) {
            Column(modifier = Modifier.weight(1f).fillMaxWidth()) {
                Text(text = "Ваш результат: $score/${questions.size}")
                Button(onClick = {
                    quizFinished = false
                    currentQuestion = 0
                    score = 0
                    selectedAnswer = ""
                }) {
                    Text(text = "Перезапустить квиз")
                }
            }
        }
    }
}
data class Question(val text: String, val options: List<String>, val correctAnswer: String, val level:Level)