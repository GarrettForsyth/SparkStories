package com.example.android.sparkstories.di

import androidx.test.core.app.ApplicationProvider
import com.example.android.sparkstories.AndroidTestApp
import com.example.android.sparkstories.di.appcomponents.DaggerAndroidTestAppComponent
import com.example.android.sparkstories.di.appcomponents.DaggerNewUserTestAppComponent

object SparkStoriesTestConfigurations {

    fun injectAndroidTestAppComponent() {
        val androidTestApp = ApplicationProvider.getApplicationContext<AndroidTestApp>()
        DaggerAndroidTestAppComponent.builder()
            .application(androidTestApp)
            .build()
            .inject(androidTestApp)
        AppInjector.init(androidTestApp)
    }

    fun injectNewUserTestAppComponent() {
        val androidTestApp = ApplicationProvider.getApplicationContext<AndroidTestApp>()
        DaggerNewUserTestAppComponent.builder()
            .application(androidTestApp)
            .build()
            .inject(androidTestApp)
        AppInjector.init(androidTestApp)

    }

}