package com.basit.workout_library.utils

import android.content.res.Resources
import android.util.TypedValue
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2

internal object WorkoutLibraryExtensions {

    fun ViewPager2.showHorizontalPreview(
        offsetDpLeft: Int,
        offsetDpRight: Int,
        marginBtwItems: Int
    ) {
        this.apply {
            clipToPadding = false   // allow full width shown with padding
            clipChildren = false    // allow left/right item is not clipped
            offscreenPageLimit = 2  // make sure left/right item is rendered
        }

        // increase this offset to show more of left/right
        val offsetPxLeft = offsetDpLeft.toPx(this.context.resources)
        val offsetPxRight = offsetDpRight.toPx(this.context.resources)
        this.setPadding(offsetPxLeft, 0, offsetPxRight, 0)

        // increase this offset to increase distance between 2 items
        val pageMarginPx = marginBtwItems.toPx(this.context.resources)
        val marginTransformer = MarginPageTransformer(pageMarginPx)
        this.setPageTransformer(marginTransformer)
    }

    fun Int.toPx(resources: Resources): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this + 0.5f,
            resources.displayMetrics
        ).toInt()
    }

}