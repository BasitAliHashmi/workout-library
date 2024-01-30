package com.basit.workout_library.core.domain.workouthistory

import androidx.annotation.WorkerThread
import com.basit.workout_library.core.data.dao.WorkoutHistoryDao
import com.basit.workout_library.core.data.entity.WorkoutHistoryEntity
import com.basit.workout_library.core.domain.model.WorkoutProgressByDays
import com.basit.workout_library.core.domain.model.WorkoutSummary
import java.time.LocalDateTime

internal class WorkoutHistoryLocalDataSource(private val workoutHistoryDao: WorkoutHistoryDao) {

    suspend fun insert(entity: WorkoutHistoryEntity){
        workoutHistoryDao.insert(entity)
    }

    suspend fun getSummarizedReport(from: LocalDateTime, to: LocalDateTime, programId:Int):List<WorkoutSummary> {
        return workoutHistoryDao.getSummarizedReport(from, to, programId)
    }

    suspend fun getSummarizedReport(dateFor: LocalDateTime, programId:Int, dayNo:Int):WorkoutSummary {
        return workoutHistoryDao.getSummarizedReport(dateFor, programId, dayNo)
    }

    @WorkerThread
    suspend fun getProgressByDays():List<WorkoutProgressByDays> {
        return workoutHistoryDao.getProgressByDays()
    }
}