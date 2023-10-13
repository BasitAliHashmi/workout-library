package com.basit.workout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.basit.workout.databinding.ActivityMainBinding
import com.basit.workout.model.LoseBellyFatProgram
import com.basit.workout_library.fragment.ListFitnessProgramsFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var workoutFragment: ListFitnessProgramsFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createWorkoutFragment()
    }

    private fun createWorkoutFragment() {

        workoutFragment = ListFitnessProgramsFragment.newInstance(
            "Workout Library App",
            LoseBellyFatProgram.program(),
            LoseBellyFatProgram.program()
        )

        val trans = supportFragmentManager.beginTransaction()
        trans.add(R.id.container, workoutFragment, "Hello")
        trans.commit()
    }

}