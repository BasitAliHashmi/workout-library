package com.basit.workout_library.models

import android.graphics.Color
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MiniWorkoutCardOptions(val cardTitle:String = "Quick Workout's",
                                  val cardTitleVisibility:Boolean = true,
                                  val cardBackgroundColor:Int = Color.parseColor("#FFE082")
): Parcelable
