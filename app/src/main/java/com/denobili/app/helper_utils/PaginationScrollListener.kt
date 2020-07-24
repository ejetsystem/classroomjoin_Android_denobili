package com.denobili.app.helper_utils

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

abstract class PaginationScrollListener/**
 * Supporting only LinearLayoutManager for now.
 *
 * @param layoutManager
 */
(layoutManager: LinearLayoutManager): RecyclerView.OnScrollListener() {
    internal var layoutManager:LinearLayoutManager
    abstract val totalPageCount:Int
    abstract val isLastPage:Boolean
    abstract var isLoading:Boolean
    init{
        this.layoutManager = layoutManager
    }
    override fun onScrolled(recyclerView:RecyclerView, dx:Int, dy:Int) {
        super.onScrolled(recyclerView, dx, dy)
        val visibleItemCount = layoutManager.getChildCount()
        val totalItemCount = layoutManager.getItemCount()
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
        if (!isLoading && !isLastPage)
        {
            if (((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0))
            {
                loadMoreItems()
            }
            else{

            }
            // && totalItemCount >= getTotalPageCount()
        }
        else{

        }
    }
    // && totalItemCount >= getTotalPageCount()
    protected abstract fun loadMoreItems()
}