package org.example.musklytrainwithmekmp

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

data class Tip(
    val category: String,
    val short: String,
    val details: String
)

class TipsViewModel {

    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    private val _expandedIndex = MutableStateFlow<String?>(null)
    val expandedIndex: StateFlow<String?> = _expandedIndex

    private val _categoriesWithTips = MutableStateFlow<List<Pair<String, List<Tip>>>>(emptyList())
    val categoriesWithTips: StateFlow<List<Pair<String, List<Tip>>>> = _categoriesWithTips

    private val allTips = listOf(
        Tip("Exercise","Warm up 5–10 min","Before lifting heavy or doing intense cardio, always warm up..."),
        Tip("Exercise","Progressive overload","Gradually increase weight, reps, or intensity..."),
        Tip("Exercise","Don’t train same muscles daily","Give at least 48h to recover..."),
        Tip("Exercise","Compound lifts matter","Squats, DL, bench – base of your routine"),
        Tip("Exercise","Change routine every 6–8 weeks","Your body adapts, avoid plateaus"),
        Tip("Exercise","Don’t go to failure every set","Save failure for last set"),
        Tip("Exercise","Cardio matters","Heart health + recovery"),
        Tip("Exercise","Track more than weight","Strength, endurance, composition"),
        Tip("Exercise","Hydrate","Avoid fatigue and cramps"),
        Tip("Exercise","Consistency beats intensity","Better regular than sporadic"),

        Tip("Technique","Form over weight","Correct technique > heavy load"),
        Tip("Technique","Breathe correctly","Inhale down, exhale up"),
        Tip("Technique","Engage your core","Back safety + power"),
        Tip("Technique","Control movement","No momentum, tension full"),
        Tip("Technique","ROM full","Max activation"),
        Tip("Technique","No sharp pain","Know discomfort vs pain"),
        Tip("Technique","Adjust machines","Fit to body size"),
        Tip("Technique","Not only mirror","Feel movement"),
        Tip("Technique","Basics matter","Squat, DL, Press first"),
        Tip("Technique","Stretch smart","Dynamic warm-up, static cooldown"),

        Tip("Rest","Growth in rest","Recovery = gains"),
        Tip("Rest","Sleep 7–9h","Bad sleep kills progress"),
        Tip("Rest","Not before bed","Affects sleep quality"),
        Tip("Rest","Respect rest days","Part of program"),
        Tip("Rest","Stretch after training","Less stiffness + mobility"),
        Tip("Rest","Deload weekly","6–8 weeks reset"),
        Tip("Rest","Active recovery","Walk, swim, yoga"),
        Tip("Rest","Avoid caffeine late","Ruins sleep"),
        Tip("Rest","Nutrition = recovery","Protein + carbs rebuilds"),
        Tip("Rest","Listen to body","Skip when exhausted"),
        Tip("Exercise", "Warm up 5–10 min", "Before lifting heavy or doing intense cardio, always warm up 5–10 minutes to prepare your muscles and joints."),
    Tip("Exercise", "Progressive overload", "Gradually increase weight, reps, or intensity to keep making progress. Small improvements add up!"),
    Tip("Exercise", "Don’t train same muscles daily", "Give your muscles at least 48h to recover before hitting them again."),
    Tip("Exercise", "Compound lifts matter", "Squats, deadlifts, bench press, and pull-ups train multiple muscles at once. Use them as the base of your routine."),
    Tip("Exercise", "Change routine every 6–8 weeks", "Your body adapts quickly. Add variation to avoid plateaus."),
    Tip("Exercise", "Don’t go to failure every set", "Save failure training for your last set of an exercise."),
    Tip("Exercise", "Cardio is also important", "Even if your goal is muscle growth, cardio keeps your heart healthy and improves recovery."),
    Tip("Exercise", "Track more than weight", "Measure s  trength, endurance, and body composition, not just the scale."),
    Tip("Exercise","Stay hydrated", "Drink water during training to avoid cramps and fatigue."),
    Tip("Exercise","Consistency beats intensity", "Training regularly is more important than going all-out once in a while."),
    Tip("Technique", "Form over weight", "Better to lift less but correctly. Good form prevents injuries and maximizes gains."),
    Tip("Technique","Breathe correctly", "Inhale when lowering, exhale when lifting. Don’t hold your breath unless using bracing."),
    Tip("Technique","Engage your core", "A strong core stabilizes your spine and improves almost every lift."),
    Tip("Technique","Control the movement", "Avoid bouncing or using too much momentum. Time under tension builds strength."),
    Tip("Technique","Full range of motion", "When safe, use the complete ROM to maximize muscle activation."),
    Tip("Technique","Stop if sharp pain", "Discomfort is fine, sharp pain is not. Know the difference."),
    Tip("Technique","Adjust equipment", "Make sure machines are set to your body size to avoid bad mechanics."),
    Tip("Technique","Don’t overuse the mirror", "Check form if needed, but also learn to feel the movement."),
    Tip("Technique","Master basics first", "Squats, deadlifts, and presses are fundamental—learn them well."),
    Tip("Technique","Stretch smart", "Dynamic stretches before training, static stretches after training."),
    Tip("Rest", "Muscle grows while resting", "Recovery is where the real gains happen, not in the gym."),
    Tip("Rest", "Sleep 7–9h" ,"Lack of sleep kills recovery, performance, and motivation."),
    Tip("Rest",  "Don’t train right before bed" , "Hard training close to bedtime can interfere with quality sleep."),
    Tip("Rest",   "Respect rest days" , "They are part of the program, not laziness."),
    Tip("Rest",   "Stretch after training" , "Helps reduce stiffness and keeps mobility."),
    Tip("Rest",   "Use deload weeks" , "Every 6–8 weeks, reduce load/volume to recover long-term."),
    Tip("Rest",    "Active recovery works", "Walking, swimming, or yoga on rest days helps circulation and repair."),
    Tip("Rest",   "Avoid too much caffeine", "Stimulants late in the day ruin sleep quality."),
    Tip("Rest",    "Nutrition = recovery", "Eat enough protein and carbs to rebuild muscles."),
    Tip("Rest",    "Listen to your body", "If you’re exhausted, skipping one workout may be better than pushing through.")
    )

    init { refreshTips() }

    fun toggleExpanded(index: String) {
        _expandedIndex.update { if (it == index) null else index }
    }

    fun refreshTips() {
        val grouped = allTips.groupBy { it.category }
        _categoriesWithTips.value = grouped.map { entry ->
            entry.key to entry.value.shuffled().take(2)
        }
    }
}
