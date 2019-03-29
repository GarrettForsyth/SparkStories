package com.example.android.sparkstories.model
/**
 * Status of a resource that is provided to the UI.
 *
 * Typically created by Repository class which return
 * 'LiveData<Resource<T>>' to pass the latest data
 * fetched to the Ui
 */
enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}