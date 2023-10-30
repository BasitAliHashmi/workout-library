package com.basit.workout_library.fragment

import android.os.Bundle
import com.basit.workout_library.base.BaseListFitnessProgramsFrag
import com.basit.workout_library.utils.WorkoutLibraryHelper

class NavListFitnessProgramsFragment: BaseListFitnessProgramsFrag() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        paramTitle = WorkoutLibraryHelper.workoutLibraryTitle
        paramFitnessPrograms = WorkoutLibraryHelper.workoutLibraryFitnessPrograms
    }
}