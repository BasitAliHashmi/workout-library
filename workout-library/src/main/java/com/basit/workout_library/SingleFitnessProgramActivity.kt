package com.basit.workout_library

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
//import androidx.navigation.NavController
//import androidx.navigation.findNavController
import com.basit.workout_library.databinding.ActivitySingleFitnessProgramBinding
import com.basit.workout_library.fragment.BrowseFitnessProgramDayFragment
import com.basit.workout_library.fragment.FitnessProgramFinishFragment
import com.basit.workout_library.fragment.FitnessProgramFragment
import com.basit.workout_library.models.FitnessProgram

internal class SingleFitnessProgramActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySingleFitnessProgramBinding
    //private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySingleFitnessProgramBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //navController = findNavController(R.id.nav_container)
        //setNavGraph()
        launchFragment()
    }

    fun launchFragment(launchMode:String = "browseDay"){

        val fitnessProgram = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("fitnessProgram", FitnessProgram::class.java)
        } else {
            intent.getParcelableExtra("fitnessProgram")
        }

        fitnessProgram?.let {

            var frag: Fragment? = null

            when (launchMode) {
                "browseDay" -> {

                    frag = BrowseFitnessProgramDayFragment.newInstance(
                        intent.getIntExtra("dayIndex", 0),
                        intent.getIntExtra("color", 0),
                        it
                    )
                }

                "programStart" -> {
                    frag = FitnessProgramFragment.newInstance(
                        intent.getIntExtra("dayIndex", 0),
                        intent.getIntExtra("color", 0),
                        it
                    )
                }

                "finish" -> {
                    frag = FitnessProgramFinishFragment.newInstance(
                        intent.getIntExtra("dayIndex", 0),
                        intent.getIntExtra("color", 0),
                        it
                    )
                }
            }


            frag?.let { currentFrag ->
                val trans = supportFragmentManager.beginTransaction()
                trans.replace(R.id.container, currentFrag, "Hello")
                trans.commit()
            }
        }
    }

    /*private fun setNavGraph() {
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
    }*/

}