package com.example.android.sparkstories.util

import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.android.sparkstories.test.getValueBlocking

fun <T> dataSourceFactoryToPagedList(
    dataSourceFactory: DataSource.Factory<Int, T>,
    pagedSize: Int
): PagedList<T> {
    return LivePagedListBuilder<Int, T>(dataSourceFactory, pagedSize)
        .build()
        .getValueBlocking()
}
