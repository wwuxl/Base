package wxl.com.base.netapi

object HttpManager {

    fun getHttpService(): HttpService {
        return HttpModule.provideHttpService()
    }

    fun getUpdaService():UpdataService{
        return HttpModule.provideUpdataService()
    }

}