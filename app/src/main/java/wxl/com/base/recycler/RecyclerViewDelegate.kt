package wxl.com.base.recycler

import android.content.Context
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.GridLayoutManager.SpanSizeLookup
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import android.widget.LinearLayout
import wxl.com.base.R
import wxl.com.base.utils.MyLog

/**
 * @date Created time 2018/9/10 17:35
 * @author wuxiulin
 * @description 实现RecyclerView列表的封装
 */
class RecyclerViewDelegate<T> : OnRecyclerViewScrollListener {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: RecyclerViewAdapter<T>
    /**
     * 头部itemView
     */
    private var mHeaderView: View? = null
    /**
     * 底部itemView
     */
    private var mFooterView: View? = null


    private var mIAdapter: RIAdapter<T>
    private var mIReloadData: IReloadData
    private var mSwipeRefreshLayout: SwipeRefreshLayout? = null
    private var isPullUpRefresh: Boolean = true
    private var isPullDownRefresh: Boolean = true
    private var mDatas: ArrayList<T>


    companion object {
        private var mCurrentPage: Int = 0
        private var mPageSize: Int = 20
    }

    constructor(builder: Builder<T>) {
        this.mIAdapter = builder.mIAdapter
        this.mIReloadData = builder.mIReloadData
        this.mAdapter = builder.mAdapter
        this.mRecyclerView = builder.mRecyclerView
        this.mFooterView = builder.mFooterView
        this.mHeaderView = builder.mHeaderView
        this.isPullDownRefresh = builder.isPullDownRefresh
        this.isPullUpRefresh = builder.isPullUpRefresh
        builder.mSwipeRefreshLayout?.let { this.mSwipeRefreshLayout = it }
        mDatas = ArrayList()


    }

    override fun onStart() {

    }

    override fun onLoadMore() {
        MyLog.e("===", "onLoadMore ")
        mAdapter.showLoadMoreView(true)
        var layoutManager=mRecyclerView.layoutManager
        when(layoutManager){
            is GridLayoutManager->{
                var position:Int= mDatas.size
                if(mHeaderView!=null){
                    position+=1
                }
                var manager=layoutManager
                manager.scrollToPositionWithOffset(position,0)
                manager.reverseLayout=false
            }
            is LinearLayoutManager->{
                var position:Int= mDatas.size
                if(mHeaderView!=null){
                    position+=1
                }
                var manager=layoutManager
                manager.scrollToPositionWithOffset(position,0)
                manager.stackFromEnd=true
            }
            else->{

            }
        }
        mIReloadData.reLoadData()

    }

    override fun onFinish() {
        setLoadingMore(false)
        mAdapter.showLoadMoreView(false)

    }

    fun reloadData() {
        //第一次加载数据
        mIReloadData.reLoadData()
    }

    fun getNextPage(): Int = mCurrentPage++

    fun getPageSize(): Int = mPageSize


    fun setDatas(datas: ArrayList<T>?) {
        if (mCurrentPage == 1 || mCurrentPage == 0) {
            this.mDatas.clear()
        }
        //不是null 就添加
        datas?.let { this.mDatas.addAll(it) }
        mAdapter.setDatas(this.mDatas)
        //隐藏下拉转圈
        mSwipeRefreshLayout?.isRefreshing = false

        mAdapter.notifyDataSetChanged()

        onFinish()
    }

    fun getDatas(): ArrayList<T> = mAdapter.getDatas()

    fun notifyDataSetChanged() {
        mAdapter.notifyDataSetChanged()

    }

    fun notifyItemChanged(position: Int) {
        if (mHeaderView != null)
            mAdapter.notifyItemChanged(position + 1)
        else
            mAdapter.notifyItemChanged(position)
    }

    fun notifyItemChanged(data: T) {
        mDatas.forEachIndexed { index, t ->
            if (data == t) {
                if (mHeaderView != null)
                    mAdapter.notifyItemChanged(index + 1)
                else
                    mAdapter.notifyItemChanged(index)
                return
            }
        }
    }

    fun notifyItemRemoved(position: Int) {
        var tempPosition: Int = position
        if (mHeaderView != null) {
            tempPosition += 1
        }

        var t1: T? = null
        for ((index, value) in mDatas.withIndex()) {
            if (index == tempPosition) {
                t1 = value
                break
            }
        }
        mDatas = mAdapter.getDatas()
        t1?.let { mDatas.remove(t1) }

        mAdapter.notifyItemRemoved(tempPosition)
        //刷新后面的itemV 数据
        mAdapter.notifyItemRangeChanged(tempPosition, mDatas.size - tempPosition)

    }

    fun notifyItemRangeRemoved(positionStart: Int, itemCOunt: Int) {
        if (mHeaderView != null)
            mAdapter.notifyItemRangeRemoved(positionStart + 1, itemCOunt)
        else
            mAdapter.notifyItemRangeRemoved(positionStart, itemCOunt)

    }

    fun notifyItemInserted(position: Int,data: T) {
        var tempPosition: Int = position
        if (mHeaderView != null)
            tempPosition += 1

        mDatas = mAdapter.getDatas()
        //添加到集合再刷新
        mDatas.add(position,data)
        mAdapter.notifyItemInserted(tempPosition)
        mAdapter.notifyItemRangeChanged(tempPosition,mDatas.size-tempPosition)
    }

    fun notifyItemInserted(data: T) {
        //添加到集合再刷新
        mDatas = mAdapter.getDatas()
        mDatas.add(data)
        mAdapter.notifyItemChanged(mDatas.size-1)
    }

