package wxl.com.base.subscriber
/**
 * @date Created time 2018/9/6 16:52
 * @author wuxiulin
 * @description 包含错误的回调接口
 */
interface OnNextOnError<T> :OnNext<T>{
    fun onError(t:T)
}