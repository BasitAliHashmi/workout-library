package com.basit.workout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
//import androidx.navigation.NavController
//import androidx.navigation.findNavController
import com.basit.workout.databinding.ActivityMainBinding
import com.basit.workout.model.LoseBellyFatProgram
import com.basit.workout_library.fragment.ListFitnessProgramsFragment
import com.basit.workout_library.listeners.FitnessProgramListener
import com.basit.workout_library.models.FitnessProgram

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
            LoseBellyFatProgram.program()
        )

        val trans = supportFragmentManager.beginTransaction()
        trans.add(R.id.container, workoutFragment, "Hello")
        trans.commit()

        workoutFragment.addListener(object : FitnessProgramListener {
            override fun onFitnessProgramDaySelect(fitnessProgram: FitnessProgram, dayIndex: Int) {
                Toast.makeText(this@MainActivity, "$dayIndex day selected", Toast.LENGTH_SHORT).show()
            }

            override fun onFitnessProgramEnd(resultCode: Int) {
                Toast.makeText(this@MainActivity, "i program end", Toast.LENGTH_SHORT).show()
            }
        })
    }


}