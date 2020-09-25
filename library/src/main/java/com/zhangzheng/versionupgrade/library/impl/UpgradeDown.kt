package com.zhangzheng.versionupgrade.library.impl

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import com.zhangzheng.versionupgrade.library.IUpgradeDown
import java.io.File
import java.util.concurrent.CountDownLatch

/**
 * apk下载帮助类
 */
private class DownloadHelp(private val context: Context) {
    //下载器
    private val downloadManager: DownloadManager =
        context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    var isDownloading = false

    //下载apk
    fun downloadAPK(url: String, file: File, callback: (isSuccess: Boolean) -> Unit) {
        if (isDownloading) {
            callback(false)
            return
        }

        val downloadId = downloadManager.enqueue(createDownRequest(url, Uri.fromFile(file)))
        isDownloading = true

        //注册广播接收者，监听下载状态
        context.registerReceiver(
            object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    callback(checkStatus(downloadId))
                    context.unregisterReceiver(this)
                    isDownloading = false
                }
            },
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        )
    }

    //检查下载状态
    fun checkStatus(downloadId: Long): Boolean {
        val query = DownloadManager.Query()
        query.setFilterById(downloadId)
        val cursor = downloadManager.query(query)
        if (cursor.moveToFirst()) {
            val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
            cursor.close()
            return when (status) {
                DownloadManager.STATUS_SUCCESSFUL -> {
                    true
                }
                DownloadManager.STATUS_FAILED -> {
                    false
                }
                else -> false
            }
        }
        return false
    }

    private fun createDownRequest(url: String, uri: Uri): DownloadManager.Request {
        //创建下载任务
        val request = DownloadManager.Request(Uri.parse(url))
        //移动网络情况下是否允许漫游
        request.setAllowedOverRoaming(false)
        //在通知栏中显示，默认就是显示的
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
        request.setTitle(getAppName(context))
        request.setDescription("新版本下载中...")
        request.setVisibleInDownloadsUi(true)
        request.setDestinationUri(uri)
        return request
    }


}

private var downloaderHelp: DownloadHelp? = null

open class UpgradeDown(var apkName: String = "") : IUpgradeDown() {

    override fun requestDownUrl(context: Context, url: String): File? {
        if (downloaderHelp == null) {
            downloaderHelp = DownloadHelp(context.applicationContext)
        }
        if (downloaderHelp!!.isDownloading) {
            onDownloading(context)
            return null
        }

        var file: File = apkFilePath(context)
        if(file.exists()){
            return file
        }

        file = File(file.absolutePath+"_temp")
        file.deleteOnExit()

        val latch = CountDownLatch(1)

        var downSuccess = false
        downloaderHelp!!.downloadAPK(url, file) {
            latch.countDown()
            downSuccess = it
        }
        try {
            latch.await()
        } catch (e: Exception) {

        }
        if (downSuccess) {
            file.renameTo(apkFilePath(context))
            onDownLoadSuccess(context)
        } else {
            onDownLoadFail(context)
            file.deleteOnExit()
        }
        return apkFilePath(context)
    }

    protected open fun onDownloading(context: Context) {
        Toast.makeText(context, "正在下载中......", Toast.LENGTH_SHORT).show()
    }

    protected open fun onDownLoadFail(context: Context) {
        Toast.makeText(context, "下载失败", Toast.LENGTH_SHORT).show()
    }

    protected open fun onDownLoadSuccess(context: Context) {

    }

    protected open fun apkFilePath(context: Context) = File(
        context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        , apkName(context)
    )

    protected open fun apkName(context: Context) =
        if (apkName.isNotEmpty()) {
            apkName
        } else getAppName(context) + "_" + getVerName(context) + ".apk"

    private fun getVerName(context: Context) = try {
        context.packageManager.getPackageInfo(context.packageName, 0).versionName
    } catch (e: PackageManager.NameNotFoundException) {
        ""
    }

}

private fun getAppName(context: Context): String {
    try {
        val packageManager = context.packageManager
        val packageInfo = packageManager.getPackageInfo(context.packageName, 0)
        val labelRes = packageInfo.applicationInfo.labelRes
        return context.resources.getString(labelRes)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return ""
}