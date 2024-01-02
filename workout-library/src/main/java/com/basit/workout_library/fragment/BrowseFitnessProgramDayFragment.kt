package com.basit.workout_library.fragment

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.basit.workout_library.R
import com.basit.workout_library.SingleFitnessProgramActivity
import com.basit.workout_library.WorkoutLibrary
import com.basit.workout_library.adapter.FitnessProgramWorkoutsAdapter
import com.basit.workout_library.base.BaseWorkoutFrag
import com.basit.workout_library.databinding.FragmentBrowseFitnessProgramDayBinding
import com.basit.workout_library.models.FitnessProgram
import com.basit.workout_library.models.FitnessProgramDifficulty
import com.basit.workout_library.utils.EmptySpaceAtBottomDecorator
import com.bumptech.glide.Glide

private const val ARG_DAY_INDEX = "dayIndex"
private const val ARG_FITNESS_PROGRAMS = "fitnessProgram"
private const val ARG_SINGLE_DAY_PROGRAM = "singleDayProgram"

internal class BrowseFitnessProgramDayFragment : BaseWorkoutFrag() {

    private var paramFitnessPrograms: FitnessProgram? = null
    private var paramDayIndex: Int? = null
    private var paramSingleDayProgram = false

    private lateinit var binding: FragmentBrowseFitnessProgramDayBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            paramDayIndex = it.getInt(ARG_DAY_INDEX)
            paramSingleDayProgram = it.getBoolean(ARG_SINGLE_DAY_PROGRAM,false)
            paramFitnessPrograms = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelable(ARG_FITNESS_PROGRAMS, FitnessProgram::class.java)
            } else {
                it.getParcelable(ARG_FITNESS_PROGRAMS)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBrowseFitnessProgramDayBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindData()
        /*logFirebaseScreenViewEvent(
            "Browse Day",
            BrowseFitnessProgramDayFragment::class.java.simpleName
        )
        FatLossAppReviewManager.getInstance().tryAskForReview(requireActivity())*/
    }

    private fun bindData() {

        binding.panelRest.visibility = View.GONE

        val fitnessProgram = paramFitnessPrograms!!
        val dayIndex = paramDayIndex!!

        val alphaFull = 1.0f
        val alphaDim = 0.4f

        binding.iconBeginner.alpha = alphaDim
        binding.iconIntermediate.alpha = alphaDim
        binding.iconAdvance.alpha = alphaDim

        when (fitnessProgram.difficulty) {
            FitnessProgramDifficulty.Beginner -> {
                binding.iconBeginner.alpha = alphaFull
            }

            FitnessProgramDifficulty.Intermediate -> {
                binding.iconBeginner.alpha = alphaFull
                binding.iconIntermediate.alpha = alphaFull
            }

            FitnessProgramDifficulty.Advance -> {
                binding.iconBeginner.alpha = alphaFull
                binding.iconIntermediate.alpha = alphaFull
                binding.iconAdvance.alpha = alphaFull
            }
        }

        binding.lblDifficulty.text = fitnessProgram.difficulty.name
        binding.lblTitle.text = fitnessProgram.title

        Glide.with(requireContext())
            .load(fitnessProgram.iconResourceId)
            .placeholder(ColorDrawable(Color.CYAN))
            .error(ColorDrawable(Color.CYAN))
            .into(binding.imgIcon)

        //binding.mainCard.setCardBackgroundColor(color)
        val color = paramFitnessPrograms!!.color
        binding.layoutRoot.setBackgroundColor(color)
        binding.btnStart.backgroundTintList = ColorStateList.valueOf(color)
        (requireActivity() as SingleFitnessProgramActivity).updateStatusBarColor(color, false)

        //exercises
        val selectedDay = fitnessProgram.days[dayIndex]
        if (!selectedDay.restDay) {
            if (!paramSingleDayProgram)
                binding.lblDay.text = "Day ${dayIndex + 1}"
            else
                binding.lblDay.visibility = View.GONE

            val workoutAdapter =
                FitnessProgramWorkoutsAdapter(
                    selectedDay.workouts,
                    requireContext()
                )
            with(binding.rvView) {
                addItemDecoration(EmptySpaceAtBottomDecorator(240))
                layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.VERTICAL,
                    false
                )
                adapter = workoutAdapter
            }

            binding.btnStart.setOnClickListener {

                val startDestinationArgs = Bundle().apply {
                    putInt("dayIndex", dayIndex)
                    putParcelable("fitnessProgram", fitnessProgram)
                }
                findNavController().navigate(
                    R.id.action_browse_day_to_fitness_program,
                    startDestinationArgs
                )
            }
        } else {
            binding.lblDay.text = "Rest day"
            with(binding) {
                rvView.visibility = View.GONE
                btnStart.visibility = View.GONE
                panelRest.visibility = View.VISIBLE
            }

            binding.btnDiscoverMore.setOnClickListener {
                val intent = Intent()
                intent.putExtra("nav", "discover")
                requireActivity().setResult(AppCompatActivity.RESULT_OK, intent)
                requireActivity().finish()
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
        fun newInstance(dayIndex: Int,  fitnessPrograms: FitnessProgram) =
            BrowseFitnessProgramDayFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_DAY_INDEX, dayIndex)
                    putParcelable(ARG_FITNESS_PROGRAMS, fitnessPrograms)
                }
            }
    }
}