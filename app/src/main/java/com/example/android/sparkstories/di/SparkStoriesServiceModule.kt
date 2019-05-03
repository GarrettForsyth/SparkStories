package com.example.android.sparkstories.di

import android.content.SharedPreferences
import com.example.android.sparkstories.auth.Authenticator
import com.example.android.sparkstories.api.SparkStoriesService
import com.example.android.sparkstories.api.SparkStoriesServiceImpl
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class SparkStoriesServiceModule {

    @Singleton
    @Provides
    fun provideSparkStoriesService(
        authenticator: Authenticator,
        firestore: FirebaseFirestore,
        sharedPreferences: SharedPreferences
    ): SparkStoriesService {
        return SparkStoriesServiceImpl(authenticator, firestore, sharedPreferences)
    }

    @Singleton
    @Provides
    fun provideFireStore() = FirebaseFirestore.getInstance()

}
