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

    fun <T> rx(flowable: Flowable<T>, listener: OnSubscriberListener<T>,statusListener: NetStatusListener?=null): Disposable {
        return flowable.onBackpressureDrop()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(QuietSubscriber<T>(listener,statusListener))
    }


    fun <T> rx(dialogImp: IFDialog?, flowable: Flowable<T>, listener: OnSubscriberListener<T>,statusListener: NetStatusListener?=null): Disposable {
        return flowable.onBackpressureDrop()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(ProgressSubscriber<T>(dialogImp, listener,statusListener))
    }

    fun <T> rxUpdata(flowable: Flowable<T>, updataListener: OnUpdataListener<T>): Disposable {
        return flowable.onBackpressureDrop()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(UpdataSubscriber<T>(updataListener))
    }

}