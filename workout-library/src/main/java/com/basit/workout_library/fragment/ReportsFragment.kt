package com.basit.workout_library.fragment

import android.graphics.DashPathEffect
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import com.basit.workout_library.R
import com.basit.workout_library.WorkoutLibrary
import com.basit.workout_library.base.BaseWorkoutFrag
import com.basit.workout_library.core.domain.model.WorkoutSummary
import com.basit.workout_library.databinding.FragmentReportsBinding
import com.basit.workout_library.models.FitnessProgram
import com.basit.workout_library.utils.BarChartBarsValueFormatter
import com.basit.workout_library.utils.BarChartDateFormatter
import com.basit.workout_library.utils.WorkoutLibraryHelper
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.google.android.material.color.MaterialColors
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

private const val ARG_FITNESS_PROGRAMS = "param_fitness_programs"
class ReportsFragment : BaseWorkoutFrag() {

    private var paramFitnessPrograms: List<FitnessProgram>? = null

    private lateinit var binding: FragmentReportsBinding
    private lateinit var viewModel: FitnessProgramViewModel

    private lateinit var currentMonth: LocalDate
    private lateinit var dateFrom: LocalDateTime
    private lateinit var dateTo: LocalDateTime

    private var dayNameLabel = arrayListOf<String>()

