package com.basit.workout_library.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Workout (
    val item:BaseSimpleWorkout,
    val type: WorkoutType,
    val workoutLength:Int
) : Parcelable