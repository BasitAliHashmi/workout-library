package com.basit.workout_library.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.basit.workout_library.R
import com.basit.workout_library.models.FitnessProgram
import com.basit.workout_library.utils.OnFitnessProgramClick
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView

internal class ListMiniFitnessProgramsAdapter(private val fitnessPrograms: List<FitnessProgram>,
                                              private val mContext: Context,
                                              private val onClick: OnFitnessProgramClick
): RecyclerView.Adapter<ListMiniFitnessProgramsAdapter.ViewHolder>() {

    internal class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mainCard: MaterialCardView = itemView.findViewById(R.id.main_card)
        val lblTitle: TextView = itemView.findViewById(R.id.lbl_title)
        val fitnessIcon: ImageView = itemView.findViewById(R.id.img_icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.single_mini_fitness_program_layout, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return fitnessPrograms.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = fitnessPrograms[position]

        holder.lblTitle.text = currentItem.title
        Glide.with(mContext)
            .load(currentItem.iconResourceId)
            .placeholder(ColorDrawable(Color.CYAN))
            .error(ColorDrawable(Color.CYAN))
            .into(holder.fitnessIcon)

        holder.mainCard.setCardBackgroundColor(Color.WHITE)

        holder.mainCard.setOnClickListener {
            onClick.onItemClick(currentItem, 0)
        }
    }

}