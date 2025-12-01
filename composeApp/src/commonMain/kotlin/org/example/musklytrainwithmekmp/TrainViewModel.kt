package org.example.musklytrainwithmekmp


import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.asStateFlow

class TrainViewModel {

    private val _routines = MutableStateFlow<Map<String, List<Exercise>>>(emptyMap())
    val routines: StateFlow<Map<String, List<Exercise>>> = _routines.asStateFlow()

    fun addExercise(day: String, exercise: Exercise) {
        _routines.update { current ->
            val list = current[day]?.toMutableList() ?: mutableListOf()
            list.add(exercise)
            current + (day to list)
        }
    }

    fun removeExercise(day: String, index: Int) {
        _routines.update { current ->
            val list = current[day]?.toMutableList() ?: return
            if (index in list.indices) {
                list.removeAt(index)
                current + (day to list)
            } else current
        }
    }
}
