package wxl.com.base.recycler

import android.content.Context
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import wxl.com.base.R
import wxl.com.base.utils.MyLog
import wxl.com.base.utils.ToastUtil


/**
 * @date Created time 2018/9/10 17:35
 * @author wuxiulin
 * @description 实现RecyclerView列表的封装
 */
class RecyclerViewDelegate<T> : OnRecyclerViewScrollListener {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: RecyclerViewAdapter<T>
    private var loadStatus: LoadMoreView.LoadStatus = LoadMoreView.LoadStatus.CLOSE_VIEW
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
    //true 没有更多数据， false 还有下一页数据
    private var isNoMore = false

    private var mCurrentPage: Int = 0
    private var mPageSize: Int = 20
    private lateinit var builder:Builder<T>


    constructor(builder: Builder<T>) {
        this.builder=builder
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

        mSwipeRefreshLayout?.let { initSwipeRefreshLayout() }
        mSwipeRefreshLayout?.let { initLoadMoreView() }
        mRecyclerView.adapter = mAdapter

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
                //不可上拉加载更多
                setLoadingMore(true)
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
            var loadMoreView = LoadMoreView(builder.context!!)
            var params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            loadMoreView.layoutParams = params
            mAdapter.setLoadMoreView(loadMoreView)
            if (mRecyclerView.layoutManager is GridLayoutManager) {
                //设置网格布局底部加载更多的View 宽带占用mSpanCount列
                (mRecyclerView.layoutManager as GridLayoutManager).spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {

                    override fun getSpanSize(p0: Int): Int {
                        when (mAdapter.getItemViewType(p0)) {
                            RecyclerViewAdapter.LOADMORE_VIEW_TYPE -> return builder.mSpanCount
                            RecyclerViewAdapter.HEADER_VIEW_TYPE -> return builder.mSpanCount
                            RecyclerViewAdapter.FOOTER_VIEW_TYPE -> return builder.mSpanCount
                        }
                        return 1
                    }
                }

            }

        }

    }

    override fun onStart() {

    }

    override fun onLoadMore() {
        mAdapter.setLoadStatus(loadStatus)
        setLoadingMore(false)
        var layoutManager = mRecyclerView.layoutManager
        //上拉加载更多时 显示加载更多布局
        when (layoutManager) {
            is GridLayoutManager -> {
                var position: Int = mDatas.size
                if (mHeaderView != null) {
                    position += 1
                }
                var manager = layoutManager
                manager.scrollToPositionWithOffset(position, 0)
                manager.reverseLayout = false
            }
            is LinearLayoutManager -> {
                var position: Int = mDatas.size
                if (mHeaderView != null) {
                    position += 1
                }
                var manager = layoutManager
                manager.scrollToPositionWithOffset(position, 0)
            }
            is StaggeredGridLayoutManager -> {
                var position: Int = mDatas.size
                if (mHeaderView != null) {
                    position += 1
                }
                var manager = layoutManager
                manager.scrollToPositionWithOffset(position, 0)
            }
        }

        mIReloadData.reLoadData()

    }

    override fun onFinish() {
        setLoadingMore(false)
        if (mCurrentPage >=  1) {
            //加载更多时，根据数据显示相应的状态
            mAdapter.setLoadStatus(loadStatus)
            loadStatus = LoadMoreView.LoadStatus.HAS_MORE
        }

    }

    /**
     * 网络错误时调用此方法关闭网络请求的状态
     */
    fun onError() {
        if (mCurrentPage == 1 || mCurrentPage == 0) {
            //隐藏下拉转圈
            mSwipeRefreshLayout?.isRefreshing = false
        } else {
            loadStatus = LoadMoreView.LoadStatus.CLOSE_VIEW
            onFinish()
        }
        mCurrentPage--
    }


    fun reloadData() {
        //第一次加载数据
        mIReloadData.reLoadData()
    }

    fun getNextPage(): Int = ++mCurrentPage

    fun getPageSize(): Int = mPageSize

    fun isPullUpRefresh() = isPullUpRefresh

    fun setDatas(datas: ArrayList<T>?) {
        if (mCurrentPage == 1 || mCurrentPage == 0) {
            this.mDatas.clear()
        } else {
            if (datas == null || datas.size == 0) {
                mCurrentPage--
            }
        }
        //加载更多时，根据数据显示相应的状态
        if (datas == null) {
            loadStatus = LoadMoreView.LoadStatus.NOT_MORE
        }
        datas?.let {
            loadStatus = if (it.size == 0) {
                LoadMoreView.LoadStatus.NOT_MORE
            } else {
                LoadMoreView.LoadStatus.CLOSE_VIEW
            }
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

    fun notifyItemChanged(oldData: T, newData: T) {
        mDatas.forEachIndexed { index, t ->
            if (oldData == t) {
                mDatas[index] = newData

                if (mHeaderView != null)
                    mAdapter.notifyItemChanged(index + 1, newData)
                else
                    mAdapter.notifyItemChanged(index, newData)
                return
            }
        }
    }

    fun notifyItemChanged(position: Int, data: T) {
        var tempPosition: Int = position
        mDatas[tempPosition] = data

        if (mHeaderView != null)
            tempPosition += 1

        mAdapter.notifyItemChanged(tempPosition, data)


    }

    fun notifyItemRemoved(position: Int) {
        var tempPosition: Int = position
        var t1: T? = null
        for ((index, value) in mDatas.withIndex()) {
            if (index == tempPosition) {
                t1 = value
                break
            }
        }
        t1?.let { mDatas.remove(t1) }

        if (mHeaderView != null) {
            tempPosition += 1
        }
        mAdapter.notifyItemRemoved(tempPosition)
        //刷新后面的itemV 数据
        mAdapter.notifyItemRangeChanged(tempPosition, mDatas.size - position)

    }

    fun notifyItemRangeRemoved(positionStart: Int, itemCount: Int) {
        var tempPositionStart = positionStart
        if (tempPositionStart + itemCount > mDatas.size) {
            ToastUtil.show(IndexOutOfBoundsException("IndexOutOfBoundsException: index: ${tempPositionStart + itemCount} , Size: ${mDatas.size}"))
            return
        }
        //从集合里移除元素
        for (i in (tempPositionStart until tempPositionStart + itemCount).reversed()) {
            mDatas.remove(mDatas[i])

        }

        if (mHeaderView != null)
            tempPositionStart += 1

        mAdapter.notifyItemRangeRemoved(tempPositionStart, itemCount)
        //刷新后面的itemV 数据
        mAdapter.notifyItemRangeChanged(tempPositionStart, mDatas.size - positionStart)
    }

    fun notifyItemInserted(position: Int, data: T) {
        var tempPosition: Int = position
        if (mHeaderView != null)
            tempPosition += 1

        mDatas = mAdapter.getDatas()
        //添加到集合再刷新
        mDatas.add(position, data)
        mAdapter.notifyItemInserted(tempPosition)
        mAdapter.notifyItemRangeChanged(tempPosition, mDatas.size - tempPosition)
    }

    fun notifyItemInserted(data: T) {
        //添加到集合再刷新
        mDatas = mAdapter.getDatas()
        mDatas.add(data)
        mAdapter.notifyItemChanged(mDatas.size - 1)
    }

    fun notifyItemRangeInserted(positionStart: Int, itemCOunt: Int) {
        if (mHeaderView != null)
            mAdapter.notifyItemRangeInserted(positionStart + 1, itemCOunt)
        else
            mAdapter.notifyItemRangeInserted(positionStart, itemCOunt)
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

        /**
         * 适配器回调
         */
        var mIAdapter: RIAdapter<T> = iAdapter
        /**
         * 加载数据回调
         */
        var mIReloadData: IReloadData = iReloadData
        var mSwipeRefreshLayout: SwipeRefreshLayout? = null
        /**
         * 上拉加载更多
         */
        var isPullUpRefresh: Boolean = true
        /**
         * 下拉刷新
         */
        var isPullDownRefresh: Boolean = true
        var context: Context? = context
        /**
         * 列数
         */
        var mSpanCount: Int = 0
        /**
         * 排列方式： 垂直或水平
         */
        var orientation: Int = LinearLayoutManager.VERTICAL
        /**
         * true 是瀑布流布局  false 反之
         */
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

        fun addHeaderView(headerView: View?): Builder<T> {
            this.mHeaderView = headerView
            return this

        }

        fun addFooterView(footerView: View?): Builder<T> {
            this.mFooterView = footerView
            return this
        }

        /**
         * 设置是否可以 上拉加载更多和下拉刷新
         */
        fun setOnPullRefresh(isPullDownRefresh: Boolean = true, isPullUpRefresh: Boolean = true): Builder<T> {
            this.isPullDownRefresh = isPullDownRefresh
            this.isPullUpRefresh = isPullUpRefresh
            return this
        }

        /**
         * 添加分割线
         */
        fun addItemDecoration(divider: RecyclerView.ItemDecoration): Builder<T> {
            mRecyclerView.addItemDecoration(divider)
            return this
        }

        /**
         * 创建RecyclerViewDelegate对象
         */
        fun build(): RecyclerViewDelegate<T> {
            mAdapter = RecyclerViewAdapter(mIAdapter)
            //添加头部和底部的布局
            mHeaderView?.let { mAdapter.setHeaderView(it) }
            mFooterView?.let { mAdapter.setFooterView(it) }


            var recyclerViewDelegate = RecyclerViewDelegate(this)
            if (isPullUpRefresh) {
                initRecyclerView(recyclerViewDelegate)
            }
            return recyclerViewDelegate
        }

        //设置RecyclerView滚动监听
        private fun initRecyclerView(recyclerViewDelegate: RecyclerViewDelegate<T>) {
            mSwipeRefreshLayout?.let { recyclerViewDelegate.setSwipeRefreshLayout(it) }
            mRecyclerView.addOnScrollListener(recyclerViewDelegate)

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


    }


}