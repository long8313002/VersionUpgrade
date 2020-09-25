package com.zhangzheng.versionupgrade.library

import android.content.Context
import java.io.File

interface IUpgradeInstall{
    /**
     * 安装APK
     */
    fun install(context: Context,file: File)
}