package wxl.com.base.subscriber

interface OnNextOnErrorUnMatch<T>:OnNextOnError<T> {
    //数据不匹配
    fun onUnMatch(t:T)
}