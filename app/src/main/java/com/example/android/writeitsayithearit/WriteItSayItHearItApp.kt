package com.example.android.writeitsayithearit

import android.app.Activity
import android.app.Application
import com.example.android.writeitsayithearit.di.AppInjector
import com.example.android.writeitsayithearit.di.DaggerAppComponent
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import timber.log.Timber
import javax.inject.Inject

class WriteItSayItHearItApp : Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        DaggerAppComponent.builder()
                .application(this)
                .build()
                .inject(this)
        AppInjector.init(this)
    }

    override fun activityInjector() = dispatchingAndroidInjector

}