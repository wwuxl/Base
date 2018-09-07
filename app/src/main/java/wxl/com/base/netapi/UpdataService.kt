package wxl.com.base.netapi


import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Path
import wxl.com.base.model.VersionInfo

interface UpdataService {


    @GET("{fileUrl}")
    fun getVersionInfo(
            @Path("fileUrl") fileUrl: String

    ): Flowable<VersionInfo>

}