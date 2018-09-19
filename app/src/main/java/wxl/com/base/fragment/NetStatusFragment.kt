package wxl.com.base.fragment

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import wxl.com.base.R
import wxl.com.base.databinding.FragmentNetStatusBinding
import wxl.com.base.subscriber.NetStatusListener
import wxl.com.base.view.NetStatusLayout

/**
 * @date Created time 2018/9/19 10:51
 * @author wuxiulin
 * @description 带网络请求结果状态的Fragment基类
 */
abstract class NetStatusFragment:BaseFragment(), NetStatusLayout.StatusListener,NetStatusListener {
    private lateinit var mBinding:FragmentNetStatusBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding=DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.fragment_net_status,container,false)
        mBinding.statusLayout.setContentView(initSucceedContentView())

        return mBinding.root
    }

    override fun onLoading() {
        mBinding.statusLayout.showStatus(NetStatusLayout.NetStatus.STATUS_LOADING)
    }

    override fun onEmpty() {
        mBinding.statusLayout.showStatus(NetStatusLayout.NetStatus.STATUS_EMPTY)
    }

    override fun onError() {
        mBinding.statusLayout.showStatus(NetStatusLayout.NetStatus.STATUS_ERROR)
    }

    override fun onSucceed() {
        mBinding.statusLayout.showStatus(NetStatusLayout.NetStatus.STATUS_SUCCEED)
    }


    abstract fun initSucceedContentView(): View?

}