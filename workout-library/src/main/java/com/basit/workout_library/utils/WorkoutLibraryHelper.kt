package com.basit.workout_library.utils

import android.content.Context
import android.graphics.Color
import com.basit.workout_library.R
import com.basit.workout_library.models.FitnessProgram
import java.time.LocalTime
import java.util.ArrayList

class WorkoutLibraryHelper {

    companion object {

        internal var isSetupWithNavigationComponent = false
        internal var workoutLibraryTitle = ""
        internal var workoutLibraryFitnessPrograms:List<FitnessProgram> = arrayListOf()

        fun setupWithNavigationComponent(title:String, vararg fitnessPrograms: FitnessProgram) {
            isSetupWithNavigationComponent = true
            workoutLibraryTitle = title
            workoutLibraryFitnessPrograms = fitnessPrograms.toCollection(ArrayList())
        }

        internal fun getFitnessProgramsColorPalette(context: Context): IntArray {
            val colorPaletteArray =
                context.resources.obtainTypedArray(R.array.fitness_program_color_palette)

            val colorPalette = IntArray(colorPaletteArray.length())
            for (n in 0 until colorPaletteArray.length()) {
                colorPalette[n] = Color.parseColor(colorPaletteArray.getString(n))
            }
            colorPaletteArray.recycle()

            return colorPalette
        }

        internal fun convertSecondsToTime(seconds: Long): String {
            val date = LocalTime.MIN.plusSeconds(seconds)

            return "${date.minute.toString().padStart(2, '0')}:${
                date.second.toString().padStart(2, '0')
            }"
        }
    }

}