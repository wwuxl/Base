package wxl.com.base.recycler

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import android.view.ViewGroup
import wxl.com.base.utils.UIUtil


class RecyclerViewAdapter<T> : RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private var datas: ArrayList<T>
    private var adapterImpl: RIAdapter<T>
    private var headerView: View? = null
    private var footerView: View? = null
    private var loadMoreView: View? = null

    companion object {
        var HEADER_VIEW_TYPE = 100 shl 2
        var LOADMORE_VIEW_TYPE = 101 shl 2
        var FOOTER_VIEW_TYPE = 102 shl 2
    }

    constructor(adapter: RIAdapter<T>) : super() {
        this.adapterImpl = adapter
        datas = ArrayList()

    }

    fun setDatas(datas: ArrayList<T>) {
        this.datas = datas
        notifyDataSetChanged()
    }

    fun getDatas(): ArrayList<T> = this.datas

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        //判断是否是瀑布流布局
        if (isStaggeredGridLayout(holder)) {
            handleLayoutIfStaggeredGridLayout(holder)
        }
    }

    private fun isStaggeredGridLayout(holder: RecyclerView.ViewHolder): Boolean {
        val layoutParams = holder.itemView.layoutParams
        return layoutParams != null && layoutParams is StaggeredGridLayoutManager.LayoutParams
    }

    private fun handleLayoutIfStaggeredGridLayout(holder: RecyclerView.ViewHolder) {
        //瀑布流布局 headerView 和 footerView 和 loadMoreView 宽度填满屏幕
        when (holder.itemViewType) {
            LOADMORE_VIEW_TYPE -> {
                val params = holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
                params.isFullSpan = true
            }
            HEADER_VIEW_TYPE -> {
                val params = holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
                params.isFullSpan = true
            }
            FOOTER_VIEW_TYPE -> {
                val params = holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
                params.isFullSpan = true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            HEADER_VIEW_TYPE -> HFLViewHolder(headerView!!)

            FOOTER_VIEW_TYPE -> HFLViewHolder(footerView!!)

            LOADMORE_VIEW_TYPE -> HFLViewHolder(loadMoreView!!)

            else -> RViewHolder(adapterImpl.onCreateView(parent, viewType))
        }
    }

    override fun getItemCount(): Int {
        return datas.size + getHeaderViewCount() + getFooterViewCount() + getLoadMoreViewCount()
    }

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, position: Int) {

        //回调给调用者
        when (getItemViewType(position)) {
            HEADER_VIEW_TYPE -> {
            }
            FOOTER_VIEW_TYPE -> {
            }
            LOADMORE_VIEW_TYPE -> {

            }
            else -> {
                val viewHolder = p0 as RecyclerViewAdapter<*>.RViewHolder
                adapterImpl.onBindView(datas[position - getHeaderViewCount()], viewHolder.binding!!, position - getHeaderViewCount())
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        if (headerView != null && position == 0) {
            return HEADER_VIEW_TYPE
        }
        if (footerView != null && itemCount - getLoadMoreViewCount() - 1 == position) {
            return FOOTER_VIEW_TYPE
        }
        if (loadMoreView != null && itemCount - 1 == position) {
            return LOADMORE_VIEW_TYPE
        }
        return super.getItemViewType(position)
    }

    /**
     * 设置头部的布局
     */
    fun setHeaderView(headerView: View) {
        this.headerView = headerView
    }

    /**
     * 设置底部的布局
     */
    fun setFooterView(footerView: View) {
        this.footerView = footerView

    }

    fun showLoadMoreView(isShow: Boolean) {
        loadMoreView?.let {
            //重绘loadMoreView的大小
            setVisibility(isShow, it)
        }


    }

    /**
     * 隐藏和显示ItemView
     *
     * @param isVisible
     * @param view
     */
    private fun setVisibility(isVisible: Boolean, view: View) {
        var param = view.layoutParams
        if (isVisible) {
            param.height = UIUtil.dip2px(50)
            param.width = UIUtil.getScreenWidth()
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.GONE
            param.height = 0
            param.width = 0
        }
        view.layoutParams = param
    }

    /**
     * 设置上拉加载更多的布局
     */
    fun setLoadMoreView(loadMoreView: View) {
        this.loadMoreView = loadMoreView
    }

    private fun getHeaderViewCount(): Int {
        return if (headerView == null) 0 else 1
    }

    private fun getFooterViewCount(): Int {
        return if (footerView == null) 0 else 1
    }

    private fun getLoadMoreViewCount(): Int {
        return if (loadMoreView == null) 0 else 1
    }


    inner class RViewHolder : RecyclerView.ViewHolder {
        var binding: ViewDataBinding? = null

        constructor(itemBinding: ViewDataBinding) : super(itemBinding.root) {
            binding = itemBinding
        }


    }

    inner class HFLViewHolder : RecyclerView.ViewHolder {
        constructor(itemView: View) : super(itemView) {
            when (itemView) {
                loadMoreView -> {
                setVisibility(false,loadMoreView!!)
                }
            }
        }
    }

}