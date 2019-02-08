package com.example.android.writeitsayithearit.di

import com.example.android.writeitsayithearit.ui.CuesFragment
import com.example.android.writeitsayithearit.ui.NewCueFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeCuesFragment() : CuesFragment

    @ContributesAndroidInjector
    abstract fun contributeNewCueFragment() : NewCueFragment
}