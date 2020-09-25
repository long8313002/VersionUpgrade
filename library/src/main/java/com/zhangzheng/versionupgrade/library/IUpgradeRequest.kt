package com.zhangzheng.versionupgrade.library

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class IUpgradeRequest {

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

    internal suspend fun isNeedUpdate() = withContext(Dispatchers.IO) {
        requestIsNeedUpdate()
    }

    internal suspend fun isForceUpdate() = withContext(Dispatchers.IO) {
        requestIsForceUpdate()
    }

    internal suspend fun getUpdateUrl() = withContext(Dispatchers.IO) {
        requestUpdateUrl()
    }

    internal suspend fun getUpdateMessage() = withContext(Dispatchers.IO){
        requestUpdateMessage()
    }

}