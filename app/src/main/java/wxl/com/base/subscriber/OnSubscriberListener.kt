package wxl.com.base.subscriber
/**
 * @date Created time 2018/9/10 10:10
 * @author wuxiulin
 * @description  网络请求接口回调监听
 */
interface OnSubscriberListener<T> {
    /**
     * 成功回调
     */
    fun onNext(t:T)
    /**
     * 错误回调
     */
    fun onError(t:Throwable){

    }

    /**
     * 参数数据或返回结果不匹配 回调
     */
    fun onUnmatch(t:T){

    }

    /**
     *  结束回调（在请求成功回调后）
     */
    fun onComplete(){

    }

}