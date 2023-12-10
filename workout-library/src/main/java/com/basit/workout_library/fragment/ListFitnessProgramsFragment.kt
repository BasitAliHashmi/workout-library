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
import com.basit.workout_library.utils.OnFitnessProgramClick
import com.basit.workout_library.utils.WorkoutLibraryExtensions.showHorizontalPreview
import java.util.ArrayList

private const val ARG_FITNESS_PROGRAMS = "param_fitness_programs"

class ListFitnessProgramsFragment : BaseWorkoutFrag(), OnFitnessProgramClick {

    private var paramFitnessPrograms: List<FitnessProgram>? = null
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
                ListFitnessProgramsAdapter(it, requireContext(), this)
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

    override fun onDayClick(fitnessProgram: FitnessProgram, dayIndex: Int) {

        mFitnessProgramListener?.onFitnessProgramDaySelect(fitnessProgram, dayIndex)

        val intent = Intent(requireContext(), SingleFitnessProgramActivity::class.java)
        intent.putExtra("fitnessProgram", fitnessProgram)
        intent.putExtra("dayIndex", dayIndex)
        intent.putExtra("bannerUnitId", mAdmobBannerAdUnitId)
        intent.putExtra("enableAdds", mEnableAdds)

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
                    putParcelableArrayList(
                        ARG_FITNESS_PROGRAMS,
                        fitnessPrograms.toCollection(ArrayList())
                    )
                }
            }
    }

}