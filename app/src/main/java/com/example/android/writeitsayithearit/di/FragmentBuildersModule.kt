package com.example.android.writeitsayithearit.di

import com.example.android.writeitsayithearit.ui.QueuesFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeQueuesFragment() : QueuesFragment
}