    private var currentProgramId = 0
    private var maxProgramId = 0
    private var monthlyReportMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            paramFitnessPrograms = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelableArrayList(ARG_FITNESS_PROGRAMS, FitnessProgram::class.java)
            } else {
                it.getParcelableArrayList(ARG_FITNESS_PROGRAMS)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReportsBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(
            this,
            FitnessProgramViewModelFactory(WorkoutLibrary.getInstance().workoutHistoryRepository)
        )[FitnessProgramViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        maxProgramId = paramFitnessPrograms!!.size
        currentMonth = LocalDate.now().withDayOfMonth(1)
        updateDates()
        setOnClickListener()
        loadData()
        viewModel.summaryReport.observe(viewLifecycleOwner) {
            bindData(it)
        }

        if (!WorkoutLibraryHelper.ReportsProgramSelectorShown) {
            WorkoutLibraryHelper.ReportsProgramSelectorShown = true
            showProgramSelector()
        }
    }

    private fun loadData() {
        viewModel.getSummarizedReport(dateFrom, dateTo, currentProgramId)
    }

    private fun configureBarChart() {

        with(binding) {
            barChart.setScaleEnabled(false)
            barChart.setPinchZoom(false)
            barChart.setDrawBarShadow(false)
            barChart.setDrawGridBackground(false)
            barChart.isAutoScaleMinMaxEnabled = true
            barChart.description.isEnabled = false
            barChart.animateY(1000, Easing.EaseInOutCubic)

            val xAxis = barChart.xAxis
            xAxis.isGranularityEnabled = false
            xAxis.setDrawGridLines(false)
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.isGranularityEnabled = false
            xAxis.typeface = ResourcesCompat.getFont(requireContext(), R.font.roboto_medium)
            xAxis.textColor = MaterialColors.getColor(context, com.google.android.material.R.attr.colorOnSurface, "Error: Primary color not found")


            val leftAxis = barChart.axisLeft
            leftAxis.setDrawGridLines(true)
            leftAxis.setDrawZeroLine(false)
            leftAxis.setDrawAxisLine(false)
            leftAxis.setGridDashedLine(DashPathEffect(floatArrayOf(6f, 6f), 20f))
            leftAxis.setDrawTopYLabelEntry(true)
            leftAxis.axisMinimum = 0f
            leftAxis.typeface = ResourcesCompat.getFont(requireContext(), R.font.roboto_medium)
            leftAxis.textColor = MaterialColors.getColor(context, com.google.android.material.R.attr.colorOnSurface, "Error: Primary color not found")

            barChart.axisRight.isEnabled = false
            barChart.legend.isEnabled = false
            barChart.setNoDataText("No data available")
            barChart.setNoDataTextColor(MaterialColors.getColor(context, com.google.android.material.R.attr.colorOnSurface, "Error: Primary color not found"))
        }
    }

    private fun getDayNameByDate(date:LocalDate):String {
        return if (monthlyReportMode) {
            date.format(DateTimeFormatter.ofPattern("dd"))
        } else {
            val dayName = date.dayOfWeek.name.substring(0, 3).lowercase()
            val firstChar = dayName.first()
            dayName.replaceFirst(firstChar, firstChar.uppercaseChar(), true)
        }
    }

    private fun bindData(workoutSummaryList:List<WorkoutSummary>) {

        if (currentProgramId == 0) {
            binding.lblBarChartTitle.text = "Aggregate Progress"
        } else {
            paramFitnessPrograms!!.forEach { program ->
                if (currentProgramId == program.id) {
                    binding.lblBarChartTitle.text = "${program.title} Progress"
                }
            }
        }

        if (monthlyReportMode) {

            binding.monthSelector.visibility = View.VISIBLE
            binding.lblBarChartDesc.text = currentMonth.format(DateTimeFormatter.ofPattern("MMM-uuuu"))
            binding.lblCurrentMonth.text = currentMonth.format(DateTimeFormatter.ofPattern("MMM-uuuu"))
        } else {

            binding.monthSelector.visibility = View.GONE
            binding.lblBarChartDesc.text = "last 7 days"
        }

        val values = ArrayList<BarEntry>()

        dayNameLabel.clear()
        workoutSummaryList.forEachIndexed { index, singleSummary ->

            val dayName = getDayNameByDate(
                LocalDate.parse(
                    singleSummary.WorkoutDate,
                    DateTimeFormatter.ofPattern("uuuu-MM-dd")
                )
            )
            dayNameLabel.add(dayName)

            values.add(
                BarEntry(
                    index.toFloat(),
                    ((singleSummary.TotalSeconds.toFloat() / 60.0F) * 100.0F).roundToInt() / 100.0F
                )
            )
        }

        val set1: BarDataSet
        if (binding.barChart.data != null &&
            binding.barChart.data.dataSetCount > 0
        ) {
            set1 = binding.barChart.data.dataSets[0] as BarDataSet
            set1.values = values
            configureBarChart()
            binding.barChart.data.notifyDataChanged()
            binding.barChart.notifyDataSetChanged()
            binding.barChart.setVisibleXRange(7f, 7f)
            binding.barChart.invalidate()

            binding.barChart.moveViewToAnimated(99f,0f, YAxis.AxisDependency.LEFT,2000L)

        } else {
            set1 = BarDataSet(values, "")
            set1.setDrawIcons(false)
            set1.isHighlightEnabled = false
            set1.valueTextSize = 10f
            set1.valueTypeface = ResourcesCompat.getFont(requireContext(), R.font.roboto_medium)
            set1.valueFormatter = BarChartBarsValueFormatter()
            set1.valueTextColor = MaterialColors.getColor(context, com.google.android.material.R.attr.colorOnSurface, "Error: Primary color not found")

            set1.setColors(
                MaterialColors.getColor(context, com.google.android.material.R.attr.colorPrimary, "Error: Primary color not found")
            )

            configureBarChart()

            val dataSets = ArrayList<IBarDataSet>()
            dataSets.add(set1)
            val data = BarData(dataSets)
            //data.barWidth = 0.9f

            binding.barChart.data = data
            binding.barChart.xAxis.valueFormatter = BarChartDateFormatter(dayNameLabel)
            binding.barChart.invalidate()
        }
    }

    private fun setOnClickListener() {

        binding.btnNextMonth.setOnClickListener {
            currentMonth = currentMonth.plusMonths(1)
            updateDates()
            loadData()
        }
        binding.btnPrevMonth.setOnClickListener {
            currentMonth = currentMonth.minusMonths(1)
            updateDates()
            loadData()
        }

        binding.btnFilter.setOnClickListener {
            showProgramSelector()
        }

        binding.switchMonthlyReports.setOnCheckedChangeListener { _, isChecked ->
            monthlyReportMode = isChecked
            updateDates()
            loadData()
        }
    }

    private fun updateDates() {

        if (monthlyReportMode) {
            dateFrom = LocalDateTime.of(currentMonth.year, currentMonth.month, 1, 0, 0, 0)
            dateTo = if (dateFrom.month == LocalDateTime.now().month) {
                binding.btnNextMonth.alpha = 0.3f
                binding.btnNextMonth.isEnabled = false

                LocalDateTime.now()
            } else {

                binding.btnNextMonth.alpha = 1f
                binding.btnNextMonth.isEnabled = true

                dateFrom.plusMonths(1L).minusDays(1)
            }

        } else {
            dateFrom = LocalDateTime.of(LocalDate.now().minusDays(6), LocalTime.of(0, 0, 0))
            dateTo = LocalDateTime.now()
        }
    }

    private fun showProgramSelector() {

        var selectedItemIndex: Int = -1

        val programTitleArray = arrayOfNulls<String?>(paramFitnessPrograms!!.size + 1)
        programTitleArray.forEachIndexed { index, s ->
            if (index == 0) {
                programTitleArray[0] = "Aggregate"

                if (currentProgramId == 0)
                    selectedItemIndex = 0

            } else {
                val item = paramFitnessPrograms!![index - 1]

                val tempTitle = item.title
                programTitleArray[index] = tempTitle
                if (currentProgramId == item.id)
                    selectedItemIndex = index
            }
        }

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Please select program")
            .setCancelable(true)
            .setSingleChoiceItems(programTitleArray, selectedItemIndex) { dialog, which ->

                currentProgramId = if (which == 0) {
                    0
                } else {
                    paramFitnessPrograms!![which - 1].id
                }

                dialog.dismiss()
                loadData()
            }
            .show()
    }

    companion object {

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param fitnessPrograms Parameter 1.
         * @return A new instance of fragment BlankFragment.
         */
        @JvmStatic
        fun newInstance(
            vararg fitnessPrograms: FitnessProgram
        ) =
            ReportsFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(
                        ARG_FITNESS_PROGRAMS,
                        fitnessPrograms.toCollection(java.util.ArrayList())
                    )
                }
            }
    }

}