package com.basit.workout_library.fragment

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.basit.workout_library.SingleFitnessProgramActivity
import com.basit.workout_library.WorkoutLibrary
//import androidx.navigation.fragment.navArgs
import com.basit.workout_library.base.BaseWorkoutFrag
import com.basit.workout_library.databinding.FragmentFinishFitnessProgramBinding
import com.basit.workout_library.models.FitnessProgram
import java.time.LocalDateTime
import kotlin.math.roundToInt

private const val ARG_DAY_INDEX = "dayIndex"
private const val ARG_FITNESS_PROGRAMS = "fitnessProgram"

internal class FitnessProgramFinishFragment : BaseWorkoutFrag() {

    private var paramFitnessPrograms: FitnessProgram? = null
    private var paramDayIndex: Int? = null

    private lateinit var binding: FragmentFinishFitnessProgramBinding
    private lateinit var viewModel: FitnessProgramViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            paramDayIndex = it.getInt(ARG_DAY_INDEX)
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
        binding = FragmentFinishFitnessProgramBinding.inflate(inflater,container,false)
        //viewModel = ViewModelProvider(this)[FitnessProgramViewModel::class.java]

        viewModel = ViewModelProvider(
            this,
            FitnessProgramViewModelFactory(WorkoutLibrary.getInstance().workoutHistoryRepository)
        )[FitnessProgramViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindData()
        loadData()
        setObservers()
        setOnClickListener()

        /*logFirebaseScreenViewEvent(
            "Training Mode Finish",
            FitnessProgramFinishFragment::class.java.simpleName
        )*/
    }

    private fun loadData() {
        viewModel.getSummarizedReport(
            LocalDateTime.now(),
            paramFitnessPrograms!!.id,
            paramDayIndex!! + 1
        )
    }

    private fun setObservers(){
        viewModel.singleDaySummaryReport.observe(viewLifecycleOwner) {
            if (it != null) {
                val totalMinutes = ((it.TotalSeconds.toFloat() / 60.0F) * 100.0F).roundToInt() / 100.0F
                binding.lblProgress.text = "You were active for $totalMinutes minutes"
            }
        }
    }

    private fun bindData(){
        val fitnessProgram = paramFitnessPrograms!!
        val dayIndex = paramDayIndex!!
        val color = paramFitnessPrograms!!.color

        binding.layoutRoot.setBackgroundColor(color)
        (requireActivity() as SingleFitnessProgramActivity).updateStatusBarColor(color,false)
        binding.lblTitle.text = fitnessProgram.title
        binding.lblDay.text = "Congratulations! Day ${dayIndex + 1} finished."

        /*Glide.with(this)
            .load(fitnessProgram.iconResourceId)
            .placeholder(ColorDrawable(Color.WHITE))
            .error(ColorDrawable(Color.WHITE))
            .into(binding.imgIcon)*/
    }

    private fun setOnClickListener() {
        binding.lblRate.setOnClickListener {
            val url = "market://details?id=${activity?.packageName}"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)

            startActivity(i)
        }

        binding.btnDiscoverMore.setOnClickListener {
            val intent = Intent()
            intent.putExtra("nav", "discover")
            requireActivity().setResult(AppCompatActivity.RESULT_OK, intent)
            requireActivity().finish()
        }

        binding.btnClose.setOnClickListener {
            requireActivity().finish()
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
        fun newInstance(dayIndex: Int, fitnessPrograms: FitnessProgram) =
            FitnessProgramFinishFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_DAY_INDEX, dayIndex)
                    putParcelable(ARG_FITNESS_PROGRAMS, fitnessPrograms)
                }
            }
    }
}