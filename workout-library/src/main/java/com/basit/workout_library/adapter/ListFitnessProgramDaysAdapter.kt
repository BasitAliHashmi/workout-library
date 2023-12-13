package com.basit.workout_library.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.basit.workout_library.R
import com.basit.workout_library.models.FitnessProgram
import com.basit.workout_library.utils.OnFitnessProgramClick
import com.google.android.material.card.MaterialCardView
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
            holder.iconRest.visibility = View.GONE
        } else {
            holder.lblTime.text = "Rest Day"
            holder.iconRest.visibility = View.VISIBLE
        }

        holder.mainCard.setOnClickListener {
            onClick.onItemClick(fitnessProgram, position)
        }
    }
}