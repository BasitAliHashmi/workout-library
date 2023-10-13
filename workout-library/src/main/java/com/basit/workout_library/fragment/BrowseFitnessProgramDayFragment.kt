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
//import androidx.navigation.fragment.findNavController
//import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.basit.workout_library.SingleFitnessProgramActivity
import com.basit.workout_library.adapter.FitnessProgramWorkoutsAdapter
import com.basit.workout_library.base.WorkoutLibBaseFragment
import com.basit.workout_library.databinding.FragmentBrowseFitnessProgramDayBinding
import com.basit.workout_library.models.FitnessProgram
import com.basit.workout_library.models.FitnessProgramDifficulty
import com.basit.workout_library.utils.EmptySpaceAtBottomDecorator
import com.bumptech.glide.Glide

private const val ARG_DAY_INDEX = "param1"
private const val ARG_COLOR = "param2"
private const val ARG_FITNESS_PROGRAMS = "param3"

internal class BrowseFitnessProgramDayFragment : WorkoutLibBaseFragment() {

    private var paramFitnessPrograms: FitnessProgram? = null
    private var paramColor: Int? = null
    private var paramDayIndex: Int? = null

    private lateinit var binding: FragmentBrowseFitnessProgramDayBinding
    //private val args: BrowseFitnessProgramDayFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            paramDayIndex = it.getInt(ARG_DAY_INDEX)
            paramColor = it.getInt(ARG_COLOR)
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
        val color = paramColor!!

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

        Glide.with(this)
            .load(fitnessProgram.iconResourceId)
            .placeholder(ColorDrawable(Color.CYAN))
            .error(ColorDrawable(Color.CYAN))
            .into(binding.imgIcon)

        //binding.mainCard.setCardBackgroundColor(color)
        binding.layoutRoot.setBackgroundColor(color)
        binding.btnStart.backgroundTintList = ColorStateList.valueOf(color)
        //(activity as BaseActivityFatLoss).updateStatusBarColor(color, false)

        //exercises
        val selectedDay = fitnessProgram.days[dayIndex]
        if (!selectedDay.restDay) {
            binding.lblDay.text = "Day ${dayIndex + 1}"
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
                (requireActivity() as SingleFitnessProgramActivity).launchFragment("programStart")
                /*findNavController().navigate(
                    BrowseFitnessProgramDayFragmentDirections.actionBrowseDayToFitnessProgram(
                        dayIndex,
                        color,
                        fitnessProgram
                    )
                )*/
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
        fun newInstance(dayIndex: Int, color: Int,  fitnessPrograms: FitnessProgram) =
            BrowseFitnessProgramDayFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_DAY_INDEX, dayIndex)
                    putInt(ARG_COLOR, color)
                    putParcelable(ARG_FITNESS_PROGRAMS, fitnessPrograms)
                }
            }
    }
}