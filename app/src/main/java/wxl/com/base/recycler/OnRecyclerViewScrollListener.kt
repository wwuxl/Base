package wxl.com.base.recycler

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager

open abstract class OnRecyclerViewScrollListener : RecyclerView.OnScrollListener(), LoadMoreListener {
    private var lastVisibleItemPosition: Int = 0
    private var mIsLoadingMore = false
    fun isLoadingMore(): Boolean {
        return mIsLoadingMore
    }

    fun setLoadingMore(loadingMore: Boolean) {
        mIsLoadingMore = loadingMore
    }
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (recyclerView.layoutManager is LinearLayoutManager) {
            lastVisibleItemPosition = (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
        } else if (recyclerView.layoutManager is GridLayoutManager) {
            lastVisibleItemPosition = (recyclerView.layoutManager as GridLayoutManager).findLastVisibleItemPosition()
        } else if (recyclerView.layoutManager is StaggeredGridLayoutManager) {
            var manager = recyclerView.layoutManager as StaggeredGridLayoutManager
            var lastPositons = IntArray(manager.spanCount)
            manager.findLastVisibleItemPositions(lastPositons)
            lastVisibleItemPosition = findMax(lastPositons)

        }


    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        var childCount = recyclerView.layoutManager?.childCount
        var totalItemCount = recyclerView.layoutManager?.itemCount
        if (childCount != null&&totalItemCount!=null) {
            //停止滚动 SCROLL_STATE_IDLE
            if(childCount > 0&&newState==RecyclerView.SCROLL_STATE_IDLE&&lastVisibleItemPosition >= totalItemCount - 1){
                if(!isLoadingMore()){
                    this.mIsLoadingMore =true
                    onStart()
                    onLoadMore()
                }
            }
        }
    }

    private fun findMax(lastPositions: IntArray): Int {
        var max = lastPositions[0]
        for (value in lastPositions) {
            if (value > max) {
                max = value
            }
        }
        return max
    }
}