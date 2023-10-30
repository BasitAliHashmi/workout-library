package com.basit.workout_library.listeners

interface FitnessProgramListener {
    fun onFitnessProgramStart()
    fun onFitnessProgramEnd(resultCode: Int)
}