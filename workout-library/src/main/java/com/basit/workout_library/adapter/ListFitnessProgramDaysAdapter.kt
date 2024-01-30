package com.basit.workout_library.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.basit.workout_library.R
import com.basit.workout_library.models.FitnessProgram
import com.basit.workout_library.utils.OnFitnessProgramClick
import com.google.android.material.card.MaterialCardView
import com.google.android.material.progressindicator.CircularProgressIndicator
import kotlin.math.ceil

internal class ListFitnessProgramDaysAdapter(private val fitnessProgram: FitnessProgram,
                                    private val mContext: Context,
                                    private val onClick: OnFitnessProgramClick
):RecyclerView.Adapter<ListFitnessProgramDaysAdapter.ViewHolder>() {

    internal class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mainCard: MaterialCardView = itemView.findViewById(R.id.main_card)
        val lblTitle: TextView = itemView.findViewById(R.id.lbl_title)
        val lblTime: TextView = itemView.findViewById(R.id.lbl_time)
        val iconRest: ImageView = itemView.findViewById(R.id.icon_rest)
        val containerProgressIndicator: FrameLayout =
            itemView.findViewById(R.id.container_progress_indicator)
        val lblDayProgress: TextView = itemView.findViewById(R.id.lbl_day_progress)
        val imgFinishedDay: ImageView = itemView.findViewById(R.id.img_finished_day)
        val dayProgressIndicator: CircularProgressIndicator =
            itemView.findViewById(R.id.day_progress)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.single_fitness_program_day_layout, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return fitnessProgram.days.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = fitnessProgram.days[position]

        holder.lblTitle.text = "Day ${position + 1}"

        if (!currentItem.restDay) {

            val convertedMinutes = ceil(currentItem.totalSeconds().toDouble() / 60.0)
            holder.lblTime.text = "${String.format("%.0f", convertedMinutes)} minutes"

            if (currentItem.progress > 99) {
                updateGui(holder, DaysListState.Completed)
                holder.dayProgressIndicator.progress = 100
            } else if (currentItem.progress > 0) {
                updateGui(holder, DaysListState.InProgress)
                holder.dayProgressIndicator.progress = currentItem.progress
                holder.lblDayProgress.text = "${currentItem.progress}%"
            } else {
                updateGui(holder, DaysListState.Pending)
            }

        } else {
            updateGui(holder, DaysListState.RestDay)
        }

        holder.mainCard.setOnClickListener {
            onClick.onItemClick(fitnessProgram, position)
        }
    }

    private fun updateGui(holder: ViewHolder, state: DaysListState) {

        holder.iconRest.visibility = View.GONE
        holder.containerProgressIndicator.visibility = View.GONE
        holder.imgFinishedDay.visibility = View.GONE
        holder.lblDayProgress.visibility = View.GONE
        holder.dayProgressIndicator.setIndicatorColor(fitnessProgram.color)
        holder.imgFinishedDay.setColorFilter(fitnessProgram.color)

        when (state) {
            DaysListState.RestDay -> {
                holder.lblTime.text = "Rest Day"
                holder.iconRest.visibility = View.VISIBLE
            }

            DaysListState.Pending -> {

            }

            DaysListState.InProgress -> {
                holder.containerProgressIndicator.visibility = View.VISIBLE
                holder.lblDayProgress.visibility = View.VISIBLE
            }

            DaysListState.Completed -> {
                holder.containerProgressIndicator.visibility = View.VISIBLE
                holder.imgFinishedDay.visibility = View.VISIBLE
            }
        }
    }

}