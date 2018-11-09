package wxl.com.base.rx

import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import wxl.com.base.subscriber.*
/**
 * @date Created time 2018/9/19 14:26
 * @author wuxiulin
 * @description 网络请求工具类
 */
object RxHttpUtils {


    fun <T> rx( flowable: Flowable<T>, listener: OnSubscriberListener<T>,dialogImp: IFDialog?=null, statusListener: NetStatusListener?=null): Disposable {
        return flowable.onBackpressureDrop()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(if(dialogImp==null) QuietSubscriber(listener,statusListener)else ProgressSubscriber(dialogImp, listener,statusListener))
    }

    fun <T> rxUpdata(flowable: Flowable<T>, updataListener: OnUpdataListener<T>): Disposable {
        return flowable.onBackpressureDrop()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(UpdataSubscriber(updataListener))
    }

}