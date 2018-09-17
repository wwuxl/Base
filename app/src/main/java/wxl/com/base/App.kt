package wxl.com.base

import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import com.facebook.stetho.Stetho

class App:Application() {

    companion object {
        lateinit var mContext:Context
        private lateinit var mHandler: Handler
        fun getContext():Context{
            return mContext
        }

        fun getHandler(): Handler {
            synchronized(App::class.java) {
                if (mHandler == null) {
                    mHandler = Handler(Looper.getMainLooper())
                }
                return mHandler
            }

        }
    }

    override fun onCreate() {
        super.onCreate()
        mContext=this
        init()
    }

    private fun init() {
        if (BuildConfig.DEBUG) {
            Stetho.initialize(Stetho.newInitializerBuilder(this)
                    .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                    .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                    .build())
        }
    }
}