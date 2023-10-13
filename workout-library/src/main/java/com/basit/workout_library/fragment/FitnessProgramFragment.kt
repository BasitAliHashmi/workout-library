package com.basit.workout_library.fragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
//import androidx.navigation.fragment.findNavController
//import androidx.navigation.fragment.navArgs
import com.basit.workout_library.R
import com.basit.workout_library.SingleFitnessProgramActivity
import com.basit.workout_library.WorkoutLibraryApplication
import com.basit.workout_library.base.WorkoutLibBaseFragment
import com.basit.workout_library.databinding.FragmentFitnessProgramBinding
import com.basit.workout_library.models.FitnessProgram
import com.basit.workout_library.models.FitnessProgramActivityState
import com.basit.workout_library.models.FitnessTimerType
import com.basit.workout_library.models.TimerTickState
import com.basit.workout_library.models.Workout
import com.basit.workout_library.models.WorkoutHistory
import com.basit.workout_library.models.WorkoutType
import com.basit.workout_library.utils.WorkoutLibraryHelper
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.target.ImageViewTarget
import kotlinx.coroutines.Job
import java.time.LocalDateTime
import java.util.Locale

private const val ARG_DAY_INDEX = "param1"
private const val ARG_COLOR = "param2"
private const val ARG_FITNESS_PROGRAMS = "param3"

internal class FitnessProgramFragment : WorkoutLibBaseFragment() {

    private var paramFitnessPrograms: FitnessProgram? = null
    private var paramColor: Int? = null
    private var paramDayIndex: Int? = null

    private lateinit var binding: FragmentFitnessProgramBinding
    private lateinit var viewModel: FitnessProgramViewModel
    //private val args:FitnessProgramFragmentArgs by navArgs()

    private lateinit var workouts:List<Workout>
    private var dayIndex = -1
    private lateinit var mTimerJob: Job
    private var mCurrentState = FitnessProgramActivityState.None
    private var mLastState = FitnessProgramActivityState.None

    private var mTextToSpeech: TextToSpeech? = null

