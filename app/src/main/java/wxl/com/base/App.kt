package wxl.com.base

import android.app.Application
import com.facebook.stetho.Stetho

class App:Application() {
    override fun onCreate() {
        super.onCreate()
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