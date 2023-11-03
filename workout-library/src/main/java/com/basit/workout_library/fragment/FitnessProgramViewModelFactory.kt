package com.basit.workout_library.fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.basit.workout_library.core.domain.WorkoutHistoryRepository

internal class FitnessProgramViewModelFactory(private val workoutHistoryRepository: WorkoutHistoryRepository): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FitnessProgramViewModel::class.java)) {
            return FitnessProgramViewModel(workoutHistoryRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}