package com.basit.workout_library.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FitnessProgram (
    val id:Int,
    val title:String,
    val difficulty: FitnessProgramDifficulty,
    val days:List<FitnessProgramDay> = arrayListOf(),
    val iconResourceId:Int
): Parcelable