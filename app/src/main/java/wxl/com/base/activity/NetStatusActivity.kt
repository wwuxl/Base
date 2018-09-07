package wxl.com.base.activity

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import io.reactivex.disposables.CompositeDisposable
import wxl.com.base.R
import wxl.com.base.databinding.ActivityNetStatusBinding
import wxl.com.base.view.NetStatusLayout

abstract class NetStatusActivity : BaseActivity(), NetStatusLayout.StatusListener {
    private lateinit var mStatusBinding: ActivityNetStatusBinding
    var mDisposable = CompositeDisposable()

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

    abstract fun initSucceedContentView():View?

    override fun onDestroy() {
        super.onDestroy()
        mDisposable.clear()
    }
}