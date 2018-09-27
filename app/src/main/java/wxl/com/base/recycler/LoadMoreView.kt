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
/**
 * @date Created time 2018/9/26 11:29
 * @author wuxiulin
 * @description 加载更多的itemView
 */
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
        var msg = Message.obtain()
        msg.obj = loadStatus
        when (loadStatus) {
            LoadStatus.NOT_MORE -> {
                moreBinding?.progressBar?.visibility = View.GONE
                moreBinding?.statusText?.text = resources.getText(R.string.no_load_more)
                //需要延时隐藏
            }
            LoadStatus.HAS_MORE -> {
                moreBinding?.progressBar?.visibility = View.VISIBLE
                moreBinding?.statusText?.text = resources.getText(R.string.load_more)
                show()
                //不需要延时隐藏
                msg=null
            }
            LoadStatus.PULL_DOWN -> {
                moreBinding?.progressBar?.visibility = View.GONE
                moreBinding?.statusText?.text = resources.getText(R.string.pull_down)

            }
            LoadStatus.CLOSE_VIEW->{
                msg=null
                hide()
            }

        }
        //延时隐藏
        msg?.let {
            handler.sendMessageDelayed(it, 1500)
        }
    }

    fun hide() {
        MyLog.e("===", "hide")
        layoutParams.height = 0
        //恢复加载更多
        moreBinding?.progressBar?.visibility = View.VISIBLE
        moreBinding?.statusText?.text = resources.getText(R.string.load_more)
    }

    fun show() {
        MyLog.e("===", "show")
        layoutParams.height = UIUtil.dip2px(50)

    }

    enum class LoadStatus {
        NOT_MORE,//没有更多数据
        HAS_MORE,//有下一页
        PULL_DOWN,//请下拉刷新
        CLOSE_VIEW//隐藏VIEW

    }


    inner class MyHandler : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            hide()
           // MyLog.e("===","$height  ${Thread.currentThread().name}")

        }
    }

}