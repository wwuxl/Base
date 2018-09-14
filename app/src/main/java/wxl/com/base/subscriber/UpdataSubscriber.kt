package wxl.com.base.subscriber

import io.reactivex.subscribers.ResourceSubscriber
/**
 * @date Created time 2018/9/14 11:16
 * @author wuxiulin
 * @description 更新apk使用
 */
class UpdataSubscriber<T>(var listener: OnUpdataListener<T>) : ResourceSubscriber<T>() {

    override fun onComplete() {
        listener.onComplete()
    }

    override fun onNext(t: T) {
        listener.onNext(t)
    }

    override fun onError(t: Throwable?) {
        listener.onError(t)

    }
}