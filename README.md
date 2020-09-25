有样式地址：https://blog.csdn.net/long8313002/article/details/108799854


概述
         最近又需要做一个apk升级功能，这个功能基本上每新做一个app就需要重写一遍，为了方便后续的使用，现对升级功能进行封装。因为面向不是单一应用，所以需要保持高抽象、高扩展性，以适配所有的app。

 

配置
 

build.gradle

 implementation 'com.zhangzheng.versionupgrade:library:1.0.0'
 

AndroidMainfest.xml

 <provider
            android:name="androidx.core.content.FileProvider"
            android:authorities="com.zhangzheng.versionupgrade.fileprovider"
            android:exported="false"
            android:grantUriPermissions="true">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/filepath" />
        </provider>
       因为要升级库中，提供了默认的安装apk功能，需要需要使用这部分功能的化（可以自己重新实现），需要在Mainfest中配置provider来配置权限。

android:authorities 为： （你的包名+fileprovider）

 

简单使用
          如果只是想简单的使用一个升级功能，只需要提供拉取版本信息的接口，升级库中提供了UpgradeRequest抽象类用于继承。你只需要提供对应的KEY，它会自己进行处理，具体使用示例如下：

 

示例
class TestUpgradeRequest :UpgradeRequest() {
 
    /**
     * 配置URL
     */
    override fun url()="http://qa-yapi.amh-group.com/mock/481/columbus-app/advert/banner/workbench"
 
    /**
     * 是否有新版本需要更新，在json文件中定义的key
     */
    override fun isNeedUpdateKey()="isNeedUpdate"
 
    /**
     * 是否强制更新，在json文件中定义的key
     */
    override fun isForceUpdateKey()="isForceUpdate"
 
    /**
     * 升级apk的下载地址
     */
    override fun updateUrlKey()="url"
}
 

使用

VersionUpgrade(TestUpgradeRequest()).requestUpdate(this)
 

高度扩展
        这里进行了五个纬度的抽象，来方便进行扩展。为了简化使用，每个都提供了默认实现。如果无法满足需求，需要自行进行扩展，以下逐个说明：

  VersionUpgrade(
            upgradeRequest = TODO(),
            upgradeView = TODO(),
            upgradeControl = TODO(),
            upgradeDown = TODO(),
            upgradeInstall = TODO(),
            uiScope = TODO()
        ).requestUpdate(this)
 

IUpgradeRequest
负责管理网络请求相关，需要关注：是否需要升级、是否强制升级、升级apkUrl、升级文案

  /**
     * 是否有新版本
     */
    abstract suspend fun requestIsNeedUpdate(): Boolean
 
    /**
     * 是否是强制升级
     */
    abstract suspend fun requestIsForceUpdate(): Boolean
 
    /**
     * 升级apk的url
     */
    abstract suspend fun requestUpdateUrl(): String
 
    /**
     * 升级文案
     */
    abstract suspend fun requestUpdateMessage():String
提供的默认实现为UpgradeRequest，可以直接继承它来进行扩展，如果变动比较大需要实现IUpgradeRequest

 

IUpgradeControl
      控制是否升级的本地策略，有时候需要做到当前拒绝后不在显示升级弹窗：

interface IUpgradeControl{
 
    /**
     * 是否允许升级，主要用来处理拒绝升级后，多久内无法再次触发升级
     */
    fun allowUpdate():Boolean
 
    /**
     * 升级被取消提醒到
     */
    fun notifyUpdateDismiss()
 
    /**
     * 点击确定升级
     */
    fun notifyStartUpdate()
 
    /**
     * 升级包下载完成
     */
    fun notifyUpdateDownSuccess()
 
}
默认实现是不进行限制

 

IUpgradeView
     负责管理升级弹窗，关注：弹窗样式、“拒绝升级”和“同意升级”事件通知出去

interface IUpgradeView {
 
    fun showDialog(context: Context, forceUpdate: Boolean, message: String)
 
    fun setUpdateListener(callBack: () -> Unit)
 
    fun setDismissListener(callBack: () -> Unit)
 
}
 

默认实现：UpgradeView

 

 

IUpgradeDown
    负责apk的下载、关注点：重复触发逻辑、下载成功、下载失败、已经下载过逻辑

abstract class IUpgradeDown {
 
    /**
     * 请求下载URL，需要同步处理
     */
    abstract fun requestDownUrl(context: Context,url: String): File?
 
 
}
 

默认实现：UpgradeDown，使用的是系统的DownloadManager来进行下载，可以基于UpgradeDown来进行扩展，提供重载函数：

    /**
     * 正在下载
     */
    protected open fun onDownloading(context: Context) {
        Toast.makeText(context, "正在下载中......", Toast.LENGTH_SHORT).show()
    }
 
    /**
     * 下载失败
     */
    protected open fun onDownLoadFail(context: Context) {
        Toast.makeText(context, "下载失败", Toast.LENGTH_SHORT).show()
    }
 
    /**
     * 下载成功
     */
    protected open fun onDownLoadSuccess(context: Context) {
 
    }
 
    /**
     * 存储下载过apk路径
     */
    protected open fun apkFilePath(context: Context) = File(
        context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        , apkName(context)
    )
 
    /**
     * apk名称
     */
    protected open fun apkName(context: Context) =
        if (apkName.isNotEmpty()) {
            apkName
        } else getAppName(context) + "_" + getVerName(context) + ".apk"
 

IUpgradeInstall
负责安装逻辑

interface IUpgradeInstall{
    /**
     * 安装APK
     */
    fun install(context: Context,file: File)
}
 

提供默认实现：UpgradeInstall，一般这部分不需要扩展。

 

 

最后
       感谢您的阅读，如果觉得对你有帮助，希望可以关注+点赞+收藏      github地址：https://github.com/long8313002/VersionUpgrade
