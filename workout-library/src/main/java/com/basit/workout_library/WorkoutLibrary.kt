package com.basit.workout_library

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import android.speech.tts.TextToSpeech
import com.basit.workout_library.core.data.AppDatabase
import com.basit.workout_library.core.domain.WorkoutHistoryRepository
import com.basit.workout_library.core.domain.workouthistory.WorkoutHistoryLocalDataSource
import java.util.Locale

class WorkoutLibrary private constructor(context: Context) {

    private val database by lazy { AppDatabase.getInstance(context) }

    internal val workoutHistoryRepository by lazy {
        WorkoutHistoryRepository(
            localDataSource = WorkoutHistoryLocalDataSource(
                database.workoutHistoryDao()
            )
        )
    }

    private lateinit var mSoundPool: SoundPool
    private var mTextToSpeech: TextToSpeech? = null

    private var mTtsInit = false
    private var mSoundEffectTicking: Int = 0
    private var mSoundEffectTickingStreamId: Int = 0

    companion object {

        var soundEffectWhistle: Int = 0
        var soundEffectWorkoutFinish: Int = 0

        @Volatile
        private var INSTANCE: WorkoutLibrary? = null

        fun initialize(context: Context): WorkoutLibrary =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: WorkoutLibrary(context).also {
                    INSTANCE = it

                    INSTANCE!!.initSoundPool(context)
                    INSTANCE!!.initTextToSpeech(context)
                }
            }

        internal fun getInstance(): WorkoutLibrary = INSTANCE!!
    }

    //region TTS

    fun initTextToSpeech(context: Context) {
        if (!mTtsInit) {
            mTextToSpeech = TextToSpeech(
                context
            ) { status ->
                if (status != TextToSpeech.ERROR) {
                    mTtsInit = true
                    mTextToSpeech?.language = Locale.US
                }
            }
        }
    }

    fun stopTts() {
        if (mTtsInit) {
            mTtsInit = false
            mTextToSpeech?.stop()
            mTextToSpeech?.shutdown()
        }
    }

    fun speech(text:String) {
        if (mTtsInit)
            mTextToSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    //endregion

    //region SoundPool

    private fun initSoundPool(context: Context) {
        val audioAttribute = AudioAttributes.Builder()
        audioAttribute.setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
        audioAttribute.setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)

        val builder = SoundPool.Builder()
        builder.setMaxStreams(3)
        builder.setAudioAttributes(audioAttribute.build())

        mSoundPool = builder.build()

        soundEffectWhistle = mSoundPool.load(context, R.raw.referee_whistle_blow, 1)
        soundEffectWorkoutFinish = mSoundPool.load(context, R.raw.workout_complete, 1)
        mSoundEffectTicking = mSoundPool.load(context, R.raw.ticking, 1)
    }

    fun soundEffect(soundEffectId: Int) {
        mSoundPool.play(
            soundEffectId,
            1.0F,
            1.0F,
            0,
            0,
            1.0F
        )
    }

    fun startTickingSoundEffect() {
        mSoundEffectTickingStreamId = mSoundPool.play(
            mSoundEffectTicking,
            1.0F,
            1.0F,
            0,
            -1,
            1.0F
        )
    }

    fun stopTickingSoundEffect() {
        mSoundPool.stop(mSoundEffectTickingStreamId)
    }

    fun pauseAllSoundEffects() {
        mSoundPool.autoPause()
    }

    fun resumeAllSoundEffects() {
        mSoundPool.autoResume()
    }

    //endregion


}