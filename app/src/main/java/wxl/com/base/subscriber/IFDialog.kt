package wxl.com.base.subscriber

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