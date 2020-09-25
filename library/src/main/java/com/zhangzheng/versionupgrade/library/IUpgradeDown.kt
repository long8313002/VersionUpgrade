package com.zhangzheng.versionupgrade.library

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

abstract class IUpgradeDown {

    abstract fun requestDownUrl(context: Context,url: String): File?

    internal suspend fun downUrl(context: Context,url: String) = withContext(Dispatchers.IO) {
        requestDownUrl(context,url)
    }

}