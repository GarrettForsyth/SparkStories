package com.example.android.writeitsayithearit.ui.util

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.example.android.writeitsayithearit.BR
import com.example.android.writeitsayithearit.model.SortOrder
import timber.log.Timber

data class QueryParameters(
    private var _filterCueId: Int = -1,
    private var _filterStoryId: Int = -1,
    private var _filterParentCommentId: Int = -1,
    private var _filterString: String = "",
    private var _sortOrder: SortOrder = SortOrder.NEW
): BaseObservable() {

    var filterCueId: Int
        @Bindable get() = _filterCueId
        set(value) {
            _filterCueId = value
            notifyPropertyChanged(BR.viewmodel)
        }

    var filterStoryId: Int
        @Bindable get() = _filterStoryId
        set(value) {
            _filterStoryId = value
            notifyPropertyChanged(BR.viewmodel)
        }

    var filterParentCommentId: Int
        @Bindable get() = _filterParentCommentId
        set(value) {
            _filterParentCommentId = value
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
            _sortOrder = value
            notifyPropertyChanged(BR.viewmodel)
        }

    override fun equals(other: Any?): Boolean {
        return (other is QueryParameters)
                && this._filterCueId == other._filterCueId
                && this._filterStoryId == other._filterStoryId
                && this._filterParentCommentId == other._filterParentCommentId
                && this._filterString == other._filterString
                && this._sortOrder == other._sortOrder
    }

}