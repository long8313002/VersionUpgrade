package com.zhangzheng.versionupgrade.library

import android.content.Context
import com.zhangzheng.versionupgrade.library.impl.*
import kotlinx.coroutines.*

class VersionUpgrade(
    var upgradeRequest: IUpgradeRequest = UpgradeRequest(),
    var upgradeView: IUpgradeView = UpgradeView(),
    var upgradeDown: IUpgradeDown = UpgradeDown(),
    var upgradeInstall: IUpgradeInstall = UpgradeInstall(),
    var upgradeControl: IUpgradeControl = UpgradeControl(),
    var uiScope: CoroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

) {

    fun requestUpdate(context: Context) {
        if (!upgradeControl.allowUpdate()) {
            return
        }

        ScopeAutoDestory(context, uiScope).autoDestory()
        uiScope.launch {
            if (upgradeRequest.isNeedUpdate()) {
                upgradeView.showDialog(
                    context,
                    upgradeRequest.isForceUpdate(),
                    upgradeRequest.requestUpdateMessage()
                )
                upgradeView.setUpdateListener {
                    uiScope.launch {
                        upgradeControl.notifyStartUpdate()
                        val file = upgradeDown.downUrl(context, upgradeRequest.getUpdateUrl())
                        upgradeControl.notifyUpdateDownSuccess()
                        if (file != null) {
                            upgradeInstall.install(context, file)
                        }
                    }
                }
                upgradeView.setDismissListener {
                    upgradeControl.notifyUpdateDismiss()
                }

            }
        }
    }

    fun needUpdate(callBack: (isNeed: Boolean) -> Unit) {
        uiScope.launch {
            callBack(upgradeRequest.isNeedUpdate())
        }
    }

}