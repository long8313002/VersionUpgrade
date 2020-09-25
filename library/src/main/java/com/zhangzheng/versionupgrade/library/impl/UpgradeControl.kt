package com.zhangzheng.versionupgrade.library.impl

import com.zhangzheng.versionupgrade.library.IUpgradeControl

open class UpgradeControl :IUpgradeControl {

    override fun allowUpdate() = true

    override fun notifyUpdateDismiss() {
    }

    override fun notifyStartUpdate() {
    }

    override fun notifyUpdateDownSuccess() {
    }
}