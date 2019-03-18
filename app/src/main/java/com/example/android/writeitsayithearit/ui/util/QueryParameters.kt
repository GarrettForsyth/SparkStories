package com.example.android.writeitsayithearit.ui.util

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.example.android.writeitsayithearit.BR
import com.example.android.writeitsayithearit.model.SortOrder
import timber.log.Timber

data class QueryParameters(
    private var _filterId: Int,
    private var _filterString: String,
    private var _sortOrder: SortOrder
): BaseObservable() {

    var filterId: Int
        @Bindable get() = _filterId
        set(value) {
            _filterId = value
            notifyPropertyChanged(BR.viewmodel)
        }

    var filterString: String
        @Bindable get() = _filterString
        set(value) {
            _filterString = value
            notifyPropertyChanged(BR.viewmodel)
        }

    var sortOrder: SortOrder
        @Bindable get() = _sortOrder
        set(value) {
            Timber.d("mytest Setting sort order to $value")
            _sortOrder = value
            notifyPropertyChanged(BR.viewmodel)
        }

    override fun equals(other: Any?): Boolean {
        return (other is QueryParameters)
                && this._filterId == other._filterId
                && this._filterString == other._filterString
                && this._sortOrder == other._sortOrder
    }

}