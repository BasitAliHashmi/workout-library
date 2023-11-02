package com.basit.workout_library.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FitnessProgramDay(
    val restDay:Boolean = false,
    val workouts:List<Workout> = arrayListOf()
) : Parcelable {
    //var totalSeconds:Int = 0

    fun totalSeconds():Int{
        var totalSeconds = 0
        this.workouts.filter { it.type == WorkoutType.Timed }.forEach { item ->
            totalSeconds += item.workoutLength
        }
        return totalSeconds
    }

    companion object {
        fun restDay(): FitnessProgramDay {
            return FitnessProgramDay( true, arrayListOf())
        }
    }
}