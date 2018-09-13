package wxl.com.base.recycler

import android.content.Context
import android.databinding.DataBindingUtil
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import wxl.com.base.R
import wxl.com.base.databinding.ViewLoadMoreBinding

class LoadMoreView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : LinearLayout(context, attrs, defStyleAttr) {
    init{
        initView()

    }

    private fun initView() {
        var binding=DataBindingUtil.inflate<ViewLoadMoreBinding>(LayoutInflater.from(context), R.layout.view_load_more,this,false)
        addView(binding.root)

    }

}