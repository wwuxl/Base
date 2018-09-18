package wxl.com.base.view

import android.content.Context
import android.databinding.DataBindingUtil
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import wxl.com.base.R
import wxl.com.base.databinding.EmptyViewBinding
import wxl.com.base.databinding.ErrorViewBinding

/**
 * @date Created time 2018/9/5 10:41
 * @author wuxiulin
 * @description 网络请求的显示界面
 */
class NetStatusLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {
    private lateinit var emptyViewBinding: EmptyViewBinding
    private lateinit var errorViewBinding: ErrorViewBinding
    private var listener: StatusListener? = null
    private var netStatus: NetStatus = NetStatus.STATUS_LOADING

    private lateinit var contentView: View

    init {
        initView()
    }

    private fun initView() {
        emptyViewBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.empty_view, this, false)
        errorViewBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.error_view, this, false)
        addView(emptyViewBinding.root)
        addView(errorViewBinding.root)
        emptyViewBinding.root.visibility= View.GONE
        errorViewBinding.root.visibility= View.GONE

        initEvent()

    }

    private fun initEvent() {
        errorViewBinding.btnReload.setOnClickListener {
            if (listener != null) listener!!.errorReloadData()
        }

    }

    fun setContentView(contentView: View?) {
       if (contentView == null) this.contentView = View(context) else this.contentView = contentView
        addView(this.contentView)
        this.contentView.visibility= View.GONE

    }


    fun showStatus(status: NetStatus) {
        netStatus = status
        when (netStatus) {
            NetStatus.STATUS_LOADING -> {
                emptyViewBinding.root.visibility = View.GONE
                errorViewBinding.root.visibility = View.GONE
                contentView.visibility = View.GONE
            }

            NetStatus.STATUS_EMPTY -> {
                emptyViewBinding.root.visibility = View.VISIBLE
                errorViewBinding.root.visibility = View.GONE
                contentView.visibility = View.GONE
            }
            NetStatus.STATUS_ERROR -> {
                emptyViewBinding.root.visibility = View.GONE
                errorViewBinding.root.visibility = View.VISIBLE
                contentView.visibility = View.GONE
            }
            NetStatus.STATUS_SUCCEED -> {
                emptyViewBinding.root.visibility = View.GONE
                errorViewBinding.root.visibility = View.GONE
                contentView.visibility = View.VISIBLE
            }
        }

    }

    fun setStatusListener(listener: StatusListener) {
        this.listener = listener
    }

    interface StatusListener {
        fun errorReloadData()
    }

    enum class NetStatus {
        STATUS_LOADING,
        STATUS_EMPTY,
        STATUS_ERROR,
        STATUS_SUCCEED
    }

}