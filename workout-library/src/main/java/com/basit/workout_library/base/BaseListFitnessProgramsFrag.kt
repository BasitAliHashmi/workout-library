package com.basit.workout_library.base

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import com.basit.workout_library.SingleFitnessProgramActivity
import com.basit.workout_library.WorkoutLibrary
import com.basit.workout_library.adapter.ListFitnessProgramsAdapter
import com.basit.workout_library.databinding.FragmentListFitnessProgramsBinding
import com.basit.workout_library.listeners.FitnessProgramListener
import com.basit.workout_library.models.FitnessProgram
import com.basit.workout_library.utils.OnFitnessProgramClick
import com.basit.workout_library.utils.WorkoutLibraryExtensions.showHorizontalPreview

abstract class BaseListFitnessProgramsFrag : BaseWorkoutFrag(), OnFitnessProgramClick {

    protected var paramTitle: String? = null
    protected var paramAdmobBannerAdUnitId: String? = null
    protected var paramEnableAdds: Boolean = false
    protected var paramFitnessPrograms: List<FitnessProgram>? = null

    private lateinit var binding: FragmentListFitnessProgramsBinding
    private lateinit var adapter: ListFitnessProgramsAdapter

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

        binding.lblTitle.text = paramTitle
    }

    override fun onDayClick(fitnessProgram: FitnessProgram, dayIndex: Int) {

        if (requireActivity() is FitnessProgramListener) {
            WorkoutLibrary.getInstance().mFitnessProgramListener = requireActivity() as FitnessProgramListener
            WorkoutLibrary.getInstance().mFitnessProgramListener?.onFitnessProgramDaySelect(fitnessProgram, dayIndex)
        }

        val intent = Intent(requireContext(), SingleFitnessProgramActivity::class.java)
        intent.putExtra("fitnessProgram", fitnessProgram)
        intent.putExtra("dayIndex", dayIndex)
        intent.putExtra("bannerUnitId", paramAdmobBannerAdUnitId)
        intent.putExtra("enableAdds", paramEnableAdds)

        getResultSingleFitnessProgramActivity.launch(intent)
    }

    private val getResultSingleFitnessProgramActivity =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            WorkoutLibrary.getInstance().mFitnessProgramListener?.onFitnessProgramEnd(it.resultCode)
        }


}