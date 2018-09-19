package wxl.com.base.subscriber
/**
 * @date Created time 2018/9/19 14:23
 * @author wuxiulin
 * @description 网络状态回调接口
 */
interface NetStatusListener {
    fun onEmpty()
    fun onError()
    fun onSucceed()
    fun onLoading()

}