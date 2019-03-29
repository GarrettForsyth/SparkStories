package com.example.android.sparkstories.api

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.android.sparkstories.R
import com.google.android.gms.tasks.Task
import java.io.IOException
import java.lang.Exception

/**
 * Class used to handle API responses.
 * @param <T> the type of response objects.
 */
sealed class ApiResponse<T> {

    companion object {

        fun <T> create(error: Exception?): ApiErrorResponse<T> {
            val context = ApplicationProvider.getApplicationContext<Context>()
            val message = if (error is IOException) {
                context.resources.getString(R.string.internet_unavailable)
            } else {
                context.resources.getString(R.string.unknown_network_error)
            }
            return ApiErrorResponse(message)
        }

        fun <T> create(task: Task<T>): ApiResponse<T> {
            return if (task.isSuccessful) {
                val body = task.result
                if (body == null) {
                    ApiEmptyResponse()
                } else {
                    ApiSuccessResponse(body = body)
                }
            } else {
                val context = ApplicationProvider.getApplicationContext<Context>()
                ApiErrorResponse(context.resources.getString(R.string.unknown_network_error))
            }
        }
    }
}

/**
 * Represents HTTP 204 response
 * Use this to avoid passing a null body with a ApiSuccessResponse
 */
class ApiEmptyResponse<T> : ApiResponse<T>()

class ApiErrorResponse<T>(val errorMessage: String) : ApiResponse<T>()

data class ApiSuccessResponse<T>(val body: T) : ApiResponse<T>()
