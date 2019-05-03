package com.example.android.sparkstories.di.appcomponents

import android.app.Application
import com.example.android.sparkstories.AndroidTestApp
import com.example.android.sparkstories.di.AndroidTestAppExecutorsModule
import com.example.android.sparkstories.di.AndroidTestDatabaseModule
import com.example.android.sparkstories.di.AppModule
import com.example.android.sparkstories.di.authenticationmodules.AndroidTestAuthenticationModule
import com.example.android.sparkstories.di.MainActivityModule
import com.example.android.sparkstories.di.sparkservicestoriesmodules.NewUserSparkStoryServiceModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        MainActivityModule::class,
        AppModule::class,
        AndroidTestDatabaseModule::class,
        AndroidTestAppExecutorsModule::class,
        AndroidTestAuthenticationModule::class,
        NewUserSparkStoryServiceModule::class // swap in the new user service module
    ]
)
interface NewUserTestAppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application) : Builder

        fun build(): NewUserTestAppComponent
    }

    fun inject(app: AndroidTestApp)
}
