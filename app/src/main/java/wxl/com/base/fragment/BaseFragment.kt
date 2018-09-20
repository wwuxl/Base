package wxl.com.base.fragment

import android.support.v4.app.Fragment
import io.reactivex.disposables.CompositeDisposable
import wxl.com.base.subscriber.IFDialog
import wxl.com.base.subscriber.LoadingDialog
/**
 * @date Created time 2018/9/19 10:52
 * @author wuxiulin
 * @description Fragment基类
 */
open class BaseFragment : Fragment(), IFDialog {
    private var loadingDialog: LoadingDialog?=null
    var mDisposable = CompositeDisposable()


    override fun showLoadingDialog() {
        loadingDialog = LoadingDialog(activity!!)
        loadingDialog?.show()
    }

    override fun dismissLoadingDialog() {
        loadingDialog?.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        dismissLoadingDialog()
        mDisposable.clear()
    }
}