package com.basit.workout_library.utils

import com.github.mikephil.charting.formatter.ValueFormatter

internal class BarChartDateFormatter(private val dayNames:ArrayList<String>) : ValueFormatter() {

    override fun getFormattedValue(value: Float): String {
        return dayNames.getOrNull(value.toInt()) ?: value.toString()
    }
}