package com.basit.workout_library.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.basit.workout_library.base.WorkoutLibBaseFragment
import com.basit.workout_library.databinding.FragmentFinishFitnessProgramBinding
import java.time.LocalDateTime
import kotlin.math.roundToInt

internal class FitnessProgramFinishFragment : WorkoutLibBaseFragment() {

    private lateinit var binding: FragmentFinishFitnessProgramBinding
    private lateinit var viewModel: FitnessProgramViewModel
    private val args:FitnessProgramFinishFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFinishFitnessProgramBinding.inflate(inflater,container,false)
        /*viewModel = ViewModelProvider(
            this,
            FitnessProgramViewModelFactory((requireActivity().application as FatLossApp).workoutHistoryRepository)
        )[FitnessProgramViewModel::class.java]*/
        viewModel = ViewModelProvider(this)[FitnessProgramViewModel::class.java]
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
            args.fitnessProgram.id,
            args.dayIndex + 1
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
        val fitnessProgram = args.fitnessProgram
        val dayIndex = args.dayIndex
        val color = args.color

        binding.layoutRoot.setBackgroundColor(color)
        //(activity as BaseActivityFatLoss).updateStatusBarColor(color,false)
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

}