package com.basit.workout_library.listeners

import com.basit.workout_library.models.FitnessProgram

interface FitnessProgramListener {

    fun onFitnessProgramDaySelect(fitnessProgram: FitnessProgram, dayIndex: Int)
    fun onFitnessProgramEnd(resultCode: Int)
}