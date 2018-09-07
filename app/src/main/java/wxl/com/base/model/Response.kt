package wxl.com.base.model


data class Response<T>(
    val errorCode: Int,
    val errorMsg: String,
    val result: T,
    val success: Boolean
)