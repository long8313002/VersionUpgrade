package com.zhangzheng.versionupgrade.library

import android.content.Context

interface IUpgradeView {

    fun showDialog(context: Context, forceUpdate: Boolean, message: String)

    fun setUpdateListener(callBack: () -> Unit)

    fun setDismissListener(callBack: () -> Unit)

}