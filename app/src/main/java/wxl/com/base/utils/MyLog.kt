package wxl.com.base.utils

import android.util.Log
import wxl.com.base.BuildConfig

class MyLog {
    companion object {
        fun e(tag:String,msg:String){
            if(BuildConfig.DEBUG){
                Log.e(tag,msg)
            }
        }

        fun e(tag: String,msg: String,tr:Throwable){
            if(BuildConfig.DEBUG){
                Log.e(tag, msg,tr)
            }
        }
        fun d(tag: String,msg: String){
            if(BuildConfig.DEBUG){
                Log.d(tag, msg)
            }
        }
        fun d(tag: String,msg: String,tr:Throwable){
            if(BuildConfig.DEBUG){
                Log.d(tag, msg,tr)
            }
        }
        fun i(tag: String,msg: String){
            if(BuildConfig.DEBUG){
                Log.i(tag, msg)
            }
        }

        fun i(tag: String,msg: String,tr:Throwable){
            if(BuildConfig.DEBUG){
                Log.i(tag, msg,tr)
            }
        }
        fun w(tag: String,msg: String){
            if(BuildConfig.DEBUG){
                Log.w(tag, msg)
            }
        }
        fun w(tag: String,msg: String,tr:Throwable){
            if(BuildConfig.DEBUG){
                Log.w(tag, msg,tr)
            }
        }
        fun v(tag: String,msg: String){
            if(BuildConfig.DEBUG){
                Log.v(tag, msg)
            }
        }
        fun v(tag: String,msg: String,tr:Throwable){
            if(BuildConfig.DEBUG){
                Log.v(tag, msg,tr)
            }
        }
    }
}