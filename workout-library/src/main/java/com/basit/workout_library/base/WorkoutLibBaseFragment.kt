package com.basit.workout_library.base

import android.content.DialogInterface
import android.os.Build
import android.util.DisplayMetrics
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.material.dialog.MaterialAlertDialogBuilder

abstract class WorkoutLibBaseFragment : Fragment() {

    private var mBannerAdView: AdView? = null
    private var initialLayoutComplete = false

    protected fun showToast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    protected fun showToastLong(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }

    fun showAlertError(title: String?, message: String?, cancelable: Boolean = true) {
        val alertDialog = MaterialAlertDialogBuilder(
            requireContext()).create()
        alertDialog.setCancelable(cancelable)
        alertDialog.setTitle(title)
        alertDialog.setMessage(message)
        alertDialog.setButton(
            AlertDialog.BUTTON_NEGATIVE, "OK"
        ) { dialog: DialogInterface, _: Int -> dialog.dismiss() }
        alertDialog.show()
    }

    protected fun showBannerAdd(containerView: FrameLayout) {
        /*if (Helper.ADD_SDK_INIT) {

            mBannerAdView = AdView(requireContext())
            containerView.addView(mBannerAdView)

            // Since we're loading the banner based on the adContainerView size, we need
            // to wait until this view is laid out before we can get the width.
            containerView.viewTreeObserver?.addOnGlobalLayoutListener {
                if (!initialLayoutComplete) {
                    initialLayoutComplete = true

                    mBannerAdView?.adUnitId = getString(R.string.admobbannerad)
                    mBannerAdView?.setAdSize(calculateAdaptiveBannerAdSize(containerView))

                    val adRequest = AdRequest
                        .Builder()
                        .build()

                    // Start loading the ad in the background.
                    mBannerAdView?.loadAd(adRequest)
                }
            }
        }*/
    }

    private fun calculateAdaptiveBannerAdSize(view: FrameLayout): AdSize {
        val screenWidth: Float = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = requireActivity().windowManager.currentWindowMetrics
            val bounds = windowMetrics.bounds
            bounds.width().toFloat()
        } else {
            val displayMetrics = DisplayMetrics()
            requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
            displayMetrics.widthPixels.toFloat()
        }

        var adWidthPixels = view.width.toFloat()

        // If the ad hasn't been laid out, default to the full screen width.
        if (adWidthPixels == 0f) {
            adWidthPixels = screenWidth
        }

        val density = resources.displayMetrics.density
        val adWidth = (adWidthPixels.div(density)).toInt()

        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(requireContext(), adWidth)
    }

    override fun onPause() {

        if (mBannerAdView != null)
            mBannerAdView?.pause()

        super.onPause()
    }

    override fun onResume() {
        super.onResume()

        if (mBannerAdView != null)
            mBannerAdView?.resume()
    }

    override fun onDestroy() {
        if (mBannerAdView != null)
            mBannerAdView?.destroy()

        super.onDestroy()
    }

}