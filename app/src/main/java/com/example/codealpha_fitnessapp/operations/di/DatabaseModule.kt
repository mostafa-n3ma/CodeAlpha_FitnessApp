package com.example.codealpha_fitnessapp.operations.di

import android.content.Context
import androidx.room.Room
import com.example.codealpha_fitnessapp.operations.dataMangment.FitDatabase
import com.example.codealpha_fitnessapp.operations.dataMangment.GoalDao
import com.example.codealpha_fitnessapp.operations.dataMangment.UserDao
import com.example.codealpha_fitnessapp.operations.dataMangment.WorkoutDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context):FitDatabase{
        return Room.databaseBuilder(
            context,
            FitDatabase::class.java,
            FitDatabase::class.java.name
        ).fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun provideUserDao(database: FitDatabase): UserDao {
        return database.userDao()
    }

    @Singleton
    @Provides
    fun provideWorkoutDao(database: FitDatabase): WorkoutDao {
        return database.workoutDao()
    }

    @Singleton
    @Provides
    fun provideGoalDao(database: FitDatabase): GoalDao {
        return database.goalDao()
    }


}