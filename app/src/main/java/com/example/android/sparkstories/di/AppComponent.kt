package com.example.android.sparkstories.di

import android.app.Application
import com.example.android.sparkstories.WriteItSayItHearItApp
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
        DatabaseModule::class,
        AppExecutorsModule::class,
        AuthenticationModule::class,
        RemoteDatabaseModule::class
    ]
)
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application) : Builder

        fun build(): AppComponent
    }

    fun inject(app: WriteItSayItHearItApp)
}