package com.basit.workout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.basit.workout.databinding.ActivityMainBinding
import com.basit.workout.model.LoseBellyFatProgram
import com.basit.workout.model.MiniWakeupWorkoutProgram
import com.basit.workout_library.fragment.ListFitnessProgramsFragment
import com.basit.workout_library.fragment.ReportsFragment
import com.basit.workout_library.listeners.FitnessProgramListener
import com.basit.workout_library.models.FitnessProgram
import com.basit.workout_library.models.MiniWorkoutCardOptions

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var workoutFragment: ListFitnessProgramsFragment
    private lateinit var reportsFragment: ReportsFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //createWorkoutFragment()
        createReportsFragment()
    }

    private fun createWorkoutFragment() {

        /*workoutFragment = ListFitnessProgramsFragment.newInstance(
            miniWorkoutCardOptions = MiniWorkoutCardOptions(cardTitle = "Mini Workout's"),
            LoseBellyFatProgram.program(),
            MiniWakeupWorkoutProgram.program()
        )*/

        workoutFragment = ListFitnessProgramsFragment.newInstance(
            LoseBellyFatProgram.program()
        )

        val trans = supportFragmentManager.beginTransaction()
        trans.add(R.id.container, workoutFragment, "Hello")
        trans.commit()

        workoutFragment.addListener(object : FitnessProgramListener {
            override fun onFitnessProgramDaySelect(fitnessProgram: FitnessProgram, dayIndex: Int) {
                Toast.makeText(this@MainActivity, "$dayIndex: ${fitnessProgram.title} ", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onFitnessProgramEnd(resultCode: Int) {
                Toast.makeText(this@MainActivity, "i program end", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun createReportsFragment() {

        reportsFragment = ReportsFragment.newInstance(
            LoseBellyFatProgram.program()
        )

        val trans = supportFragmentManager.beginTransaction()
        trans.add(R.id.container, reportsFragment, "Hello")
        trans.commit()
    }


}