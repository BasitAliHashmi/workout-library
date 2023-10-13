package com.basit.workout_library.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
data class WorkoutHistory(
    val id: Int,
    val programId: Int,
    val day: Int,
    val workoutId: Int,
    val durationSeconds: Int,
    val createdOn: LocalDateTime
): Parcelable