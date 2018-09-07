package wxl.com.base.netapi

import io.reactivex.Flowable
import retrofit2.http.*
import wxl.com.base.model.Response

interface HttpService {

    /**
     * 获取产品展示列表 搜索查询
     */
    @GET("company/prod/listAll")
    fun getprodSearchList(
            @Query("page.currentPage") currentPage: Int,
            @Query("page.pageSize") pageSize: Int,
            @Query("goodsName") goodsName: String
    ): Flowable<Any>

    /**
     * 获取订单的物流记录
     */
    @FormUrlEncoded
    @POST("logisticsOrder/record")
    fun getLogisticsOrderRecord(
            @Field("id") id: Int
    ): Flowable<Response<Any>>

    /**
     * 获取商品的二维码
     * @param goodsId
     * @param scene
     * @return
     */
    @FormUrlEncoded
    @POST("goods/getGoodsQrcode")
    fun getGoodsQrCode(
            @Field("goodsId") goodsId: Int,
            @Field("scene") scene: Int
    ): Flowable<Response<String>>
}