package com.example.android.sparkstories

import android.app.Activity
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.example.android.sparkstories.di.AppInjector
import com.google.firebase.FirebaseApp
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

        // Dagger Components are injected in each test suite such that
        // each test suite can use the module it needs.

        Timber.d("AndroidTest injection used.")
        Timber.d("Application onCreate() mytrace")
    }

    override fun activityInjector() = dispatchingAndroidInjector
}
