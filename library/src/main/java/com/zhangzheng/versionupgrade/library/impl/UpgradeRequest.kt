package com.zhangzheng.versionupgrade.library.impl

import com.zhangzheng.versionupgrade.library.IUpgradeRequest

open class UpgradeRequest :IUpgradeRequest() {
    override suspend fun requestIsNeedUpdate(): Boolean {
        return true
    }

    override suspend fun requestIsForceUpdate(): Boolean {
        return true
    }

    override suspend fun requestUpdateUrl(): String {
        return "https://devimage.ymm56.com/ymmfile/columbus-pub/ANDROID_ROAM_DRIVER_TEST_1.12.0.apk"
    }

    override suspend fun requestUpdateMessage(): String {
        return "欢迎更新"
    }
}