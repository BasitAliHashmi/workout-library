package com.basit.workout.model

import com.basit.workout.R
import com.basit.workout_library.models.BaseSimpleWorkout

class SimpleWorkout {

    companion object {

        val Burpee = BaseSimpleWorkout(
            1,
            "Burpee",
            R.drawable.burpee_card,
            R.drawable.burpee_still
        )

        val BodyWeightSquatJump = BaseSimpleWorkout(
            2,
            "Body Weight Squat Jump",
            R.drawable.bodyweightsquatjump_card,
            R.drawable.bodyweightsquatjump_still
        )

        val BicycleCrunches = BaseSimpleWorkout(
            3,
            "Bicycle Crunches",
            R.drawable.bicycle_crunches_card,
            R.drawable.bicycle_crunches_still
        )

        val Bridge = BaseSimpleWorkout(
            4,
            "Bridge",
            R.drawable.bridge_card,
            R.drawable.bridge_still
        )

        val Squat = BaseSimpleWorkout(
            5,
            "Squat",
            R.drawable.squats_card,
            R.drawable.squats_still
        )

        val PlankCrawl = BaseSimpleWorkout(
            6,
            "Plank Crawl",
            R.drawable.plank_crawl_card,
            R.drawable.plank_crawl_still
        )

        val Pushups = BaseSimpleWorkout(
            7,
            "Push ups",
            R.drawable.pushup_card,
            R.drawable.push_ups_still
        )

        val SidePlank = BaseSimpleWorkout(
            8,
            "Side Plank",
            R.drawable.side_plank_card,
            R.drawable.side_plank_still
        )

        val Skaters = BaseSimpleWorkout(
            9,
            "Skaters",
            R.drawable.skaters_card,
            R.drawable.skaters_still
        )

        val Crunches = BaseSimpleWorkout(
            15,
            "Crunches",
            R.drawable.crunches_card,
            R.drawable.crunches_still
        )

        val MountainClimber = BaseSimpleWorkout(
            17,
            "Mountain Climber",
            R.drawable.mountain_climber_card,
            R.drawable.mountain_climber_still
        )

        val Plank = BaseSimpleWorkout(
            20,
            "Plank",
            R.drawable.planks_card,
            R.drawable.planks_still
        )

        val WalkingLunge = BaseSimpleWorkout(
            21,
            "Walking Lunge",
            R.drawable.walking_lunge_card,
            R.drawable.walking_lunge_still
        )

    }

}