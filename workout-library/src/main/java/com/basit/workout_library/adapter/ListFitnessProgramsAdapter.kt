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
import com.basit.workout_library.models.MiniWorkoutCardOptions
import com.basit.workout_library.utils.EmptySpaceAtBottomDecorator
import com.basit.workout_library.utils.OnFitnessProgramClick
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView

internal class ListFitnessProgramsAdapter(private val items:List<FitnessProgram>,
                                          private val miniFitnessPrograms:List<FitnessProgram>,
                                          private val mContext: Context,
                                          private val mMiniWorkoutCardOptions: MiniWorkoutCardOptions,
                                          private val onClick: OnFitnessProgramClick
):RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val mSimpleFitnessProgramViewType = 0
    private val mMiniFitnessProgramsViewType = 1

    internal class SimpleFitnessProgramViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mainCard: MaterialCardView = itemView.findViewById(R.id.main_card)
        val iconBeginner: ImageView = itemView.findViewById(R.id.icon_beginner)
        val iconIntermediate: ImageView = itemView.findViewById(R.id.icon_intermediate)
        val iconAdvance: ImageView = itemView.findViewById(R.id.icon_advance)
        val lblDifficulty: TextView = itemView.findViewById(R.id.lbl_difficulty)
        val lblTitle: TextView = itemView.findViewById(R.id.lbl_title)
        val fitnessIcon: ImageView = itemView.findViewById(R.id.img_icon)
        val daysRecyclerView: RecyclerView = itemView.findViewById(R.id.rv_view)
    }

    internal class MiniFitnessProgramsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mainCard: MaterialCardView = itemView.findViewById(R.id.main_card)
        val lblTitle: TextView = itemView.findViewById(R.id.lbl_title)
        val programsRecyclerView: RecyclerView = itemView.findViewById(R.id.rv_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            mSimpleFitnessProgramViewType -> SimpleFitnessProgramViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.single_fitness_program_item, parent, false)
            )

            mMiniFitnessProgramsViewType -> MiniFitnessProgramsViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.mini_fitness_programs_list, parent, false)
            )

            else -> {
                SimpleFitnessProgramViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.single_fitness_program_item, parent, false)
                )

            }
        }
    }

    override fun getItemViewType(position: Int): Int {

        return if (position < items.size)
            mSimpleFitnessProgramViewType
        else
            mMiniFitnessProgramsViewType
    }

    override fun getItemCount(): Int {

        return if (miniFitnessPrograms.isNotEmpty())
            items.size + 1
        else
            items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SimpleFitnessProgramViewHolder -> {

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
                holder.daysRecyclerView.addItemDecoration(EmptySpaceAtBottomDecorator(120))
            }

            is MiniFitnessProgramsViewHolder -> {

                holder.mainCard.setCardBackgroundColor(mMiniWorkoutCardOptions.cardBackgroundColor)
                holder.lblTitle.text = mMiniWorkoutCardOptions.cardTitle
                if (mMiniWorkoutCardOptions.cardTitleVisibility)
                    holder.lblTitle.visibility = View.VISIBLE
                else
                    holder.lblTitle.visibility = View.GONE

                //bind programs
                val adapter = ListMiniFitnessProgramsAdapter(miniFitnessPrograms, mContext, onClick)
                holder.programsRecyclerView.layoutManager =
                    LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
                holder.programsRecyclerView.adapter = adapter
                holder.programsRecyclerView.addItemDecoration(EmptySpaceAtBottomDecorator(120))
            }
        }
    }

}