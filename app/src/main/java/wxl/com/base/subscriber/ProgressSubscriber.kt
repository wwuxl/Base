package wxl.com.base.subscriber

/**
 * @date Created time 2018/9/7 17:07
 * @author wuxiulin
 * @description 带正在加载网络弹窗的订阅者
 */
class ProgressSubscriber<T>(var dialogImp: IFDialog?): QuietSubscriber<T>() {

    override fun onStart() {
        super.onStart()
        dialogImp?.showLoadingDialog()

    }

    override fun onComplete() {
        dialogImp?.dismissDialog()
    }

}