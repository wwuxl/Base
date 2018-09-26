package wxl.com.base.recycler

import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager

open abstract class OnRecyclerViewScrollListener : RecyclerView.OnScrollListener(), LoadMoreListener {
    private var lastVisibleItemPosition: Int = 0
    private var mIsLoadingMore = false
    private var swipeRefreshLayout:SwipeRefreshLayout?=null

    private fun isLoadingMore(): Boolean {
        return mIsLoadingMore
    }

    fun setLoadingMore(loadingMore: Boolean) {
        mIsLoadingMore = loadingMore
    }
    fun setSwipeRefreshLayout(swipeRefreshLayout: SwipeRefreshLayout){
        this.swipeRefreshLayout = swipeRefreshLayout
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
            var lastView=recyclerView.getChildAt(childCount-1)


//            if(childCount > 0&&newState==RecyclerView.SCROLL_STATE_IDLE&&lastVisibleItemPosition >= totalItemCount - 1){
//                MyLog.e("===","$mIsLoadingMore")
//                if(!isLoadingMore()){
//                    this.mIsLoadingMore =true
//                    onStart()
//                    //onLoadMore()
//                }
//            }

            //停止滚动 SCROLL_STATE_IDLE
            if(childCount > 0&&newState==RecyclerView.SCROLL_STATE_IDLE&&lastView is LoadMoreView){
                if(!isLoadingMore()){
                    this.mIsLoadingMore =true

                    swipeRefreshLayout?.let {
                        if(it.isRefreshing){
                            return
                        }
                    }
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