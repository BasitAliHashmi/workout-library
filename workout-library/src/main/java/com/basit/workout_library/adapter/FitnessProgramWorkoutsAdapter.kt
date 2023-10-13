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
import com.basit.workout_library.models.Workout
import com.basit.workout_library.models.WorkoutType
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.target.ImageViewTarget

internal class FitnessProgramWorkoutsAdapter(private val items: List<Workout>,
                                    private val mContext: Context
):RecyclerView.Adapter<FitnessProgramWorkoutsAdapter.ViewHolder>() {

    internal class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.lbl_title)
        val time: TextView = itemView.findViewById(R.id.lbl_time)
        val imageView: ImageView = itemView.findViewById(R.id.image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.single_fitness_program_workout_layout, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = items[position]

        holder.title.text = currentItem.item.title
        when(currentItem.type) {
            WorkoutType.Timed -> {
                holder.time.text = "${currentItem.workoutLength} sec"
            }

            WorkoutType.Frequency -> {
                holder.time.text = "${currentItem.workoutLength}x"
            }
        }
        Glide.with(mContext)
            .asGif()
            .load(currentItem.item.animatedImageResourceId)
            .placeholder(ColorDrawable(Color.WHITE))
            .error(ColorDrawable(Color.WHITE))
            .into(object : ImageViewTarget<GifDrawable?>(holder.imageView) {
                override fun setResource(resource: GifDrawable?) {
                    holder.imageView.setImageDrawable(resource)
                }
            })
    }
}