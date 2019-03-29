package com.example.android.sparkstories.di

import android.app.Application
import com.example.android.sparkstories.AndroidTestApp
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
            AndroidTestRemoteDatabaseModule::class
        ]
)
interface AndroidTestAppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application) : Builder

        fun build(): AndroidTestAppComponent
    }

    fun inject(app: AndroidTestApp)
}