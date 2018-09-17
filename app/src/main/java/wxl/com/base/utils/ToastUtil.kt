package wxl.com.base.utils

import android.content.Context
import android.text.TextUtils
import android.widget.Toast
import android.widget.Toast.makeText
import com.google.gson.JsonParseException
import wxl.com.base.subscriber.NotSuccessException
import java.net.SocketException
import java.net.UnknownHostException

object ToastUtil {
    var toast: Toast? = null
    fun show(context: Context, message: String?) {
        if (context == null) {
            return
        }
        if (TextUtils.isEmpty(message)) {
            return
        }
        if (toast == null) {
            toast = makeText(context, message, Toast.LENGTH_SHORT)
        } else {
            toast!!.setText(message)
        }
        toast!!.show()

    }

    fun show(context: Context, t: Throwable) {
        if (t is SocketException) {
            show(context, "网络错误：网络连接失败")
        } else if (t is UnknownHostException) {
            show(context, "网络错误：找不到网址")
        } else if (t is JsonParseException) {
            show(context, "数据解析异常")
        } else if (t is NotSuccessException) {
            show(context, t.message)
        } else {
            show(context, "网络错误:" + t.message)
        }

    }


}