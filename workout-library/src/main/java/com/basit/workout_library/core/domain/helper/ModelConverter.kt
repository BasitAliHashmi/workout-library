package com.basit.workout_library.core.domain.helper

import com.basit.workout_library.core.data.entity.WorkoutHistoryEntity
import com.basit.workout_library.core.domain.model.WorkoutHistory

internal class ModelConverter {

    companion object {
        fun convertToWorkoutHistoryEntity(model: WorkoutHistory): WorkoutHistoryEntity {

            return WorkoutHistoryEntity(
                id = model.id,
                programId = model.programId,
                day = model.day,
                workoutId = model.workoutId,
                durationSeconds = model.durationSeconds,
                createdOn = model.createdOn
            )
        }

        fun convertToWorkoutHistoryModel(entity: WorkoutHistoryEntity): WorkoutHistory {

            return WorkoutHistory(
                id = entity.id,
                programId = entity.programId,
                day = entity.day,
                workoutId = entity.workoutId,
                durationSeconds = entity.durationSeconds,
                createdOn = entity.createdOn
            )
        }

    }
}