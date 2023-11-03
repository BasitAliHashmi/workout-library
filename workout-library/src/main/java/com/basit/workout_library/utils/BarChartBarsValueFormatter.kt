package com.basit.workout_library.utils

import com.github.mikephil.charting.formatter.ValueFormatter

internal class BarChartBarsValueFormatter: ValueFormatter() {

    override fun getFormattedValue(value: Float): String {
        return if (value <= 0)
            ""
        else
            "${super.getFormattedValue(value)}m"
    }
}