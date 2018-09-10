package wxl.com.base.subscriber

import io.reactivex.subscribers.ResourceSubscriber
import wxl.com.base.utils.MyLog
/**
 * @date Created time 2018/9/7 17:09
 * @author wuxiulin
 * @description 统一
 */
open class QuietSubscriber<T>(var listener: OnSubscriberListener<T>?):ResourceSubscriber<T>() {

    override fun onStart() {
        //请求一次数据
        request(1)
    }

    override fun onComplete() {
        listener?.onComplete()

    }

    override fun onNext(t: T) {
        MyLog.e("===","onNext "+t.toString())


        listener?.onNext(t)

    }

    override fun onError(t: Throwable?) {
        MyLog.e("===", "onError "+t?.message!!)
        listener?.onError(t)
    }

}