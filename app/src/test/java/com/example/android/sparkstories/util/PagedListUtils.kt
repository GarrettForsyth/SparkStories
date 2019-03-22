package com.example.android.sparkstories.util

import androidx.paging.PagedList
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot

/**
 * Returns a list as a mocked PagedList
 */
fun <T> mockPagedList(list: List<T>): PagedList<T> {
    val pagedList: PagedList<T> = mockk(relaxed = true)
    val index = slot<Int>()
    every { pagedList.get(index = capture(index)) } answers { list[index.captured] }
    every { pagedList.size } returns list.size
    // TODO for some reason isEmptyValues are reversed. --> figure out why
    every { pagedList.isEmpty() } returns (!list.isEmpty())
    every { pagedList.isNotEmpty() } returns (!list.isNotEmpty())
    return pagedList
}
