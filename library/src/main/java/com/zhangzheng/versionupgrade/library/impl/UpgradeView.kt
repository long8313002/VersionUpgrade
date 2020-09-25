package com.zhangzheng.versionupgrade.library.impl

import android.app.AlertDialog
import android.content.Context
import com.zhangzheng.versionupgrade.library.IUpgradeView

open class UpgradeView : IUpgradeView {

    private var successListener = {}
    private var failListener = {}

    override fun showDialog(context: Context, forceUpdate: Boolean, message: String) {
        val builder = AlertDialog.Builder(context)
            .setTitle("升级提示")
            .setMessage(message)

        builder.setPositiveButton("升级"){ _, _ -> successListener()}
        if (!forceUpdate) {
            builder.setNegativeButton("取消") { dialog, _ -> dialog.dismiss()}
        }

        val dialog = builder.create()

        dialog.setCanceledOnTouchOutside(!forceUpdate)

        dialog.setOnDismissListener { failListener() }

        dialog.show()
    }


    override fun setUpdateListener(callBack: () -> Unit) {
        successListener = callBack
    }

    override fun setDismissListener(callBack: () -> Unit) {
        failListener = callBack
    }
}