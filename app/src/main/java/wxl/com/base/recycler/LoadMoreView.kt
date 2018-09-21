package wxl.com.base.recycler

import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import wxl.com.base.R
import wxl.com.base.databinding.ViewLoadMoreBinding
import wxl.com.base.utils.MyLog
import wxl.com.base.utils.UIUtil

class LoadMoreView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : LinearLayout(context, attrs, defStyleAttr) {
    private var moreBinding: ViewLoadMoreBinding? = null
    private var handler: MyHandler

    init {
        initView()
        handler = MyHandler()
    }

    private fun initView() {
        moreBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.view_load_more, this, false)
        addView(moreBinding?.root)

    }

    fun setLoadingMoreStatus(loadStatus: LoadStatus) {
        show()
        when (loadStatus) {
            LoadStatus.NOT_MORE -> {
                moreBinding?.progressBar?.visibility = View.GONE
                moreBinding?.statusText?.text = resources.getText(R.string.no_load_more)
            }
            LoadStatus.HAS_MORE -> {
                moreBinding?.progressBar?.visibility = View.VISIBLE
                moreBinding?.statusText?.text = resources.getText(R.string.load_more)
            }
            LoadStatus.PULL_DOWN -> {
                moreBinding?.progressBar?.visibility = View.GONE
                moreBinding?.statusText?.text = resources.getText(R.string.pull_down)

            }

        }
        var msg = Message.obtain()
        msg.obj = loadStatus
        handler.sendMessageDelayed(msg, 1000)
    }

    fun hide() {
        MyLog.e("===", "hide")
        layoutParams.height = 0

    }

    fun show() {
        MyLog.e("===", "show")
        layoutParams.height = UIUtil.dip2px(50)

    }

    enum class LoadStatus {
        NOT_MORE,//没有更多数据
        HAS_MORE,//有下一页
        PULL_DOWN//请下拉刷新
    }


    inner class MyHandler : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when (msg?.obj) {
                LoadStatus.NOT_MORE -> {
                    hide()
                }
                LoadStatus.PULL_DOWN -> {
                    hide()
                }
            }

        }
    }

}