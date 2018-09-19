package wxl.com.base.subscriber

import io.reactivex.subscribers.ResourceSubscriber
/**
 * @date Created time 2018/9/7 17:09
 * @author wuxiulin
 * @description 统一网络请求
 */
open class QuietSubscriber<T>(var listener: OnSubscriberListener<T>?,var statusListener: NetStatusListener?):ResourceSubscriber<T>() {

    override fun onStart() {
        //请求一次数据
        request(1)
        statusListener?.onLoading()
    }

    override fun onComplete() {
        listener?.onComplete()
        listener?.onEnd()
    }

    override fun onNext(t: T) {

        listener?.onNext(t)
        //做判断数据是否为空
        //statusListener?.onEmpty()
        statusListener?.onSucceed()

    }

    override fun onError(t: Throwable) {
        listener?.onError(t)
        listener?.onEnd()
        //网络错误回调更新界面
        statusListener?.onError()

    }

}