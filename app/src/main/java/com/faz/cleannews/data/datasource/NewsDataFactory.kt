package com.faz.cleannews.data.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.faz.cleannews.data.model.News
import com.faz.cleannews.data.remote.RestApi
import io.reactivex.disposables.CompositeDisposable

class NewsDataFactory(
    private val compositeDisposable: CompositeDisposable,
    private val restApi: RestApi
) : DataSource.Factory<Int, News>() {

    val feedDataSourceLiveData = MutableLiveData<NewsDataSource>()

    override fun create(): DataSource<Int, News> {
        val newsDataSource = NewsDataSource(restApi, compositeDisposable)
        feedDataSourceLiveData.postValue(newsDataSource)
        return newsDataSource
    }
}
