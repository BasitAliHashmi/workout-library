package com.basit.workout.model

import android.graphics.Color
import com.basit.workout.R
import com.basit.workout_library.models.FitnessProgram
import com.basit.workout_library.models.FitnessProgramDay
import com.basit.workout_library.models.FitnessProgramDifficulty
import com.basit.workout_library.models.Workout
import com.basit.workout_library.models.WorkoutType

class LoseBellyFatProgram {

    companion object {

        fun program(): FitnessProgram {

            return FitnessProgram(
                1,
                "Lose Belly Fat",
                FitnessProgramDifficulty.Beginner,
                arrayListOf(
                    day1(),
                    day2(),
                    day3(),
                    FitnessProgramDay.restDay(),
                    day5()
                ),
                R.drawable.fat_loss_model,
                Color.parseColor("#6200EE")
                //Color.parseColor("#164E7C")
            )
        }

        //region DAYS

        private fun day1(): FitnessProgramDay {

            val list = ArrayList<Workout>()

            list.add(Workout(SimpleWorkout.Burpee, WorkoutType.Timed, 20))
            list.add(Workout(SimpleWorkout.BodyWeightSquatJump, WorkoutType.Timed, 20))
            list.add(Workout(SimpleWorkout.Bridge, WorkoutType.Frequency, 10))
            list.add(Workout(SimpleWorkout.Squat, WorkoutType.Timed, 20))
            list.add(Workout(SimpleWorkout.SidePlank, WorkoutType.Timed, 20))
            list.add(Workout(SimpleWorkout.Skaters, WorkoutType.Timed, 20))
            list.add(Workout(SimpleWorkout.WalkingLunge, WorkoutType.Timed, 30))
            list.add(Workout(SimpleWorkout.BicycleCrunches, WorkoutType.Frequency, 12))
            list.add(Workout(SimpleWorkout.PlankCrawl, WorkoutType.Frequency, 18))
            list.add(Workout(SimpleWorkout.Pushups, WorkoutType.Frequency, 20))

            list.add(Workout(SimpleWorkout.MountainClimber, WorkoutType.Timed, 20))
            list.add(Workout(SimpleWorkout.Plank, WorkoutType.Timed, 20))
            list.add(Workout(SimpleWorkout.Crunches, WorkoutType.Timed, 20))

            return FitnessProgramDay(workouts = list)
        }

        private fun day2(): FitnessProgramDay {

            val list = ArrayList<Workout>()

            list.add(Workout(SimpleWorkout.BodyWeightSquatJump, WorkoutType.Timed, 20))
            list.add(Workout(SimpleWorkout.Bridge, WorkoutType.Frequency, 10))
            list.add(Workout(SimpleWorkout.Squat, WorkoutType.Timed, 20))
            list.add(Workout(SimpleWorkout.SidePlank, WorkoutType.Timed, 20))
            list.add(Workout(SimpleWorkout.Skaters, WorkoutType.Timed, 20))
            list.add(Workout(SimpleWorkout.WalkingLunge, WorkoutType.Timed, 30))
            list.add(Workout(SimpleWorkout.BicycleCrunches, WorkoutType.Frequency, 12))
            list.add(Workout(SimpleWorkout.PlankCrawl, WorkoutType.Frequency, 18))
            list.add(Workout(SimpleWorkout.Pushups, WorkoutType.Frequency, 20))
            list.add(Workout(SimpleWorkout.Burpee, WorkoutType.Timed, 20))

            list.add(Workout(SimpleWorkout.MountainClimber, WorkoutType.Timed, 20))
            list.add(Workout(SimpleWorkout.Plank, WorkoutType.Timed, 20))
            list.add(Workout(SimpleWorkout.Crunches, WorkoutType.Timed, 20))


            return FitnessProgramDay(workouts = list)
        }

        private fun day3(): FitnessProgramDay {

            val list = ArrayList<Workout>()

            list.add(Workout(SimpleWorkout.Bridge, WorkoutType.Frequency, 10))
            list.add(Workout(SimpleWorkout.Squat, WorkoutType.Timed, 20))
            list.add(Workout(SimpleWorkout.SidePlank, WorkoutType.Timed, 20))
            list.add(Workout(SimpleWorkout.Skaters, WorkoutType.Timed, 20))
            list.add(Workout(SimpleWorkout.WalkingLunge, WorkoutType.Timed, 30))
            list.add(Workout(SimpleWorkout.BicycleCrunches, WorkoutType.Frequency, 12))
            list.add(Workout(SimpleWorkout.PlankCrawl, WorkoutType.Frequency, 18))
            list.add(Workout(SimpleWorkout.Pushups, WorkoutType.Frequency, 20))
            list.add(Workout(SimpleWorkout.Burpee, WorkoutType.Timed, 20))
            list.add(Workout(SimpleWorkout.BodyWeightSquatJump, WorkoutType.Timed, 20))


            list.add(Workout(SimpleWorkout.MountainClimber, WorkoutType.Timed, 20))
            list.add(Workout(SimpleWorkout.Plank, WorkoutType.Timed, 20))
            list.add(Workout(SimpleWorkout.Crunches, WorkoutType.Timed, 20))

            return FitnessProgramDay(workouts = list)
        }

        private fun day5(): FitnessProgramDay {

            val list = ArrayList<Workout>()

            list.add(Workout(SimpleWorkout.MountainClimber, WorkoutType.Timed, 20))

            list.add(Workout(SimpleWorkout.SidePlank, WorkoutType.Timed, 20))
            list.add(Workout(SimpleWorkout.Skaters, WorkoutType.Timed, 20))
            list.add(Workout(SimpleWorkout.WalkingLunge, WorkoutType.Timed, 30))
            list.add(Workout(SimpleWorkout.BicycleCrunches, WorkoutType.Frequency, 12))
            list.add(Workout(SimpleWorkout.PlankCrawl, WorkoutType.Frequency, 18))
            list.add(Workout(SimpleWorkout.Pushups, WorkoutType.Frequency, 20))
            list.add(Workout(SimpleWorkout.Burpee, WorkoutType.Timed, 20))
            list.add(Workout(SimpleWorkout.BodyWeightSquatJump, WorkoutType.Timed, 20))
            list.add(Workout(SimpleWorkout.Bridge, WorkoutType.Frequency, 10))
            list.add(Workout(SimpleWorkout.Squat, WorkoutType.Timed, 20))

            list.add(Workout(SimpleWorkout.Plank, WorkoutType.Timed, 20))
            list.add(Workout(SimpleWorkout.Crunches, WorkoutType.Timed, 20))




            return FitnessProgramDay(workouts = list)
        }

        //endregion

    }
}