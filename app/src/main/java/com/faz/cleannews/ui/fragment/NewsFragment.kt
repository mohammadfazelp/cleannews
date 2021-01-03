package com.faz.cleannews.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.faz.cleannews.R
import com.faz.cleannews.data.remote.State
import com.faz.cleannews.ui.NewsAdapter
import com.faz.cleannews.viewmodel.FeedListViewModel
import kotlinx.android.synthetic.main.fragment_news.*

class NewsFragment : Fragment() {

    private lateinit var viewModel: FeedListViewModel
    private lateinit var adapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(FeedListViewModel::class.java)
        initAdapter()
        initList()
    }

    private fun initAdapter() {
        adapter = NewsAdapter { viewModel.retry() }
        listFeedRV.adapter = adapter
        viewModel.feedList.observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })
    }

    private fun initList() {
        txt_error.setOnClickListener { viewModel.retry() }
        viewModel.getState().observe(viewLifecycleOwner, { state ->
            progress_bar.visibility =
                if (viewModel.isListEmpty() && state == State.LOADING) VISIBLE else GONE
            txt_error.visibility =
                if (viewModel.isListEmpty() && state == State.ERROR) VISIBLE else GONE
            if (viewModel.isListIsNotEmpty()) {
                adapter.setState(state ?: State.DONE)
            }
        })
    }
}