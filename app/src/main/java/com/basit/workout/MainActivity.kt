package com.basit.workout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.basit.workout.databinding.ActivityMainBinding
import com.basit.workout.model.LoseBellyFatProgram
import com.basit.workout_library.fragment.ListFitnessProgramsFragment
import com.basit.workout_library.listeners.FitnessProgramListener

class MainActivity : AppCompatActivity(), FitnessProgramListener {

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
            LoseBellyFatProgram.program()
        )
        workoutFragment.addListener(this@MainActivity)

        val trans = supportFragmentManager.beginTransaction()
        trans.add(R.id.container, workoutFragment, "Hello")
        trans.commit()
    }

    override fun onFitnessProgramStart() {
        Toast.makeText(this,"start program", Toast.LENGTH_SHORT).show()
    }

    override fun onFitnessProgramEnd(resultCode: Int) {
        Toast.makeText(this,"end program", Toast.LENGTH_SHORT).show()
    }

}