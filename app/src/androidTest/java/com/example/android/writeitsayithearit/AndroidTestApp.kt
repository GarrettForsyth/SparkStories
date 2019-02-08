package com.example.android.writeitsayithearit

import android.app.Activity
import android.app.Application
import com.example.android.writeitsayithearit.di.AppInjector
import com.example.android.writeitsayithearit.di.DaggerAndroidTestAppComponent
import com.example.android.writeitsayithearit.di.DaggerAppComponent
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import timber.log.Timber
import javax.inject.Inject

/**
 * AndroidTest app replaces some dependencies to be more test friendly.
 * Unit test are expected to mock out dependencies.
 */
class AndroidTestApp : Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        DaggerAndroidTestAppComponent.builder()
                .application(this)
                .build()
                .inject(this)
        AppInjector.init(this)
        Timber.d("AndroidTest injection used.")
    }

    override fun activityInjector() = dispatchingAndroidInjector
}
