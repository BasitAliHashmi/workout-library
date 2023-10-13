package com.basit.workout_library.fragment

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.basit.workout_library.adapter.FitnessProgramWorkoutsAdapter
import com.basit.workout_library.base.WorkoutLibBaseFragment
import com.basit.workout_library.databinding.FragmentBrowseFitnessProgramDayBinding
import com.basit.workout_library.models.FitnessProgramDifficulty
import com.basit.workout_library.utils.EmptySpaceAtBottomDecorator
import com.bumptech.glide.Glide

internal class BrowseFitnessProgramDayFragment : WorkoutLibBaseFragment() {

    private lateinit var binding: FragmentBrowseFitnessProgramDayBinding
    private val args: BrowseFitnessProgramDayFragmentArgs by navArgs()

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

        val fitnessProgram = args.fitnessProgram
        val dayIndex = args.dayIndex
        val color = args.color

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
                findNavController().navigate(
                    BrowseFitnessProgramDayFragmentDirections.actionBrowseDayToFitnessProgram(
                        dayIndex,
                        color,
                        fitnessProgram
                    )
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
}