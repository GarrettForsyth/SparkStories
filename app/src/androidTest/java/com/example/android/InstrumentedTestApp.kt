package com.example.android

import android.app.Activity
import android.app.Application
import com.example.android.writeitsayithearit.di.AppInjector
import com.example.android.writeitsayithearit.di.DaggerAppComponent
import com.example.android.writeitsayithearit.test.BuildConfig
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import timber.log.Timber
import javax.inject.Inject

class InstrumentedTestApp : Application(), HasActivityInjector {

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
        Timber.d("Test Injection used.")
    }

    override fun activityInjector() = dispatchingAndroidInjector
}