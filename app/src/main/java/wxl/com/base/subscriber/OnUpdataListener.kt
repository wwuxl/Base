package wxl.com.base.subscriber
/**
 * @date Created time 2018/9/19 14:24
 * @author wuxiulin
 * @description 更新apk专用
 */
interface OnUpdataListener<T> {
    fun onNext(t:T)
    fun onError(t:Throwable?)
    fun onComplete()

}