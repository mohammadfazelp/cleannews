package com.faz.cleannews.data.datasource

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.faz.cleannews.data.model.News
import com.faz.cleannews.data.remote.RestApi
import com.faz.cleannews.data.remote.State
import com.faz.cleannews.utils.API_KEY
import com.faz.cleannews.utils.QUERY
import com.faz.cleannews.utils.SOURCES
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers

class NewsDataSource(
    private val restApi: RestApi,
    private val compositeDisposable: CompositeDisposable
) : PageKeyedDataSource<Int, News>() {

    companion object {
        private val TAG = NewsDataSource::class.java.simpleName
    }

    var state: MutableLiveData<State> = MutableLiveData()
    private var retryCompletable: Completable? = null

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, News>
    ) {
        compositeDisposable.add(
            restApi.getNews(1, params.requestedLoadSize, SOURCES, API_KEY)
                .subscribe({ response ->
                    updateState(State.DONE)
                    callback.onResult(
                        response.news,
                        null,
                        2
                    )
                }, {
                    it?.let { throwable ->
                        throwable.message?.let { message ->
                            Log.e(TAG, message)
                        }
                    }
                    updateState(State.ERROR)
                    setRetry { loadInitial(params, callback) }
                })
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, News>) {
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, News>) {
        updateState(State.LOADING)
        compositeDisposable.add(
            restApi.getNews(params.key, params.requestedLoadSize, SOURCES, API_KEY)
                .subscribe({ response ->
                    updateState(State.DONE)
                    callback.onResult(
                        response.news,
                        params.key + 1
                    )
                }, {
                    it?.let { throwable ->
                        throwable.message?.let { message ->
                            Log.e(TAG, message)
                        }
                    }
                    updateState(State.ERROR)
                    setRetry { loadAfter(params, callback) }
                })
        )
    }

    fun retry() {
        if (retryCompletable != null) {
            compositeDisposable.add(
                retryCompletable!!
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()
            )
        }
    }

    private fun updateState(state: State) {
        this.state.postValue(state)
    }

    private fun setRetry(action: Action?) {
        retryCompletable = if (action == null) null else Completable.fromAction(action)
    }
}