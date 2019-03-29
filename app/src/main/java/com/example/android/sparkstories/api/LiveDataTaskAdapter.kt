package com.example.android.sparkstories.api

import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.Task
import java.lang.reflect.Type
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Retrofit adapter that converts the Call into a LiveData ApiResponse
 * @param <R> the type the ApiResponse holds (ApiResponse<R>)
 */
class LiveDataTaskAdapter<R>() {

    fun adapt(task: Task<R>): LiveData<ApiResponse<R>> {
        return object : LiveData<ApiResponse<R>>() {

            private var started = AtomicBoolean(false)

            override fun onActive() {
                if (started.compareAndSet(false, true)) {
                    task.addOnCompleteListener {

                        if (it.isSuccessful) {
                            postValue(ApiResponse.create(it))
                        }else {
                            postValue(ApiResponse.create(it.exception))
                        }

                    }
                }
            }
        }
    }
}