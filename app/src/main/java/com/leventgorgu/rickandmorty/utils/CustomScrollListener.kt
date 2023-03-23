package com.leventgorgu.rickandmorty.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CustomScrollListener(private val layoutManager: LinearLayoutManager, private val loadMore: () -> Unit) : RecyclerView.OnScrollListener() {

    private var isLoading = false

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
            val totalItemCount = layoutManager.itemCount
            if (lastVisibleItemPosition == totalItemCount - 1 && !isLoading) {
                isLoading = true
                loadMore.invoke()
            }
        }
    }

    fun setIsLoading(isLoading: Boolean) {
        this.isLoading = isLoading
    }
}