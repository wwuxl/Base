package wxl.com.base.recycler

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup

class RecyclerViewAdapter<T> : RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private var datas: ArrayList<T>
    private var adapterImpl: RIAdapter<T>
    private var headerView: View? = null
    private var footerView: View? = null
    private var loadMoreView: View? = null

    companion object {
        private var HEADER_VIEW_TYPE = 100 shl 2
        var LOADMORE_VIEW_TYPE = 101 shl 2
        private var FOOTER_VIEW_TYPE = 102 shl 2
    }

    constructor(adapter: RIAdapter<T>) : super() {
        this.adapterImpl = adapter
        datas= ArrayList()

    }
    fun setDatas(datas: ArrayList<T>){
        this.datas = datas
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder = RViewHolder(adapterImpl.onCreateView())

    override fun getItemCount(): Int = datas.size + getHeaderViewCount() + getFooterViewCount() + getFooterViewCount()

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
        var viewHolder = p0 as RecyclerViewAdapter<*>.RViewHolder
        //回调给调用者
        adapterImpl.onBindView(datas[p1], viewHolder.binding, p1)
    }

    override fun getItemViewType(position: Int): Int {
        if (headerView != null && position == 0) {
            return HEADER_VIEW_TYPE
        }
        if (footerView != null && itemCount - getLoadMoreView() - 1 == position) {
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

    private fun getLoadMoreView(): Int {
        return if (loadMoreView == null) 0 else 1
    }


    inner class RViewHolder : RecyclerView.ViewHolder {
        var binding: ViewDataBinding

        constructor(itemBinding: ViewDataBinding) : super(itemBinding.root) {
            binding = itemBinding
        }

    }

}