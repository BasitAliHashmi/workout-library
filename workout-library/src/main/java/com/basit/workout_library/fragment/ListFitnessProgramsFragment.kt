package com.basit.workout_library.fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.basit.workout_library.SingleFitnessProgramActivity
import com.basit.workout_library.adapter.ListFitnessProgramsAdapter
import com.basit.workout_library.base.WorkoutLibBaseFragment
import com.basit.workout_library.databinding.FragmentListFitnessProgramsBinding
import com.basit.workout_library.models.FitnessProgram
import com.basit.workout_library.utils.OnFitnessProgramClick
import com.basit.workout_library.utils.WorkoutLibraryExtensions.showHorizontalPreview
import com.basit.workout_library.utils.WorkoutLibraryHelper
import java.util.ArrayList

private const val ARG_TITLE = "param1"
private const val ARG_FITNESS_PROGRAMS = "param2"

class ListFitnessProgramsFragment : WorkoutLibBaseFragment(), OnFitnessProgramClick {

    private var paramTitle: String? = null
    private var paramFitnessPrograms: List<FitnessProgram>? = null

    private lateinit var binding:FragmentListFitnessProgramsBinding
    private lateinit var adapter:ListFitnessProgramsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!WorkoutLibraryHelper.isSetupWithNavigationComponent) {
            arguments?.let {
                paramTitle = it.getString(ARG_TITLE)
                paramFitnessPrograms = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    it.getParcelableArrayList(ARG_FITNESS_PROGRAMS, FitnessProgram::class.java)
                } else {
                    it.getParcelableArrayList(ARG_FITNESS_PROGRAMS)
                }
            }
        } else {
            paramTitle = WorkoutLibraryHelper.workoutLibraryTitle
            paramFitnessPrograms = WorkoutLibraryHelper.workoutLibraryFitnessPrograms
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListFitnessProgramsBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        paramFitnessPrograms?.let {
            adapter =
                ListFitnessProgramsAdapter(it, requireContext(), this@ListFitnessProgramsFragment)
            binding.viewPager.showHorizontalPreview(25, 25, 10)
            binding.viewPager.adapter = adapter
        }

        binding.lblTitle.text = paramTitle

    }

    override fun onDayClick(fitnessProgram: FitnessProgram, dayIndex: Int, color:Int) {
        /*(requireActivity() as MainActivity).navigateToFitnessProgramActivity(
            fitnessProgram,
            dayIndex,
            color
        )*/

        val intent = Intent(requireContext(), SingleFitnessProgramActivity::class.java)
        intent.putExtra("fitnessProgram", fitnessProgram)
        intent.putExtra("dayIndex", dayIndex)
        intent.putExtra("color", color)

        startActivity(intent)
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