    private var currentWorkoutPosition = 0
    private var currentTimerMaxSeconds = 0
    private var currentTimerSeconds = 0
    private var mTimerType = FitnessTimerType.Definite
    private var textToSpeech = ""
    private var textToSpeechInit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            paramDayIndex = it.getInt(ARG_DAY_INDEX)
            paramColor = it.getInt(ARG_COLOR)
            paramFitnessPrograms = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelable(ARG_FITNESS_PROGRAMS, FitnessProgram::class.java)
            } else {
                it.getParcelable(ARG_FITNESS_PROGRAMS)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFitnessProgramBinding.inflate(inflater, container, false)
        /*viewModel = ViewModelProvider(
            this,
            FitnessProgramViewModelFactory((requireActivity().application as FatLossApp).workoutHistoryRepository)
        )[FitnessProgramViewModel::class.java]*/
        viewModel = ViewModelProvider(this)[FitnessProgramViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.windowPause.visibility = View.GONE
        binding.windowRest.visibility = View.GONE
        updateActivityState(FitnessProgramActivityState.StandBy)

        getData()
        clickListeners()
        setObservers()

        /*logFirebaseScreenViewEvent(
            "Training Mode",
            FitnessProgramFragment::class.java.simpleName
        )*/

        requireActivity().onBackPressedDispatcher.addCallback(requireActivity(), mBackPressCallback)
    }

    private fun workoutLoop() {
        binding.windowPause.visibility = View.GONE
        binding.windowRest.visibility = View.GONE

        when(mCurrentState) {
            FitnessProgramActivityState.StandBy -> {

                val firstItem = workouts[0]

                loadWorkoutImage(true)
                with(binding) {
                    lblReadyToGo.visibility = View.VISIBLE
                    lblWorkoutTitle.text = firstItem.item.title
                    lblWorkoutTimer.visibility = View.GONE
                    panelStandbyProgress.visibility = View.VISIBLE
                    panelWorkoutActions.visibility = View.GONE

                    val workoutUnit = if (firstItem.type == WorkoutType.Timed) {
                        "seconds"
                    } else {
                        "times"
                    }

                    textToSpeech =
                        "Ready to go, The next, ${firstItem.workoutLength}, $workoutUnit, ${firstItem.item.title}"

                    startTimer(10)
                }
            }

            FitnessProgramActivityState.Workout -> {
                val workoutItem = workouts[currentWorkoutPosition]

                loadWorkoutImage(true)
                with(binding) {
                    lblReadyToGo.visibility = View.GONE
                    lblWorkoutTitle.text = workoutItem.item.title
                    lblWorkoutTimer.visibility = View.VISIBLE
                    panelStandbyProgress.visibility = View.GONE
                    panelWorkoutActions.visibility = View.VISIBLE
                    imgPlay.setImageResource(R.drawable.round_pause_24)
                    btnPlay.isEnabled = true

                    speech("Please start ${workoutItem.item.title}")

                    if (workoutItem.type == WorkoutType.Timed) {
                        startTimer(workoutItem.workoutLength)
                    } else {
                        lblWorkoutTimer.text = "${workoutItem.workoutLength}x"
                        imgPlay.setImageResource(R.drawable.round_check_24)
                        startTimer(-1)
                    }


                    if (currentWorkoutPosition == 0) {
                        changeNextPrevButtonState(btnPreviousWorkout,false)
                        changeNextPrevButtonState(btnNextWorkout,true)
                    } else {
                        if (isLastWorkout()) {
                            changeNextPrevButtonState(btnPreviousWorkout, true)
                            changeNextPrevButtonState(btnNextWorkout, false)
                        } else {
                            changeNextPrevButtonState(btnPreviousWorkout, true)
                            changeNextPrevButtonState(btnNextWorkout, true)
                        }
                    }

                    WorkoutLibraryApplication.startTickingSoundEffect()
                }
            }

            FitnessProgramActivityState.Rest -> {

                showBannerAdd(binding.bannerAddView)

                val nextWorkoutIndex = currentWorkoutPosition + 1
                val nextWorkout = workouts[nextWorkoutIndex]

                with(binding) {
                    windowRest.visibility = View.VISIBLE
                    lblTitleNextWorkoutNumber.text = "Next ${nextWorkoutIndex + 1}/${workouts.size}"
                    lblTitleNextWorkout.text = "${nextWorkout.item.title}"

                    when (nextWorkout.type) {
                        WorkoutType.Timed -> {
                            lblTimeNextWorkout.text = "${nextWorkout.workoutLength} sec"
                        }

                        WorkoutType.Frequency -> {
                            lblTimeNextWorkout.text = "${nextWorkout.workoutLength}x"
                        }
                    }
                }

                Glide.with(this)
                    .asGif()
                    .load(nextWorkout.item.animatedImageResourceId)
                    .placeholder(ColorDrawable(Color.WHITE))
                    .error(ColorDrawable(Color.WHITE))
                    .into(object : ImageViewTarget<GifDrawable?>(binding.imageNextWorkout) {
                        override fun setResource(resource: GifDrawable?) {
                            if (resource != null) {
                                binding.imageNextWorkout.setImageDrawable(resource)
                            }
                        }
                    })

                loadWorkoutImage(false)
                speech("Take Rest")
                startTimer(20)
            }

            FitnessProgramActivityState.Finish -> {

                mBackPressCallback.remove()
                (requireActivity() as SingleFitnessProgramActivity).launchFragment("finish")
                /*findNavController().navigate(
                    FitnessProgramFragmentDirections.actionFitnessProgramToFinish(
                        args.dayIndex,
                        args.color,
                        args.fitnessProgram
                    )
                )*/
            }

            FitnessProgramActivityState.Pause -> {
                loadWorkoutImage(false)
                binding.windowPause.visibility = View.VISIBLE
                binding.imgPlay.setImageResource(R.drawable.round_play_arrow_24)

                mTextToSpeech?.stop()
                stopTimer(false)
            }

            else -> {

            }
        }
    }

    private fun changeNextPrevButtonState(btn: ImageView, enable:Boolean) {
        if (enable) {
            btn.visibility = View.VISIBLE
            btn.alpha = 1.0F
            btn.isEnabled = true
        } else {
            btn.visibility = View.VISIBLE
            btn.alpha = 0.5F
            btn.isEnabled = false
        }
    }

    private fun skip(moveForward:Boolean = true) {
        stopTimer(true)
        updateActivityState(FitnessProgramActivityState.Workout)
        if (moveForward)
            nextWorkout()
        else
            prevWorkout()

        workoutLoop()
    }

    private fun finishCurrentWorkout(totalSecondsSpent:Int) {
        stopTimer(true)

        viewModel.addHistory(
            WorkoutHistory(
                0,
                paramFitnessPrograms!!.id,
                paramDayIndex!! + 1,
                workouts[currentWorkoutPosition].item.id,
                totalSecondsSpent,
                LocalDateTime.now()
            )
        )

        val state = if (isLastWorkout())
            FitnessProgramActivityState.Finish
        else
            FitnessProgramActivityState.Rest

        updateActivityState(state)
        WorkoutLibraryApplication.soundEffect(WorkoutLibraryApplication.soundEffectWorkoutFinish)
        workoutLoop()
    }

    private fun nextWorkout() {
        val nextPos = currentWorkoutPosition + 1
        if (nextPos < workouts.size)
            currentWorkoutPosition = nextPos
        else
            updateActivityState(FitnessProgramActivityState.Finish)
    }

    private fun prevWorkout() {
        val prevPos = currentWorkoutPosition - 1
        if (prevPos >= 0)
            currentWorkoutPosition = prevPos
    }

    private fun isLastWorkout():Boolean {
        return currentWorkoutPosition >= (workouts.size - 1)
    }

    private fun startTimer(seconds:Int) {

        if (seconds >= 0) {
            mTimerType = FitnessTimerType.Definite
            if (currentTimerSeconds == 0) {
                currentTimerMaxSeconds = seconds
                currentTimerSeconds = seconds
            }

            when (mCurrentState) {
                FitnessProgramActivityState.StandBy -> {
                    with(binding) {
                        circularProgressBar.max = currentTimerMaxSeconds.times(1000)
                        circularProgressBar.progress =
                            currentTimerMaxSeconds.times(1000) - currentTimerSeconds.times(1000)
                        lblCounter.text = currentTimerSeconds.toString()
                    }
                }

                FitnessProgramActivityState.Workout -> {
                    with(binding) {
                        lblWorkoutTimer.text =
                            WorkoutLibraryHelper.convertSecondsToTime(currentTimerSeconds.toLong())
                    }
                }

                FitnessProgramActivityState.Rest -> {
                    with(binding) {
                        lblRestTimer.text =
                            WorkoutLibraryHelper.convertSecondsToTime(currentTimerSeconds.toLong())
                    }
                }

                else -> {}
            }
            mTimerJob = viewModel.startTimer(currentTimerSeconds)
        } else {
            mTimerType = FitnessTimerType.Indefinite
            mTimerJob = viewModel.startTimer(seconds)
        }
    }

    private fun stopTimer(resetCounter:Boolean) {
        if (!mTimerJob.isCancelled)
            mTimerJob.cancel()

        if (resetCounter) {
            currentTimerMaxSeconds = 0
            currentTimerSeconds = 0
        }

        WorkoutLibraryApplication.stopTickingSoundEffect()
    }

    private fun setObservers() {
        viewModel.tick.observe(viewLifecycleOwner) { tickState ->

            if (tickState != null) {
                when (tickState) {
                    TimerTickState.Millis -> {

                        if (mCurrentState == FitnessProgramActivityState.StandBy) {
                            with(binding) {
                                circularProgressBar.progress += 100
                            }
                            //Log.d(TAG,"m progress: ${binding.circularProgressBar.progress}")
                        }
                    }

                    TimerTickState.Seconds -> {

                        if(mTimerType == FitnessTimerType.Definite)
                            currentTimerSeconds -= 1
                        else
                            currentTimerSeconds += 1

                        //Log.d(TAG,"state: ${mCurrentState.name} seconds: $currentTimerSeconds")

                        when(mCurrentState) {
                            FitnessProgramActivityState.StandBy -> {

                                with(binding) {
                                    circularProgressBar.progress += 100
                                    lblCounter.text = currentTimerSeconds.toString()

                                    if (currentTimerSeconds in 0..3)
                                        speech(currentTimerSeconds.toString())
                                }

                                //Log.d(TAG,"s progress: ${binding.circularProgressBar.progress}")
                                //Log.d(TAG,"remaining seconds= $currentTimerRemainingSeconds")

                                if (currentTimerSeconds == 0) {
                                    stopTimer(true)
                                    Handler(Looper.getMainLooper()).postDelayed({

                                        updateActivityState(FitnessProgramActivityState.Workout)
                                        WorkoutLibraryApplication.soundEffect(WorkoutLibraryApplication.soundEffectWhistle)
                                        workoutLoop()

                                    }, 1000)
                                }
                            }

                            FitnessProgramActivityState.Workout -> {

                                if(mTimerType == FitnessTimerType.Definite) {

                                    with(binding) {
                                        lblWorkoutTimer.text =
                                            WorkoutLibraryHelper.convertSecondsToTime(currentTimerSeconds.toLong())

                                        if (currentTimerSeconds == 10)
                                            speech("10 seconds left")

                                        if (currentTimerSeconds in 0..5)
                                            speech(currentTimerSeconds.toString())
                                    }

                                    //Log.d(TAG,"s progress: ${binding.circularProgressBar.progress}")
                                    //Log.d(TAG,"remaining seconds= $currentTimerRemainingSeconds")

                                    if (currentTimerSeconds == 0) {
                                        Handler(Looper.getMainLooper()).postDelayed({
                                            finishCurrentWorkout(workouts[currentWorkoutPosition].workoutLength)
                                        }, 1000)
                                    }
                                }
                            }

                            FitnessProgramActivityState.Rest -> {

                                with(binding) {
                                    lblRestTimer.text =
                                        WorkoutLibraryHelper.convertSecondsToTime(currentTimerSeconds.toLong())
                                }

                                if (currentTimerSeconds == 0) {
                                    stopTimer(true)
                                    Handler(Looper.getMainLooper()).postDelayed({
                                        updateActivityState(FitnessProgramActivityState.Workout)
                                        nextWorkout()
                                        workoutLoop()

                                    }, 1000)
                                }
                            }

                            else -> {}
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        initTextToSpeech()

        if (mLastState != FitnessProgramActivityState.None)
            updateActivityState(mLastState)

        workoutLoop()
    }

    override fun onPause() {
        updateActivityState(FitnessProgramActivityState.Pause)
        workoutLoop()
        mTextToSpeech?.stop()
        mTextToSpeech?.shutdown()
        super.onPause()
    }

    private fun initTextToSpeech() {
        mTextToSpeech = TextToSpeech(
            requireContext()
        ) { status ->
            if (status != TextToSpeech.ERROR) {
                textToSpeechInit = true
                mTextToSpeech?.language = Locale.US

                if (currentTimerSeconds > 0 && mCurrentState == FitnessProgramActivityState.StandBy) {
                    speech(textToSpeech)
                }
            }
        }
    }

    private fun speech(text:String) {
        if (textToSpeechInit)
            mTextToSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    private fun getData() {
        val fitnessProgram = paramFitnessPrograms!!
        dayIndex = paramDayIndex!!
        val color = paramColor!!

        //(activity as BaseActivityFatLoss).updateStatusBarColor(Color.WHITE,true)
        workouts = fitnessProgram.days[dayIndex].workouts
    }

    private fun clickListeners() {

        with(binding) {
            btnSkipRest.setOnClickListener {
                skip()
            }
            btnIncreaseRestTime.setOnClickListener {
                val increaseTimeLimitSeconds = 300

                val newRestTime = currentTimerSeconds + 20
                stopTimer(false)
                if (newRestTime <= increaseTimeLimitSeconds) {
                    currentTimerSeconds = newRestTime
                    startTimer(currentTimerSeconds)
                } else {
                    currentTimerSeconds = increaseTimeLimitSeconds
                    startTimer(currentTimerSeconds)
                }
            }
            btnBack.setOnClickListener {
                updateActivityState(FitnessProgramActivityState.Pause)
                workoutLoop()
            }
            btnBackWindowPause.setOnClickListener {
                if (mLastState != FitnessProgramActivityState.None)
                    updateActivityState(mLastState)

                workoutLoop()
            }
            btnResume.setOnClickListener {

                if (mLastState != FitnessProgramActivityState.None)
                    updateActivityState(mLastState)

                workoutLoop()
            }
            btnQuit.setOnClickListener {
                requireActivity().finish()
            }
            btnPlay.setOnClickListener {
                btnPlay.isEnabled = false

                val currentWorkout = workouts[currentWorkoutPosition]

                if (currentWorkout.type == WorkoutType.Timed) {
                    if (mCurrentState != FitnessProgramActivityState.Pause) {
                        updateActivityState(FitnessProgramActivityState.Pause)
                        workoutLoop()
                    }
                } else {
                    finishCurrentWorkout(currentTimerSeconds)
                }
            }
            btnNextWorkout.setOnClickListener {
                skip()
            }
            btnPreviousWorkout.setOnClickListener {
                skip(false)
            }
        }
    }

    private fun loadWorkoutImage(animate:Boolean) {

        if (animate) {
            Glide.with(this)
                .asGif()
                .load(workouts[currentWorkoutPosition].item.animatedImageResourceId)
                .placeholder(ColorDrawable(Color.WHITE))
                .error(ColorDrawable(Color.WHITE))
                .into(object : ImageViewTarget<GifDrawable?>(binding.workoutImage) {
                    override fun setResource(resource: GifDrawable?) {
                        if (resource != null) {
                            binding.workoutImage.setImageDrawable(resource)
                        }
                    }
                })
        } else {
            val stillImageId = workouts[currentWorkoutPosition].item.stillImageResourceId
            if (stillImageId > 0) {
                Glide.with(this)
                    .load(stillImageId)
                    .placeholder(ColorDrawable(Color.WHITE))
                    .error(ColorDrawable(Color.WHITE))
                    .into(binding.workoutImage)
            }
        }
    }

    private val mBackPressCallback = object: OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (mCurrentState != FitnessProgramActivityState.Pause) {
                updateActivityState(FitnessProgramActivityState.Pause)
                workoutLoop()
            }
        }
    }

    private fun updateActivityState(state:FitnessProgramActivityState) {
        mLastState = mCurrentState
        mCurrentState = state

        //Log.d(TAG, "state: ${mCurrentState.name} prev: ${mLastState.name}")
    }

    companion object {

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param title Parameter 1.
         * @param fitnessPrograms Parameter 2.
         * @return A new instance of fragment BlankFragment.
         */
        @JvmStatic
        fun newInstance(dayIndex: Int, color: Int,  fitnessPrograms: FitnessProgram) =
            FitnessProgramFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_DAY_INDEX, dayIndex)
                    putInt(ARG_COLOR, color)
                    putParcelable(ARG_FITNESS_PROGRAMS, fitnessPrograms)
                }
            }
    }

}