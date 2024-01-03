package com.basit.workout_library

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.basit.workout_library.databinding.ActivitySingleFitnessProgramBinding
import com.basit.workout_library.models.FitnessProgram

class SingleFitnessProgramActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySingleFitnessProgramBinding
    private lateinit var navController: NavController

    private var mAddSkdInit = false
    private var mSingleDayProgram = false
    private var mAdmobBannerAdUnitId:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySingleFitnessProgramBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.nav_container)
        mAddSkdInit = intent.getBooleanExtra("enableAdds", false)
        mSingleDayProgram = intent.getBooleanExtra("singleDayProgram", false)
        mAdmobBannerAdUnitId = intent.getStringExtra("bannerUnitId")
        setNavGraph()
    }

    private fun setNavGraph() {
        val fitnessProgram = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("fitnessProgram", FitnessProgram::class.java)
        } else {
            intent.getParcelableExtra("fitnessProgram")
        }

        val startDestinationArgs = if (!mSingleDayProgram){
            Bundle().apply {
                putInt("dayIndex", intent.getIntExtra("dayIndex", 0))
                putParcelable("fitnessProgram", fitnessProgram)
            }
        } else {
            Bundle().apply {
                putInt("dayIndex", 0)
                putBoolean("singleDayProgram", true)
                putParcelable("fitnessProgram", fitnessProgram)
            }
        }

        navController.setGraph(
            R.navigation.workout_library_program_navigation,
            startDestinationArgs
        )
    }

    fun getBannerAdUnitId():String? {
        return mAdmobBannerAdUnitId
    }

    fun addSdkInit():Boolean {
        return mAddSkdInit
    }

    override fun onPause() {
        WorkoutLibrary.getInstance().stopTts()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        WorkoutLibrary.getInstance().initTextToSpeech(applicationContext)
    }

    fun updateStatusBarColor(color:Int, lightStatusBar:Boolean) {

        window.statusBarColor = color
        window.navigationBarColor = color

        if (lightStatusBar) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                window.insetsController?.setSystemBarsAppearance(
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                )
                window.insetsController?.setSystemBarsAppearance(
                    WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS,
                    WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
                )

            } else {
                @Suppress("DEPRECATION")
                window.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                window.insetsController?.setSystemBarsAppearance(
                    0,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                )
                window.insetsController?.setSystemBarsAppearance(
                    0,
                    WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
                )

            } else {
                @Suppress("DEPRECATION")
                window.decorView.systemUiVisibility =
                    View.STATUS_BAR_VISIBLE
            }
        }
    }

}