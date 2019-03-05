package com.example.android.writeitsayithearit

import android.app.Activity
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.example.android.writeitsayithearit.di.AppInjector
import com.example.android.writeitsayithearit.di.DaggerAndroidTestAppComponent
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import timber.log.Timber
import javax.inject.Inject

/**
 * AndroidTest app replaces some dependencies to be more test friendly.
 * Unit test are expected to mock out dependencies.
 */
class AndroidTestApp : MultiDexApplication(), HasActivityInjector {

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
        Timber.d("Application onCreate() mytrace")
    }

    override fun activityInjector() = dispatchingAndroidInjector
}
