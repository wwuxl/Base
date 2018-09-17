package wxl.com.base.utils

import android.content.res.Resources
import android.os.Handler
import wxl.com.base.App

object UIUtil {

    fun getResources(): Resources = App.getContext().resources

    fun dip2px(dip:Int ):Int{
        //密度
        var density= getResources().displayMetrics.density
        return (dip*density+0.5f).toInt()
    }

    fun px2dip(px:Int):Int{
        //密度
        var density= getResources().displayMetrics.density
        return (px/density+0.5f).toInt()
    }

    fun px2sp(pxValue:Float):Int{
        var density= getResources().displayMetrics.scaledDensity
        return (pxValue/density+0.5f).toInt()
    }

    fun sp2px(spValue: Float):Int{
        var density= getResources().displayMetrics.scaledDensity
        return (spValue/density+0.5f).toInt()
    }

    fun getScreenWidth():Int{
        return getResources().displayMetrics.widthPixels
    }

    fun getScreenHeight():Int{
        return getResources().displayMetrics.heightPixels
    }

    /**
     * 得到颜色 - use Resource by id
     * @param id
     * @return
     */
    fun getColor(id: Int): Int {
        return getResources().getColor(id)
    }

    /**
     * 把Runnable 提交到主线程
     *
     * @param runnable
     */
    fun runOnUiThread(runnable: Runnable) {
        // 在主线程运行
        if (android.os.Process.myTid().toLong() == Thread.currentThread().id) {
            runnable.run()
        } else {
            //获取handler
            getHandler().post(runnable)
        }
    }

    fun getHandler(): Handler {
        return App.getHandler()
    }

}