    fun notifyItemRangeInserted(positionStart: Int, itemCOunt: Int) {
        if (mHeaderView != null)
            mAdapter.notifyItemRangeInserted(positionStart + 1, itemCOunt)
        else
            mAdapter.notifyItemRangeInserted(positionStart, itemCOunt)
    }

    fun notifyItemChanged(position: Int, data: T?) {
        if (mHeaderView != null)
            mAdapter.notifyItemChanged(position + 1, data)
        else
            mAdapter.notifyItemChanged(position, data)
    }

    fun notifyItemRangeChanged(position: Int, itemCOunt: Int, data: T?) {
        if (mHeaderView != null)
            mAdapter.notifyItemRangeChanged(position + 1, itemCOunt, data)
        else
            mAdapter.notifyItemRangeChanged(position, itemCOunt, data)
    }


    class Builder<T>(context: Context, iAdapter: RIAdapter<T>, iReloadData: IReloadData) {
        lateinit var mRecyclerView: RecyclerView
        lateinit var mAdapter: RecyclerViewAdapter<T>
        /**
         * 头部itemView
         */
        var mHeaderView: View? = null
        /**
         * 底部itemView
         */
        var mFooterView: View? = null


        var mIAdapter: RIAdapter<T> = iAdapter
        var mIReloadData: IReloadData = iReloadData
        var mSwipeRefreshLayout: SwipeRefreshLayout? = null
        var isPullUpRefresh: Boolean = true
        var isPullDownRefresh: Boolean = true
        var context: Context? = context
        var mSpanCount: Int = 0
        var orientation: Int = LinearLayoutManager.VERTICAL
        //true 是瀑布流布局
        var isStaggered = false


        fun recyclerView(swipeRefreshLayout: SwipeRefreshLayout? = null, recyclerView: RecyclerView, spanCount: Int = 0, orientation: Int = LinearLayoutManager.VERTICAL, isStaggered: Boolean = false): Builder<T> {
            this.mSwipeRefreshLayout = swipeRefreshLayout!!
            this.mRecyclerView = recyclerView
            this.mSpanCount = spanCount
            this.orientation = orientation
            this.isStaggered = isStaggered
            initlayoutManager()
            return this
        }

        fun addHeaderView(headerView: View?) {
            this.mHeaderView = headerView


        }

        fun addFooterView(footerView: View?) {
            this.mFooterView = footerView

        }


        fun onPullRefresh(isPullDownRefresh: Boolean, isPullUpRefresh: Boolean): Builder<T> {
            this.isPullDownRefresh = isPullDownRefresh
            this.isPullUpRefresh = isPullUpRefresh
            return this
        }


        fun build(): RecyclerViewDelegate<T> {
            mAdapter = RecyclerViewAdapter(mIAdapter)
            //添加头部和底部的布局
            mHeaderView?.let { mAdapter.setHeaderView(it) }
            mFooterView?.let { mAdapter.setFooterView(it) }

            mSwipeRefreshLayout?.let { initSwipeRefreshLayout() }
            mSwipeRefreshLayout?.let { initLoadMoreView() }

            var recyclerViewDelegate = RecyclerViewDelegate(this)
            initRecyclerView(recyclerViewDelegate)

            return recyclerViewDelegate
        }

        //设置RecyclerView滚动监听
        private fun initRecyclerView(recyclerViewDelegate: RecyclerViewDelegate<T>) {
            mRecyclerView.addOnScrollListener(recyclerViewDelegate)
            mRecyclerView.adapter = mAdapter
        }

        private fun initlayoutManager() {
            if (mSpanCount == 0 || mSpanCount == 1) {
                mRecyclerView.layoutManager = LinearLayoutManager(context, orientation, false)
            } else {
                if (isStaggered) {
                    mRecyclerView.layoutManager = StaggeredGridLayoutManager(mSpanCount, orientation)
                } else {
                    mRecyclerView.layoutManager = GridLayoutManager(context, mSpanCount, orientation, false)
                }
            }


        }

        private fun initSwipeRefreshLayout() {
            //设置下拉圆圈的大小，两个值 LARGE， DEFAULT
            mSwipeRefreshLayout?.setSize(SwipeRefreshLayout.DEFAULT)
            // 设置下拉圆圈上的颜色，红色、绿色、蓝色、黄色
            mSwipeRefreshLayout?.setColorSchemeResources(
                    R.color.color_red,
                    R.color.color_green,
                    R.color.color_blue,
                    R.color.color_yellow)
            //可以下来
            if (isPullDownRefresh) {
                mSwipeRefreshLayout?.setOnRefreshListener {
                    //下拉刷新请求回到第一页数据
                    mCurrentPage = 0
                    MyLog.e("===", "RefreshListener  $mCurrentPage")
                    //回调重新加载数据的方法
                    mIReloadData.reLoadData()

                }
            } else {
                mSwipeRefreshLayout?.isEnabled = false
            }

        }

        private fun initLoadMoreView() {
            //上拉加载更多
            if (isPullUpRefresh) {
                var loadMoreView = LoadMoreView(context!!)
                var params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                loadMoreView.layoutParams = params
                mAdapter.setLoadMoreView(loadMoreView)
                if (mRecyclerView.layoutManager is GridLayoutManager) {
                    //设置网格布局底部加载更多的View 宽带占用mSpanCount列
                    (mRecyclerView.layoutManager as GridLayoutManager).spanSizeLookup = object : SpanSizeLookup() {

                        override fun getSpanSize(p0: Int): Int {
                            when (mAdapter.getItemViewType(p0)) {
                                RecyclerViewAdapter.LOADMORE_VIEW_TYPE -> return 1
                            }
                            return mSpanCount
                        }
                    }

                }

            }

        }

    }


}