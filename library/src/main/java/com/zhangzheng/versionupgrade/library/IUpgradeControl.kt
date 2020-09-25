package com.zhangzheng.versionupgrade.library

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