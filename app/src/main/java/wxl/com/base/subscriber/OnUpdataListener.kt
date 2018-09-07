package wxl.com.base.subscriber

interface OnUpdataListener<T> {
    fun onNext(t:T)
    fun onError(t:Throwable?)
    fun onComplete()

}