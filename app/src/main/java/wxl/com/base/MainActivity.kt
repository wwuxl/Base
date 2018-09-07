package wxl.com.base

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import wxl.com.base.activity.NetStatusActivity
import wxl.com.base.databinding.ActivityMainBinding
import wxl.com.base.databinding.ViewTitleRightBinding
import wxl.com.base.model.VersionInfo
import wxl.com.base.netapi.HttpManager
import wxl.com.base.rx.RxUtils
import wxl.com.base.subscriber.OnUpdataListener
import wxl.com.base.utils.MyLog
import wxl.com.base.view.NetStatusLayout

class MainActivity : NetStatusActivity() {


    private lateinit var mBinding: ActivityMainBinding

    override fun initSucceedContentView(): View? {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_main, null, false)
        return mBinding.root
    }

    override fun isHasTitle(): Boolean {
        return false
    }

    override fun initTitle(): String? {
        return "首页"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initData()
        initRightView()

    }

    private fun initRightView() {
        var rightBinding: ViewTitleRightBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.view_title_right, null, false)
        addTitleRightView(rightBinding.root)
    }

    private fun initData() {
        setDataStatus(NetStatusLayout.NetStatus.STATUS_ERROR)


    }

    override fun reloadData() {
        Log.e("===", "重新加载")
        //mDisposable.add(RxUtils.rx(HttpManager.getHttpService().getGoodsQrCode(57468, 57468)))
        mDisposable.add(RxUtils.rxUpdata(HttpManager.getUpdaService().getVersionInfo("seller_version.json"),object :OnUpdataListener<VersionInfo>{
            override fun onError(t: Throwable?) {
                MyLog.e("===","onError "+t.toString())
            }

            override fun onComplete() {
                MyLog.e("===","onComplete")
            }

            override fun onNext(t: VersionInfo) {
                MyLog.e("===","onNext "+t.upContent)
            }

        }))

    }

}
