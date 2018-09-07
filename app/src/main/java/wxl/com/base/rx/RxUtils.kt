package wxl.com.base.rx

import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import wxl.com.base.subscriber.*

class RxUtils {
    companion object {
        fun <T> rx(flowable:Flowable<T>): Disposable{
            return flowable.onBackpressureDrop()
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(QuietSubscriber<T>())
        }

        
        fun <T> rx(dialogImp:IFDialog,flowable: Flowable<T>):Disposable{
            return flowable.onBackpressureDrop()
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(ProgressSubscriber<T>(dialogImp))
        }

        fun <T> rxUpdata(flowable: Flowable<T>,updataListener: OnUpdataListener<T>):Disposable{
            return flowable.onBackpressureDrop()
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(UpdataSubscriber<T>(updataListener))
        }

    }


}