package com.basit.workout_library.fragment

import android.os.Build
import android.os.Bundle
import com.basit.workout_library.base.BaseListFitnessProgramsFrag
import com.basit.workout_library.models.FitnessProgram
import java.util.ArrayList

private const val ARG_TITLE = "param1"
private const val ARG_FITNESS_PROGRAMS = "param2"

class ListFitnessProgramsFragment : BaseListFitnessProgramsFrag() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            paramTitle = it.getString(ARG_TITLE)
            paramFitnessPrograms = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelableArrayList(ARG_FITNESS_PROGRAMS, FitnessProgram::class.java)
            } else {
                it.getParcelableArrayList(ARG_FITNESS_PROGRAMS)
            }
        }
    }

    companion object {

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param title Parameter 1.
         * @param fitnessPrograms Parameter 2.
         * @return A new instance of fragment BlankFragment.
         */
        @JvmStatic
        fun newInstance(title: String, vararg fitnessPrograms: FitnessProgram) =
            ListFitnessProgramsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TITLE, title)
                    putParcelableArrayList(ARG_FITNESS_PROGRAMS, fitnessPrograms.toCollection(ArrayList()))
                }
            }
    }

}