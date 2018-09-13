package wxl.com.base.recycler

import android.content.Context
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.GridLayoutManager.SpanSizeLookup
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import wxl.com.base.R

/**
 * @date Created time 2018/9/10 17:35
 * @author wuxiulin
 * @description 实现RecyclerView列表的封装
 */
class RecyclerViewDelegate<T> {
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
    private var mSwipeRefreshLayout: SwipeRefreshLayout?=null
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

    fun getNextPage(): Int = mCurrentPage++

    fun getPageSize(): Int = mPageSize


    fun setDatas(datas: ArrayList<T>?) {
        if (mCurrentPage == 1) {
            this.mDatas.clear()
        }
        //不是null 就添加
        datas?.let { this.mDatas.addAll(it) }
        mAdapter.setDatas(this.mDatas)
        //隐藏下拉转圈
        mSwipeRefreshLayout?.isRefreshing=false

    }

    fun getDatas(): ArrayList<T> = mDatas


    class Builder<T> {
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


        var mIAdapter: RIAdapter<T>
        var mIReloadData: IReloadData
        var mSwipeRefreshLayout: SwipeRefreshLayout? = null
        var isPullUpRefresh: Boolean = true
        var isPullDownRefresh: Boolean = true
        var context: Context? = null
        var mSpanCount: Int = 0
        var orientation: Int = LinearLayoutManager.VERTICAL
        //true 是瀑布流布局
        var isStaggered=false

        constructor(iAdapter: RIAdapter<T>, iReloadData: IReloadData) {
            this.mIAdapter = iAdapter
            this.mIReloadData = iReloadData

        }

        fun with(context: Context, iAdapter: RIAdapter<T>, iReloadData: IReloadData): Builder<T> {
            this.context = context
            return Builder(iAdapter, iReloadData)
        }

        fun recyclerView(swipeRefreshLayout: SwipeRefreshLayout? = null, recyclerView: RecyclerView, spanCount: Int = 0, orientation: Int = LinearLayoutManager.VERTICAL,isStaggered:Boolean=false): Builder<T> {
            this.mSwipeRefreshLayout = swipeRefreshLayout!!
            this.mRecyclerView = recyclerView
            this.mSpanCount = spanCount
            this.orientation=orientation
            this.isStaggered=isStaggered
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

            return RecyclerViewDelegate(this)
        }

        private fun initlayoutManager() {
            if (mSpanCount == 0 || mSpanCount == 1) {
                mRecyclerView.layoutManager = LinearLayoutManager(context,orientation,false)
            }else{
                if(isStaggered){
                    mRecyclerView.layoutManager = StaggeredGridLayoutManager(mSpanCount,orientation)
                }else{
                    mRecyclerView.layoutManager = GridLayoutManager(context,mSpanCount,orientation,false)
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
                if (mRecyclerView.layoutManager is StaggeredGridLayoutManager) {
                    //设置网格布局底部加载更多的View 宽带占用mSpanCount列
//                    (mRecyclerView.layoutManager as StaggeredGridLayoutManager).spanSizeLookup = object : SpanSizeLookup() {
//
//                        override fun getSpanSize(p0: Int): Int {
//                            when (mAdapter.getItemViewType(p0)) {
//                                RecyclerViewAdapter.LOADMORE_VIEW_TYPE -> return 1
//                            }
//                            return mSpanCount
//                        }
//                    }

//                }


            }

        }

    }


}