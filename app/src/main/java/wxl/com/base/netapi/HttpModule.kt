package wxl.com.base.netapi

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.orhanobut.logger.Logger
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import wxl.com.base.BuildConfig
import java.util.concurrent.TimeUnit


object HttpModule {
    private val TOKEN = "Shop-Token"
    private val VERSION="Shop-Version"


    fun provideHttpService(): HttpService {
        return provideRetrofit(Api.BASE_URL, provideOkHttpClient(true))
                .create(HttpService::class.java)
    }

    fun provideUpdataService():UpdataService{
        return provideRetrofit(Api.BASE_URL_UPDATE, provideOkHttpClient(false))
                .create(UpdataService::class.java)
    }


    private fun provideRetrofit(url: String, client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .baseUrl(url)
                .client(client)
                //gson 格式转换工厂
                .addConverterFactory(GsonConverterFactory.create())
                //解析rxjava的适配器
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

    }

    private fun provideOkHttpClient(isAddHeader:Boolean): OkHttpClient {
        var builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            //添加网络数据打印
            builder.addInterceptor(providePrinterIntercaptor())
            //添加Chrome网络数据拦截
            builder.addNetworkInterceptor(StethoInterceptor())
        }
        if(isAddHeader){
            return builder.retryOnConnectionFailure(true)
                    .connectTimeout(5, TimeUnit.SECONDS)
                    .addNetworkInterceptor(provideInterceptor())
                    .build()
        }else{
            return builder.retryOnConnectionFailure(true)
                    .connectTimeout(5, TimeUnit.SECONDS)
                    .build()
        }



    }

    /**
     * okHttpClient 使用拦截器 打印请求数据
     */
    private fun providePrinterIntercaptor(): Interceptor {
        return Interceptor { chain ->
            //打印url
            Logger.d(chain.request().newBuilder().build().url())
            var response = chain.proceed(chain.request())
            var body = response.body()
            //打印网络请求的数据
            if (body != null) {
                var contentType = body.contentType()
                var stringBody = body.string()
                var responseBody = ResponseBody.create(contentType, stringBody)
                response = response.newBuilder().body(responseBody).build()

                Logger.d(stringBody)
            }

            response
        }
    }

    /**
     * okHttpClient 使用拦截器 添加请求头
     */
    private fun provideInterceptor(): Interceptor {
        return Interceptor { chain ->
            //添加到头部

            //TOKEN 不为空时
            var request = chain.request()
                    .newBuilder()
                    .header(TOKEN, "628cd58d7cb14612ba1b1f8dad8820e0")
                    .header(VERSION, "1.4.1")
                    .build()


            chain.proceed(request)
        }
    }


}