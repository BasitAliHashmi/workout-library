package com.basit.workout

import android.app.Application
import com.basit.workout_library.WorkoutLibrary

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        WorkoutLibrary.initialize(this)
    }

}