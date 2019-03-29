package com.example.android.sparkstories.di

import androidx.lifecycle.MutableLiveData
import com.example.android.sparkstories.data.remote.RemoteDatabase
import com.example.android.sparkstories.model.Resource
import com.facebook.internal.Mutable
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import dagger.Module
import dagger.Provides
import io.mockk.every
import io.mockk.invoke
import io.mockk.mockk
import io.mockk.slot
import timber.log.Timber
import javax.inject.Singleton

@Module
class AndroidTestRemoteDatabaseModule {

    @Singleton
    @Provides
    fun provideRemoteDatabase(): RemoteDatabase {
        val db: RemoteDatabase = mockk(relaxed = true)

        val userExistence = MutableLiveData<Resource<Boolean>>()
        userExistence.postValue(Resource.success(true))
        every { db.checkUserExists() } returns userExistence

        return db
    }

}
