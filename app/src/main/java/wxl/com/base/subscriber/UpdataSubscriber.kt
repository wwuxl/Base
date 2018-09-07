package wxl.com.base.subscriber

import io.reactivex.subscribers.ResourceSubscriber

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