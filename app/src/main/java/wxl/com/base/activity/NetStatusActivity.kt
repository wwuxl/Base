package wxl.com.base.activity

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import wxl.com.base.R
import wxl.com.base.databinding.ActivityNetStatusBinding
import wxl.com.base.subscriber.NetStatusListener
import wxl.com.base.view.NetStatusLayout
/**
 * @date Created time 2018/9/7 11:46
 * @author wuxiulin
 * @description 含有网络请求结果状态的Activity基类
 */
abstract class NetStatusActivity : BaseActivity(), NetStatusLayout.StatusListener,NetStatusListener {
    private lateinit var mStatusBinding: ActivityNetStatusBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mStatusBinding.statusLayout.setStatusListener(this)
        mStatusBinding.statusLayout.setContentView(initSucceedContentView())
    }


    fun setDataStatus(status: NetStatusLayout.NetStatus) {
        mStatusBinding.statusLayout.showStatus(status)

    }

    override fun initContentView(): View? {
        mStatusBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_net_status, null, false)
        return mStatusBinding.root
    }
    override fun onLoading() {
        mStatusBinding.statusLayout.showStatus(NetStatusLayout.NetStatus.STATUS_LOADING)
    }

    override fun onEmpty() {
        mStatusBinding.statusLayout.showStatus(NetStatusLayout.NetStatus.STATUS_EMPTY)
    }

    override fun onError() {
        mStatusBinding.statusLayout.showStatus(NetStatusLayout.NetStatus.STATUS_ERROR)
    }

    override fun onSucceed() {
        mStatusBinding.statusLayout.showStatus(NetStatusLayout.NetStatus.STATUS_SUCCEED)
    }


    abstract fun initSucceedContentView():View?

}