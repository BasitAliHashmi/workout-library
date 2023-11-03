package com.basit.workout_library.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
internal data class WorkoutHistory(
    val id: Int,
    val programId: Int,
    val day: Int,
    val workoutId: Int,
    val durationSeconds: Int,
    val createdOn: LocalDateTime
): Parcelable