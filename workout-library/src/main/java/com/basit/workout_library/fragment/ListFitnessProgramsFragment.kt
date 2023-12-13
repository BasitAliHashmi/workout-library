package com.basit.workout_library.fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import com.basit.workout_library.SingleFitnessProgramActivity
import com.basit.workout_library.WorkoutLibrary
import com.basit.workout_library.adapter.ListFitnessProgramsAdapter
import com.basit.workout_library.base.BaseWorkoutFrag
import com.basit.workout_library.databinding.FragmentListFitnessProgramsBinding
import com.basit.workout_library.listeners.FitnessProgramListener
import com.basit.workout_library.models.FitnessProgram
import com.basit.workout_library.models.MiniWorkoutCardOptions
import com.basit.workout_library.utils.OnFitnessProgramClick
import com.basit.workout_library.utils.WorkoutLibraryExtensions.showHorizontalPreview
import java.util.ArrayList

private const val ARG_FITNESS_PROGRAMS = "param_fitness_programs"
private const val ARG_MINI_WORKOUT_CARD_OPTIONS = "param_mini_workout_card_options"

class ListFitnessProgramsFragment : BaseWorkoutFrag(), OnFitnessProgramClick {

    private var paramFitnessPrograms: List<FitnessProgram>? = null
    private var paramMiniWorkoutCardOptions: MiniWorkoutCardOptions? = null

    private var mFitnessProgramListener: FitnessProgramListener? = null

    private lateinit var binding: FragmentListFitnessProgramsBinding
    private lateinit var adapter: ListFitnessProgramsAdapter

    private var mAdmobBannerAdUnitId: String? = null
    private var mEnableAdds: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            paramFitnessPrograms = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelableArrayList(ARG_FITNESS_PROGRAMS, FitnessProgram::class.java)
            } else {
                it.getParcelableArrayList(ARG_FITNESS_PROGRAMS)
            }

            paramMiniWorkoutCardOptions =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    it.getParcelable(
                        ARG_MINI_WORKOUT_CARD_OPTIONS,
                        MiniWorkoutCardOptions::class.java
                    )
                } else {
                    it.getParcelable(
                        ARG_MINI_WORKOUT_CARD_OPTIONS
                    )
                }
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
                ListFitnessProgramsAdapter(
                    getSimpleFitnessPrograms(it),
                    getMiniFitnessPrograms(it),
                    requireContext(),
                    paramMiniWorkoutCardOptions!!,
                    this
                )
            if (it.size > 1) {
                binding.viewPager.showHorizontalPreview(25, 25, 10)
            } else {
                val lp = binding.viewPager.layoutParams as ConstraintLayout.LayoutParams
                lp.marginStart = 25
                lp.marginEnd = 25
                binding.viewPager.layoutParams = lp

                binding.viewPager.isUserInputEnabled = false
            }
            binding.viewPager.adapter = adapter
        }
    }

    private fun getSimpleFitnessPrograms(input:List<FitnessProgram>):List<FitnessProgram> {
        return input.filter { x -> x.days.size > 1 }
    }

    private fun getMiniFitnessPrograms(input:List<FitnessProgram>):List<FitnessProgram> {
        return input.filter { x -> x.days.size == 1 }
    }

    override fun onItemClick(fitnessProgram: FitnessProgram, dayIndex: Int) {

        mFitnessProgramListener?.onFitnessProgramDaySelect(fitnessProgram, dayIndex)

        val intent = Intent(requireContext(), SingleFitnessProgramActivity::class.java)
        intent.putExtra("fitnessProgram", fitnessProgram)
        intent.putExtra("dayIndex", dayIndex)
        intent.putExtra("bannerUnitId", mAdmobBannerAdUnitId)
        intent.putExtra("enableAdds", mEnableAdds)

        if (fitnessProgram.days.size == 1)
            intent.putExtra("singleDayProgram", true)

        getResultSingleFitnessProgramActivity.launch(intent)
    }

    fun enableAds(admobBannerUnitId: String) {
        mAdmobBannerAdUnitId = admobBannerUnitId
        mEnableAdds = true
    }

    fun disableAds() {
        mAdmobBannerAdUnitId = null
        mEnableAdds = false
    }

    fun addListener(listener: FitnessProgramListener){
        mFitnessProgramListener = listener
    }

    private val getResultSingleFitnessProgramActivity =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            mFitnessProgramListener?.onFitnessProgramEnd(it.resultCode)
        }

    companion object {

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param fitnessPrograms Parameter 1.
         * @return A new instance of fragment BlankFragment.
         */
        @JvmStatic
        fun newInstance(
            vararg fitnessPrograms: FitnessProgram
        ) =
            ListFitnessProgramsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_MINI_WORKOUT_CARD_OPTIONS, MiniWorkoutCardOptions())
                    putParcelableArrayList(
                        ARG_FITNESS_PROGRAMS,
                        fitnessPrograms.toCollection(ArrayList())
                    )
                }
            }

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param miniWorkoutCardOptions Parameter 1.
         * @param fitnessPrograms Parameter 1.
         * @return A new instance of fragment BlankFragment.
         */
        @JvmStatic
        fun newInstance(
            miniWorkoutCardOptions: MiniWorkoutCardOptions,
            vararg fitnessPrograms: FitnessProgram
        ) =
            ListFitnessProgramsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_MINI_WORKOUT_CARD_OPTIONS, miniWorkoutCardOptions)
                    putParcelableArrayList(
                        ARG_FITNESS_PROGRAMS,
                        fitnessPrograms.toCollection(ArrayList())
                    )
                }
            }
    }

}