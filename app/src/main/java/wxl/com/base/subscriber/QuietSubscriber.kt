package wxl.com.base.subscriber

import io.reactivex.subscribers.ResourceSubscriber
import wxl.com.base.utils.MyLog
/**
 * @date Created time 2018/9/7 17:09
 * @author wuxiulin
 * @description 统一
 */
open class QuietSubscriber<T>:ResourceSubscriber<T>() {

    override fun onStart() {
        //请求一次数据
        request(1)
    }

    override fun onComplete() {

    }

    override fun onNext(t: T) {
        MyLog.e("===","onNext "+t.toString())

    }

    override fun onError(t: Throwable?) {
        MyLog.e("===", "onError "+t?.message!!)
    }

}