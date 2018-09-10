package wxl.com.base.recycler

import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * @date Created time 2018/9/10 17:35
 * @author wuxiulin
 * @description 实现RecyclerView列表的封装
 */
class RecyclerViewDelegate {


    class Builder<VH : RecyclerView.ViewHolder>{
        /**
         * 头部itemView
         */
        private lateinit var headView: View
        /**
         * 底部itemView
         */
        private lateinit var footView:View

        private lateinit var adapter:RecyclerView.Adapter<VH>

        fun setAdapter(adapter: RecyclerView.Adapter<VH>){
            this.adapter=adapter
        }
        fun getAdapter():RecyclerView.Adapter<VH>{
            return adapter
        }


    }
}