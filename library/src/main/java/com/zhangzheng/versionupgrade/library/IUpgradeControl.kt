package com.zhangzheng.versionupgrade.library

interface IUpgradeControl{

    fun allowUpdate():Boolean

    fun notifyUpdateDismiss()

    fun notifyStartUpdate()

    fun notifyUpdateDownSuccess()

}