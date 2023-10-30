package com.basit.workout_library.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.basit.workout_library.R
import com.basit.workout_library.models.FitnessProgram
import com.basit.workout_library.models.FitnessProgramDifficulty
import com.basit.workout_library.utils.OnFitnessProgramClick
import com.basit.workout_library.utils.WorkoutLibraryHelper
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView

internal class ListFitnessProgramsAdapter(private val items:List<FitnessProgram>,
                                 private val mContext: Context,
                                 private val onClick: OnFitnessProgramClick
):RecyclerView.Adapter<ListFitnessProgramsAdapter.ViewHolder>() {

    //private var colorPalette:IntArray = WorkoutLibraryHelper.getFitnessProgramsColorPalette(mContext)

    internal class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mainCard: MaterialCardView = itemView.findViewById(R.id.main_card)
        val iconBeginner: ImageView = itemView.findViewById(R.id.icon_beginner)
        val iconIntermediate: ImageView = itemView.findViewById(R.id.icon_intermediate)
        val iconAdvance: ImageView = itemView.findViewById(R.id.icon_advance)
        val lblDifficulty: TextView = itemView.findViewById(R.id.lbl_difficulty)
        val lblTitle: TextView = itemView.findViewById(R.id.lbl_title)
        val fitnessIcon: ImageView = itemView.findViewById(R.id.img_icon)
        val daysRecyclerView: RecyclerView = itemView.findViewById(R.id.rv_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.single_fitness_program_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = items[position]

        val alphaFull = 1.0f
        val alphaDim = 0.4f

        holder.iconBeginner.alpha = alphaDim
        holder.iconIntermediate.alpha = alphaDim
        holder.iconAdvance.alpha = alphaDim

        when (currentItem.difficulty) {
            FitnessProgramDifficulty.Beginner -> {
                holder.iconBeginner.alpha = alphaFull
            }

            FitnessProgramDifficulty.Intermediate -> {
                holder.iconBeginner.alpha = alphaFull
                holder.iconIntermediate.alpha = alphaFull
            }

            FitnessProgramDifficulty.Advance -> {
                holder.iconBeginner.alpha = alphaFull
                holder.iconIntermediate.alpha = alphaFull
                holder.iconAdvance.alpha = alphaFull
            }
        }

        holder.lblDifficulty.text = currentItem.difficulty.name
        holder.lblTitle.text = currentItem.title
        Glide.with(mContext)
            .load(currentItem.iconResourceId)
            .placeholder(ColorDrawable(Color.CYAN))
            .error(ColorDrawable(Color.CYAN))
            .into(holder.fitnessIcon)

        holder.mainCard.setCardBackgroundColor(currentItem.color)

        //bind days
        val adapter = ListFitnessProgramDaysAdapter(currentItem, mContext, onClick)
        holder.daysRecyclerView.layoutManager =
            LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        holder.daysRecyclerView.adapter = adapter
    }

}