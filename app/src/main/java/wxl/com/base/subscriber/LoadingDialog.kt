package wxl.com.base.subscriber

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.app.Dialog
import android.content.Context
import android.os.Handler
import android.os.Message
import android.widget.ImageView
import kotlinx.android.synthetic.main.view_dialog_loading.*
import wxl.com.base.R

/**
 * @date Created time 2018/9/10 17:31
 * @author wuxiulin
 * @description 网络正在加载时的dialog
 */
class LoadingDialog @JvmOverloads constructor(context: Context,themeResId:Int=R.style.dialog_loading):Dialog(context, themeResId) {
    private lateinit var anims:ArrayList<ObjectAnimator>
    init {
        initView()
    }

    private fun initView() {
        setContentView(R.layout.view_dialog_loading)
        show()
        anims=ArrayList()

        var handler=MyHandler()
        var msg1=Message.obtain()
        msg1.obj=image1
        handler.sendMessageDelayed(msg1,0)
        var msg2=Message.obtain()
        msg2.obj=image2
        handler.sendMessageDelayed(msg2,300)
        var msg3=Message.obtain()
        msg3.obj=image3
        handler.sendMessageDelayed(msg3,600)

       setOnDismissListener {
           //停止动画
           anims.forEach {
               it.end()
           }
       }

    }


    private fun startAnimator(imageView: ImageView){
        val holder1 = PropertyValuesHolder.ofFloat("scaleX", 1f, 0.4f, 1f)
        val holder2 = PropertyValuesHolder.ofFloat("scaleY", 1f, 0.4f, 1f)
        var animator=ObjectAnimator.ofPropertyValuesHolder(imageView,holder1,holder2)
        animator.repeatCount=-1
        animator.repeatMode=ObjectAnimator.RESTART
        animator.duration = 800
        animator.start()
        anims.add(animator)

    }

    inner class MyHandler : Handler(){
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            if(msg?.obj is ImageView){
                startAnimator(msg?.obj as ImageView)
            }

        }

    }
}