package wxl.com.base.subscriber
/**
 * @date Created time 2018/9/6 16:52
 * @author wuxiulin
 * @description 包含成功数据的回调
 */
interface OnNext<T> {
    fun  onNext(t:T)
}