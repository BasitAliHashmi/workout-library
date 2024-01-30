package com.basit.workout_library.core.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.basit.workout_library.core.data.entity.WorkoutHistoryEntity
import com.basit.workout_library.core.domain.model.WorkoutProgressByDays
import com.basit.workout_library.core.domain.model.WorkoutSummary
import java.time.LocalDateTime

@Dao
internal interface WorkoutHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(workoutHistory: WorkoutHistoryEntity)

    @Query("select Date(wh.created_on) as WorkoutDate, sum(wh.duration_seconds) as TotalSeconds from workout_history wh where Date(wh.created_on) >= Date(:from) and Date(wh.created_on) <= Date(:to) and (0 = :programId or wh.program_id = :programId) Group By Date(wh.created_on);")
    suspend fun getSummarizedReport(from: LocalDateTime, to: LocalDateTime, programId:Int): List<WorkoutSummary>

    @Query("select Date(wh.created_on) as WorkoutDate, sum(wh.duration_seconds) as TotalSeconds from workout_history wh where Date(wh.created_on) = Date(:dateFor) and wh.day = :dayNo and (0 = :programId or wh.program_id = :programId) Group By Date(wh.created_on);")
    suspend fun getSummarizedReport(dateFor: LocalDateTime, programId:Int, dayNo:Int): WorkoutSummary

    @Query("select wh.program_id as programId, wh.day as dayNumber, wh.workout_id as workoutId, sum(wh.duration_seconds) as totalSecondsActive from workout_history wh Group By wh.program_id, wh.day, wh.workout_id")
    suspend fun getProgressByDays() : List<WorkoutProgressByDays>
}