package com.basit.workout_library.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BaseSimpleWorkout(val id:Int,
                             val title:String,
                             val animatedImageResourceId:Int,
                             val stillImageResourceId:Int
): Parcelable
