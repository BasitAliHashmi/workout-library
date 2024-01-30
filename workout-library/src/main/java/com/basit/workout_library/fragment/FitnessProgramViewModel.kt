package com.basit.workout_library.fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.basit.workout_library.core.domain.WorkoutHistoryRepository
import com.basit.workout_library.models.TimerTickState
import com.basit.workout_library.core.domain.model.WorkoutHistory
import com.basit.workout_library.core.domain.model.WorkoutProgressByDays
import com.basit.workout_library.core.domain.model.WorkoutSummary
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

internal class FitnessProgramViewModel(private val workoutHistoryRepository: WorkoutHistoryRepository): ViewModel() {

    val tick = MutableLiveData<TimerTickState>()
    val summaryReport = MutableLiveData<List<WorkoutSummary>>()
    val singleDaySummaryReport = MutableLiveData<WorkoutSummary>()
    val progressByDays = MutableLiveData<List<WorkoutProgressByDays>>()

    fun startTimer(seconds: Int) = viewModelScope.launch {
        if (seconds > 0) {
            val delayMillis = 100L
            var secondsLeft = seconds
            var millisMeasurementForSecond = 0L

            while (secondsLeft > 0 && isActive) {
                delay(delayMillis)
                millisMeasurementForSecond += delayMillis
                tick.postValue(TimerTickState.Millis)

                if (millisMeasurementForSecond % 1000 == 0L) {
                    tick.postValue(TimerTickState.Seconds)
                    secondsLeft -= 1
                    millisMeasurementForSecond = 0L
                }
            }
        } else {

            while (isActive) {
                delay(1000)
                tick.postValue(TimerTickState.Seconds)
            }
        }
    }

    fun addHistory(model: WorkoutHistory) = viewModelScope.launch {
        workoutHistoryRepository.insert(model)
    }

    fun getSummarizedReport(from: LocalDateTime, to: LocalDateTime, programId: Int) =
        viewModelScope.launch {
            val data = workoutHistoryRepository.getSummarizedReport(from, to, programId)
            summaryReport.postValue(createEmptyDays(data, from, to))
        }

    fun getSummarizedReport(dateFor: LocalDateTime, programId: Int, dayNo: Int) =
        viewModelScope.launch {
            val data = workoutHistoryRepository.getSummarizedReport(dateFor, programId, dayNo)
            singleDaySummaryReport.postValue(data)
        }

    private fun createEmptyDays(
        summaryList: List<WorkoutSummary>,
        dateFrom: LocalDateTime,
        dateTo: LocalDateTime
    ): List<WorkoutSummary> {
        val list = arrayListOf<WorkoutSummary>()

        val daysBetween = ChronoUnit.DAYS.between(dateFrom, dateTo)

        for (i in 0..daysBetween) {
            val newDate = dateFrom.toLocalDate().plusDays(i)
            var seconds = 0
            summaryList.forEach {
                val date = LocalDate.parse(
                    it.WorkoutDate,
                    DateTimeFormatter.ofPattern("uuuu-MM-dd")
                )

                if (newDate.isEqual(date)) {
                    seconds = it.TotalSeconds
                    return@forEach
                }
            }

            list.add(
                WorkoutSummary(
                    newDate.format(DateTimeFormatter.ofPattern("uuuu-MM-dd")),
                    seconds
                )
            )
        }

        return list
    }

    fun loadWorkoutProgress() = viewModelScope.launch {
        progressByDays.value = workoutHistoryRepository.getProgressByDays()
    }

}