package com.zhangzheng.versionupgrade.library

import android.content.Context
import java.io.File

interface IUpgradeInstall{
    fun install(context: Context,file: File)
}