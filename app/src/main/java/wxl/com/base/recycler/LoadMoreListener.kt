package wxl.com.base.recycler
/**
 * @date Created time 2018/9/27 11:43
 * @author wuxiulin
 * @description 加载更多回调
 */
interface LoadMoreListener {
    fun onStart()
    fun onLoadMore()
    fun onFinish()


}