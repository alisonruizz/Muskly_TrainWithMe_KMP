package org.example.musklytrainwithmekmp


import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.asStateFlow

import kotlinx.serialization.Serializable

@Serializable
data class PetRoutine(
    val petName: String = "",
    val routines: Map<String, List<Exercise>> = emptyMap(),
    val shop: ShopData = ShopData()
)
class TrainViewModel : ViewModel(){

    private val _routines = MutableStateFlow<Map<String, List<Exercise>>>(emptyMap())
    val routines =  _routines.asStateFlow()
    var petName = MutableStateFlow("")
    private val _isPetNameLocked = MutableStateFlow(false)
    val isPetNameLocked = _isPetNameLocked.asStateFlow()



    // Firebase instance
    private val firestore = Firebase.firestore

    fun loadRoutineFromFirebase(userId: String, onFinish:()->Unit = {}) {

        firestore.collection("users")
            .document(userId)
            .collection("mascotas")
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val doc = documents.documents.first()   // toma la primera mascota encontrada

                    val name = doc.getString("petName") ?: ""
                    petName.value = name

                    // 🔒 Si hay nombre → bloquear edición
                    if (name.isNotEmpty()) {
                        _isPetNameLocked.value = true
                    }




                    val map = doc.get("routines") as? Map<String, List<Map<String, Any>>>
                    val routinesConverted = map?.mapValues { entry ->
                        entry.value.map { ex ->
                            Exercise(
                                name = ex["name"] as String,
                                series = (ex["series"] as Long).toInt(),
                                reps = (ex["reps"] as Long).toInt(),
                                weight = (ex["weight"] as Long).toInt()
                            )
                        }
                    } ?: emptyMap()

                    _routines.value = routinesConverted
                }
                onFinish()
            }
            .addOnFailureListener { onFinish() }
    }


    fun addExercise(day: String, exercise: Exercise) {
        _routines.update { current ->
            val list = current[day]?.toMutableList() ?: mutableListOf()
            list.add(exercise)
            current + (day to list)
        }
    }

    fun removeExercise(day: String, index: Int) {
        _routines.update { current ->
            val list = current[day]?.toMutableList() ?: return@update current
            if (index in list.indices) {
                list.removeAt(index)
                current + (day to list)
            } else current
        }
    }

    fun updateRoutineInFirebase(userId: String, onResult: (Boolean) -> Unit) {
        // Solo actualizar si ya existe la mascota (nombre bloqueado)
        if (!_isPetNameLocked.value) {
            onResult(false)
            return
        }

        val data = mapOf(
            "petName" to petName.value,
            "routines" to routines.value,
        )

        firestore.collection("users")
            .document(userId)
            .collection("mascotas")
            .document(petName.value)  // Usa el mismo ID del documento existente
            .set(data, SetOptions.merge())
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }

}
