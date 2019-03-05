package com.example.android.writeitsayithearit

import android.app.Activity
import android.app.Application
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.example.android.writeitsayithearit.di.AppInjector
import com.example.android.writeitsayithearit.di.DaggerAppComponent
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import timber.log.Timber
import javax.inject.Inject

class WriteItSayItHearItApp : MultiDexApplication(), HasActivityInjector {

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
        Timber.d("Production injection used.")
    }

    override fun activityInjector() = dispatchingAndroidInjector

}