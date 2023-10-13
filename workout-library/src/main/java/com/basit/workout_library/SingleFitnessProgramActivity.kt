package com.basit.workout_library

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.basit.workout_library.databinding.ActivitySingleFitnessProgramBinding
import com.basit.workout_library.models.FitnessProgram

internal class SingleFitnessProgramActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySingleFitnessProgramBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySingleFitnessProgramBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.nav_container)
        setNavGraph()
    }

    private fun setNavGraph() {
        val fitnessProgram = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("fitnessProgram", FitnessProgram::class.java)
        } else {
            intent.getParcelableExtra("fitnessProgram")
        }

        val startDestinationArgs = Bundle().apply {
            putInt("dayIndex", intent.getIntExtra("dayIndex", 0))
            putInt("color", intent.getIntExtra("color", 0))
            putParcelable("fitnessProgram", fitnessProgram)
        }
        navController.setGraph(R.navigation.fitness_program_navigation, startDestinationArgs)
    }

}