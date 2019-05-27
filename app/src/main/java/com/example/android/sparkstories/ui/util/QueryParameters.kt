package com.example.android.sparkstories.ui.util

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.example.android.sparkstories.BR
import com.example.android.sparkstories.model.SortOrder
import timber.log.Timber

data class QueryParameters(
    private var _filterCueId: String = "",
    private var _filterStoryId: String = "",
    private var _filterParentCommentId: Int = -1,
    private var _filterString: String = "",
    private var _sortOrder: SortOrder = SortOrder.NEW
): BaseObservable() {

    var filterCueId: String
        @Bindable get() = _filterCueId
        set(value) {
            if (value != _filterCueId) {
                _filterCueId = value
                notifyPropertyChanged(BR.viewmodel)
            }
        }

    var filterStoryId: String
        @Bindable get() = _filterStoryId
        set(value) {
            if (value != _filterStoryId) {
                _filterStoryId = value
                notifyPropertyChanged(BR.viewmodel)
            }
        }

    var filterParentCommentId: Int
        @Bindable get() = _filterParentCommentId
        set(value) {
            if (value != _filterParentCommentId) {
                _filterParentCommentId = value
                notifyPropertyChanged(BR.viewmodel)
            }
        }

    var filterString: String
        @Bindable get() = _filterString
        set(value) {
            if (value != _filterString) {
                _filterString = value
                notifyPropertyChanged(BR.viewmodel)
            }
        }

    var sortOrder: SortOrder
        @Bindable get() = _sortOrder
        set(value) {
            if(value != _sortOrder) {
                _sortOrder = value
                notifyPropertyChanged(BR.viewmodel)
            }
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