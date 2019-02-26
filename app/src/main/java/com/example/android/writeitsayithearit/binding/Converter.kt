package com.example.android.writeitsayithearit.binding

import com.example.android.writeitsayithearit.model.SortOrder

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
