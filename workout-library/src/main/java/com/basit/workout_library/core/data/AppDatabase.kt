package com.basit.workout_library.core.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.basit.workout_library.core.data.converter.AppDateTimeTypeConverter
import com.basit.workout_library.core.data.dao.WorkoutHistoryDao
import com.basit.workout_library.core.data.entity.WorkoutHistoryEntity

@Database(entities = [WorkoutHistoryEntity::class], version = 1, exportSchema = false)
@TypeConverters(AppDateTimeTypeConverter::class)
internal abstract class AppDatabase: RoomDatabase() {

    abstract fun workoutHistoryDao(): WorkoutHistoryDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java, "fitness-app.db"
            )
                .addTypeConverter(AppDateTimeTypeConverter())
                .fallbackToDestructiveMigration()
                .build()
    }
}