package com.basit.workout_library.fragment

import android.os.Build
import android.os.Bundle
import com.basit.workout_library.base.BaseListFitnessProgramsFrag
import com.basit.workout_library.models.FitnessProgram
import com.basit.workout_library.utils.WorkoutLibraryHelper
import java.util.ArrayList

private const val ARG_TITLE = "param_title"
private const val ARG_ADBMOB_BANNER_UNIT_ID = "param_admob_banner_unit_id"
private const val ARG_ENABLE_ADDS = "param_enable_adds"
private const val ARG_FITNESS_PROGRAMS = "param_fitness_programs"

class ListFitnessProgramsFragment : BaseListFitnessProgramsFrag() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            paramTitle = it.getString(ARG_TITLE)
            paramAdmobBannerAdUnitId = it.getString(ARG_ADBMOB_BANNER_UNIT_ID)
            paramEnableAdds = it.getBoolean(ARG_ENABLE_ADDS)
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
         * @param admobBannerUnitId Parameter 2.
         * @param fitnessPrograms Parameter 3.
         * @return A new instance of fragment BlankFragment.
         */
        @JvmStatic
        fun newInstance(
            title: String,
            admobBannerUnitId: String?,
            enableAdds: Boolean,
            vararg fitnessPrograms: FitnessProgram
        ) =
            ListFitnessProgramsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TITLE, title)
                    putString(ARG_ADBMOB_BANNER_UNIT_ID, admobBannerUnitId)
                    putBoolean(ARG_ENABLE_ADDS, enableAdds)
                    putParcelableArrayList(
                        ARG_FITNESS_PROGRAMS,
                        fitnessPrograms.toCollection(ArrayList())
                    )
                }
            }
    }

}