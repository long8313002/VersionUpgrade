package com.zhangzheng.versionupgrade.library

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class IUpgradeRequest {

    abstract suspend fun requestIsNeedUpdate(): Boolean

    abstract suspend fun requestIsForceUpdate(): Boolean

    abstract suspend fun requestUpdateUrl(): String

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