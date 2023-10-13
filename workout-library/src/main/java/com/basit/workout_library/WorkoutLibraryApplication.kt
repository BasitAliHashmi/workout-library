package com.basit.workout_library

import android.app.Application
import android.media.AudioAttributes
import android.media.SoundPool

abstract class WorkoutLibraryApplication : Application() {

    companion object {

        /*var appInterstitialAdd:InterstitialAd? = null
        var appInterstitialAdLoadStatus = InterstitialAdLoadStatus.None

        var firebaseAnalytics: FirebaseAnalytics? = null*/

        private lateinit var soundPool: SoundPool
        private var soundEffectTicking: Int = 0
        private var soundEffectTickingStreamId: Int = 0

        var soundEffectWhistle: Int = 0
        var soundEffectWorkoutFinish: Int = 0


        fun soundEffect(soundEffectId: Int) {
            soundPool.play(
                soundEffectId,
                1.0F,
                1.0F,
                0,
                0,
                1.0F
            )
        }

        fun startTickingSoundEffect() {
            soundEffectTickingStreamId = soundPool.play(
                soundEffectTicking,
                1.0F,
                1.0F,
                0,
                -1,
                1.0F
            )
        }

        fun stopTickingSoundEffect() {
            soundPool.stop(soundEffectTickingStreamId)
        }

        fun pauseAllSoundEffects() {
            soundPool.autoPause()
        }

        fun resumeAllSoundEffects() {
            soundPool.autoResume()
        }
    }

    override fun onCreate() {
        super.onCreate()

        /*Helper.SHOW_ADDS =
            UserPreferences(this).getValueBoolean(UserPreferences.KEY_SHOW_ADS_BOOL, true)
        Helper.INTERSTITIAL_REPEAT_DELAY_SECS =
            UserPreferences(this).getValueInt(UserPreferences.KEY_INTERSTITIAL_REPEAT_DELAY_INT, 60)
        Helper.INTERSTITIAL_AFTER_EXIT_ACTIVITY = UserPreferences(this).getValueBoolean(
            UserPreferences.KEY_SHOW_INTERSTITIAL_AFTER_EXIT_BOOL,
            true
        )

        if (Helper.enableFirebaseAnalytics) {
            firebaseAnalytics = FirebaseAnalytics.getInstance(this)
            firebaseAnalytics!!.setAnalyticsCollectionEnabled(true)
        }

        if (Helper.enableFirebaseCloudMessaging) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (NotificationManagerCompat.from(this).areNotificationsEnabled()) {
                    Firebase.messaging.isAutoInitEnabled = true
                }
            } else {
                Firebase.messaging.isAutoInitEnabled = true
            }
        }

        if (BuildConfig.DEBUG)
            Helper.SHOW_ADDS = false*/

        initializeSoundPool()
    }

    private fun initializeSoundPool() {
        val audioAttribute = AudioAttributes.Builder()
        audioAttribute.setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
        audioAttribute.setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)

        val builder = SoundPool.Builder()
        builder.setMaxStreams(3)
        builder.setAudioAttributes(audioAttribute.build())

        soundPool = builder.build()

        soundEffectWhistle = soundPool.load(this, R.raw.referee_whistle_blow, 1)
        soundEffectWorkoutFinish = soundPool.load(this, R.raw.workout_complete, 1)
        soundEffectTicking = soundPool.load(this, R.raw.ticking, 1)
    }
}