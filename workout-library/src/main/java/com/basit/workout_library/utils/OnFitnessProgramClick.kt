package com.basit.workout_library.utils

import com.basit.workout_library.models.FitnessProgram

internal interface OnFitnessProgramClick {
    fun onItemClick(fitnessProgram: FitnessProgram, dayIndex: Int)
}