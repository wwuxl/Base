package wxl.com.base.subscriber

interface NetStatusListener {
    fun onEmpty()
    fun onError()
    fun onSucceed()
    fun onLoading()

}