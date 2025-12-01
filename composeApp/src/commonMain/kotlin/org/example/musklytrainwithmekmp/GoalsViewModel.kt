package com.example.muskly_trainwithme_kmp

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.toLocalDateTime


data class Goal(
    val id: Int,
    val description: String,
    val reward: Int,
    val completed: Boolean = false
)

class GoalsViewModel {

    // Lista inicial de retos
    private val initialGoals = listOf(
        Goal(1, "Do 50 squads", 10),
        Goal(2, "Do over 2 hours of training", 15),
        Goal(3, "Have 5 day streak", 25),
        Goal(4, "Train all the muscles in a week", 30),
        Goal(5, "Have a 14 day streak", 50),
        Goal(6, "Do 100 push-ups", 20),
        Goal(7, "Run 10 km", 40)
    )

    // Flow de la lista de retos
    private val _goals = MutableStateFlow(initialGoals)
    val goals: StateFlow<List<Goal>> = _goals

    // Flow de monedas ganadas
    private val _coins = MutableStateFlow(0)
    val coins: StateFlow<Int> = _coins

    init {
        resetGoalsIfMonday()
    }

    fun completeGoal(goal: Goal): Int {
        val index = _goals.value.indexOfFirst { it.id == goal.id }
        if (index != -1 && !_goals.value[index].completed) {

            val updated = _goals.value.toMutableList()
            updated[index] = updated[index].copy(completed = true)
            _goals.value = updated

            _coins.value += goal.reward

            return goal.reward
        }
        return 0
    }

    private fun resetGoalsIfMonday() {
        val now = Clock.System.now()
        val date = now.toLocalDateTime(TimeZone.currentSystemDefault()).date
        val day = date.dayOfWeek

        if (day.isoDayNumber == 1) { // Lunes
            _goals.value = _goals.value.map { it.copy(completed = false) }
        }
    }

    fun resetAllGoals() {
        _goals.value = _goals.value.map { it.copy(completed = false)}
        }
}