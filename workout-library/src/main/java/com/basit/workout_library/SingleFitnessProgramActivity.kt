package com.basit.workout_library

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.basit.workout_library.databinding.ActivitySingleFitnessProgramBinding
import com.basit.workout_library.models.FitnessProgram
import com.basit.workout_library.utils.WorkoutLibraryHelper
import com.google.android.gms.ads.MobileAds

internal class SingleFitnessProgramActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySingleFitnessProgramBinding
    private lateinit var navController: NavController
    private var mAddSkdInit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySingleFitnessProgramBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.nav_container)
        setNavGraph()

        if (intent.getBooleanExtra("enableAdds", false)) {
            MobileAds.initialize(applicationContext) { mAddSkdInit = true }
        }
    }

    private fun setNavGraph() {
        val fitnessProgram = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("fitnessProgram", FitnessProgram::class.java)
        } else {
            intent.getParcelableExtra("fitnessProgram")
        }

        val startDestinationArgs = Bundle().apply {
            putInt("dayIndex", intent.getIntExtra("dayIndex", 0))
            putParcelable("fitnessProgram", fitnessProgram)
        }
        navController.setGraph(R.navigation.workout_library_program_navigation, startDestinationArgs)
    }

    fun getBannerAdUnitId():String? {
        return intent.getStringExtra("bannerUnitId")
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
        //val window = this.window
        //window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        //window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = color

        if (lightStatusBar) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                window.insetsController?.setSystemBarsAppearance(
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
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
            } else {
                @Suppress("DEPRECATION")
                window.decorView.systemUiVisibility =
                    View.STATUS_BAR_VISIBLE
            }
        }
    }

}