package com.basit.workout_library.core.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "workout_history")
internal data class WorkoutHistoryEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "rowid") val id: Int,
    @ColumnInfo(name = "program_id") val programId: Int,
    @ColumnInfo(name = "day") val day: Int,
    @ColumnInfo(name = "workout_id") val workoutId: Int,
    @ColumnInfo(name = "duration_seconds") val durationSeconds: Int,
    @ColumnInfo(name = "created_on") val createdOn: LocalDateTime
)
