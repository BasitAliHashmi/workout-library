package com.basit.workout_library.core.domain

import androidx.annotation.WorkerThread
import com.basit.workout_library.core.domain.helper.ModelConverter
import com.basit.workout_library.core.domain.model.WorkoutHistory
import com.basit.workout_library.core.domain.model.WorkoutProgressByDays
import com.basit.workout_library.core.domain.model.WorkoutSummary
import com.basit.workout_library.core.domain.workouthistory.WorkoutHistoryLocalDataSource
import java.time.LocalDateTime

internal class WorkoutHistoryRepository(private val localDataSource: WorkoutHistoryLocalDataSource) {

    @WorkerThread
    suspend fun insert(workoutHistory: WorkoutHistory) {
        localDataSource.insert(ModelConverter.convertToWorkoutHistoryEntity(workoutHistory))
    }

    @WorkerThread
    suspend fun getSummarizedReport(from: LocalDateTime, to: LocalDateTime, programId:Int):List<WorkoutSummary> {
        return localDataSource.getSummarizedReport(from, to, programId)
    }

    @WorkerThread
    suspend fun getSummarizedReport(dateFor: LocalDateTime, programId:Int, dayNo:Int):WorkoutSummary {
        return localDataSource.getSummarizedReport(dateFor, programId, dayNo)
    }

    @WorkerThread
    suspend fun getProgressByDays():List<WorkoutProgressByDays> {
        return localDataSource.getProgressByDays()
    }
}