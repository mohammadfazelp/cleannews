package com.faz.cleannews.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.faz.cleannews.data.datasource.NewsDataFactory
import com.faz.cleannews.data.datasource.NewsDataSource
import com.faz.cleannews.data.model.News
import com.faz.cleannews.data.remote.RestApi
import com.faz.cleannews.data.remote.State
import io.reactivex.disposables.CompositeDisposable

class FeedListViewModel : ViewModel() {

    companion object {
        private const val PAGE_SIZE = 5
    }

    private val restApi = RestApi.getService()
    var feedList: LiveData<PagedList<News>>
    private val compositeDisposable = CompositeDisposable()
    private val dataSourceFactory: NewsDataFactory

    init {
        dataSourceFactory = NewsDataFactory(compositeDisposable, restApi)
        val config = PagedList.Config.Builder()
            .setPageSize(PAGE_SIZE)
            .setInitialLoadSizeHint(PAGE_SIZE * 2)
            .setEnablePlaceholders((false)).build()
        feedList = LivePagedListBuilder(dataSourceFactory, config).build()
    }

    fun getState(): LiveData<State> = Transformations.switchMap(
        dataSourceFactory.feedDataSourceLiveData,
        NewsDataSource::state
    )

    fun retry() {
        dataSourceFactory.feedDataSourceLiveData.value?.retry()
    }

    fun isListEmpty(): Boolean {
        return feedList.value?.isEmpty() ?: true
    }

    fun isListIsNotEmpty(): Boolean {
        return !isListEmpty()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}