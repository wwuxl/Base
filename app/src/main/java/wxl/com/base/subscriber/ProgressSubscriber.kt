package wxl.com.base.subscriber

/**
 * @date Created time 2018/9/7 17:07
 * @author wuxiulin
 * @description 带正在加载网络弹窗的订阅者
 */
class ProgressSubscriber<T>(var dialogImp: IFDialog?, listener: OnSubscriberListener<T>?,statusListener: NetStatusListener?)
    : QuietSubscriber<T>(listener,statusListener) {

    override fun onStart() {
        super.onStart()
        dialogImp?.showLoadingDialog()

    }

    override fun onError(t: Throwable) {
        super.onError(t)
        dialogImp?.dismissLoadingDialog()
    }

    override fun onComplete() {
        super.onComplete()
        dialogImp?.dismissLoadingDialog()
    }

}