package com.example.android.sparkstories.binding

import com.example.android.sparkstories.model.SortOrder

object Converter {

    @JvmStatic
    fun toSortOrder(selection: Any): SortOrder {
        return when (selection) {
            "New" -> SortOrder.NEW
            "Top" -> SortOrder.TOP
            else -> SortOrder.HOT
        }

    }

}
