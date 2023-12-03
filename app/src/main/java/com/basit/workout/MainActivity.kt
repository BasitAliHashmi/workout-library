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

class MainActivity : AppCompatActivity(), FitnessProgramListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var workoutFragment: ListFitnessProgramsFragment
    //private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createWorkoutFragment()
        //initFitnessProgramNavComponent()
        //initReportsNavComponent()
    }

    private fun createWorkoutFragment() {

        workoutFragment = ListFitnessProgramsFragment.newInstance(
            "Workout Library App",
            null,
            false,
            LoseBellyFatProgram.program()
        )

        val trans = supportFragmentManager.beginTransaction()
        trans.add(R.id.container, workoutFragment, "Hello")
        trans.commit()
    }

    /*private fun initFitnessProgramNavComponent() {
        navController = findNavController(R.id.nav_container)

        //navController.popBackStack()
        val destinationArgs = Bundle().apply {
            putString("param_title", "Nav Component")
            putString("param_admob_banner_unit_id", null)
            putBoolean("param_enable_adds", false)
            putParcelableArrayList(
                "param_fitness_programs",
                arrayListOf(
                    LoseBellyFatProgram.program(),
                    LoseBellyFatProgram.program(),
                    LoseBellyFatProgram.program()
                )
            )
        }

        //navController.navigate(com.basit.workout_library.R.id.workout_library_navigation)
        navController.setGraph(R.navigation.my_nav, destinationArgs)
    }

    private fun initReportsNavComponent() {
        navController = findNavController(R.id.nav_container)

        //navController.popBackStack()
        val destinationArgs = Bundle().apply {
            putParcelableArrayList(
                "param_fitness_programs",
                arrayListOf(
                    LoseBellyFatProgram.program(),
                    LoseBellyFatProgram.program(),
                    LoseBellyFatProgram.program()
                )
            )
        }

        //navController.navigate(com.basit.workout_library.R.id.workout_library_navigation)
        navController.setGraph(R.navigation.my_nav, destinationArgs)
    }*/

    override fun onFitnessProgramDaySelect(fitnessProgram: FitnessProgram, dayIndex: Int) {
        Toast.makeText(this@MainActivity, "i day selected", Toast.LENGTH_SHORT).show()
    }

    override fun onFitnessProgramEnd(resultCode: Int) {
        Toast.makeText(this@MainActivity, "i program end", Toast.LENGTH_SHORT).show()
    }


}