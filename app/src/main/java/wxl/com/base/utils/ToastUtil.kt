package wxl.com.base.utils
import android.text.TextUtils
import android.widget.Toast
import android.widget.Toast.makeText
import com.google.gson.JsonParseException
import wxl.com.base.App
import wxl.com.base.subscriber.NotSuccessException
import java.net.SocketException
import java.net.UnknownHostException

object ToastUtil {
    var toast: Toast? = null
    fun show( message: String?) {

        if (TextUtils.isEmpty(message)) {
            return
        }
        if (toast == null) {
            toast = makeText(App.getContext(), message, Toast.LENGTH_SHORT)
        } else {
            toast!!.setText(message)
        }
        toast!!.show()

    }

    fun show( t: Throwable) {
        if (t is SocketException) {
            show("网络错误：网络连接失败")
        } else if (t is UnknownHostException) {
            show("网络错误：找不到网址")
        } else if (t is JsonParseException) {
            show("数据解析异常")
        } else if (t is NotSuccessException) {
            show( t.message)
        } else {
            show(t.message)
        }

    }


}