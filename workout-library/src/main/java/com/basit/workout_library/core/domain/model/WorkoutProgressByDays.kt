package com.basit.workout_library.core.domain.model

internal data class WorkoutProgressByDays(
    val programId:Int,
    val dayNumber: Int,
    val workoutId:Int,
    val totalSecondsActive:Int
)
