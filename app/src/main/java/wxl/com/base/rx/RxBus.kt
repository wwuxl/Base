package wxl.com.base.rx

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.processors.FlowableProcessor
import io.reactivex.processors.PublishProcessor
import io.reactivex.schedulers.Schedulers
import wxl.com.base.utils.MyLog

/**
 * @date Created time 2018/9/19 14:25
 * @author wuxiulin
 * @description 事件总线工具类
 */
object RxBus {

    private var mBus: FlowableProcessor<Any>? = null
    init {
        MyLog.e("===","RxBus  init")
        mBus =PublishProcessor.create<Any>().toSerialized()
    }

    fun post(any: Any){
        mBus?.onNext(any)
    }

    fun <T> register(clazz: Class<T>,consumer: Consumer<T>):Disposable{
        return mBus?.filter {
            it.javaClass == clazz
        }!!.map { o -> o as T  }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer)

    }

}