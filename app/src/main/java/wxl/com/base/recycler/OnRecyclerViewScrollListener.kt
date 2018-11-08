package wxl.com.base.recycler

import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import wxl.com.base.utils.MyLog
import wxl.com.base.utils.UIUtil
import wxl.com.base.utils.UIUtil.getResources
/**
 * @date Created time 2018/9/12 11:41
 * @author wuxiulin
 * @description RecyclerView 滚动监听
 */
open abstract class OnRecyclerViewScrollListener : RecyclerView.OnScrollListener(), LoadMoreListener {
    private var lastVisibleItemPosition: Int = 0
    private var mIsLoadingMore = false
    private var swipeRefreshLayout: SwipeRefreshLayout? = null

    private fun isLoadingMore(): Boolean {
        return mIsLoadingMore
    }

    fun setLoadingMore(loadingMore: Boolean) {
        mIsLoadingMore = loadingMore
    }

    fun setSwipeRefreshLayout(swipeRefreshLayout: SwipeRefreshLayout) {
        this.swipeRefreshLayout = swipeRefreshLayout
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
//        if (recyclerView.layoutManager is LinearLayoutManager) {
//            lastVisibleItemPosition = (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
//        } else if (recyclerView.layoutManager is GridLayoutManager) {
//            lastVisibleItemPosition = (recyclerView.layoutManager as GridLayoutManager).findLastVisibleItemPosition()
//        } else if (recyclerView.layoutManager is StaggeredGridLayoutManager) {
//            var manager = recyclerView.layoutManager as StaggeredGridLayoutManager
//            var lastPositons = IntArray(manager.spanCount)
//            manager.findLastVisibleItemPositions(lastPositons)
//            lastVisibleItemPosition = findMax(lastPositons)
//
//        }
    }


    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        var childCount = recyclerView.layoutManager?.childCount
        var totalItemCount = recyclerView.layoutManager?.itemCount
        if (childCount != null && totalItemCount != null) {
            var lastView = recyclerView.getChildAt(childCount - 1)


//            if(childCount > 0&&newState==RecyclerView.SCROLL_STATE_IDLE&&lastVisibleItemPosition >= totalItemCount - 1){
//                MyLog.e("===","$mIsLoadingMore")
//                if(!isLoadingMore()){
//                    this.mIsLoadingMore =true
//                    onStart()
//                    //onLoadMore()
//                }
//            }
            //当前屏幕显示的区域高度
            var scrollExtent = recyclerView.computeVerticalScrollExtent()
            //获取整个View控件的高度
            var scrollRange = recyclerView.computeVerticalScrollRange()
            //获取当前屏幕滑过的距离
            var scrollOffset = recyclerView.computeVerticalScrollOffset()

           // MyLog.e("===", "$scrollRange --- $scrollExtent --- $scrollOffset  --- ${recyclerView.top}")
            //获取状态栏的高度
//            var statusBarHeight1 = -1    //获取status_bar_height资源的ID
//            var resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android")
//            if (resourceId > 0) {
//                //根据资源ID获取响应的尺寸值
//                statusBarHeight1 = getResources().getDimensionPixelSize(resourceId)
//            }

            //停止滚动 SCROLL_STATE_IDLE
            if (childCount > 0 && newState == RecyclerView.SCROLL_STATE_IDLE && lastView is LoadMoreView) {
                if (!isLoadingMore()) {
                    swipeRefreshLayout?.let {
                        if (it.isRefreshing) {
                            return
                        }
                    }
                    //获取recyclerView的起点位置
                    var ins= IntArray(2)
                    recyclerView.getLocationOnScreen(ins)
                    //判段是否到屏幕底部
                    if (UIUtil.getScreenHeight()-ins[1] != scrollExtent) {
                        return
                    }
                    mIsLoadingMore = true
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