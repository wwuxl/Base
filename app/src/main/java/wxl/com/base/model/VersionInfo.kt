package wxl.com.base.model


data class VersionInfo(
    val vsCode: Int,
    val vsName: String,
    //0正常更新  1 强制更新  2 强制停止使用
    val compelStatus: Int,
    //最后强制更新的版本号(判读当前版本是否需要强制更新)
    val lastVsCompelCode: Int,
    val apkName: String,
    val apkUrl: String,
    val upContent: String
)