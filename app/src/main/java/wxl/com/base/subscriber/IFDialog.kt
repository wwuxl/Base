package wxl.com.base.subscriber
/**
 * @date Created time 2018/9/19 14:22
 * @author wuxiulin
 * @description 网络正在请求的dialog接口
 */
interface IFDialog {
    /**
     * 显示正在加载的dialog
     */
    fun showLoadingDialog()

    /**
     * 网络请求结束时， 关闭正在加载的dialog
     */
    fun dismissLoadingDialog()

}