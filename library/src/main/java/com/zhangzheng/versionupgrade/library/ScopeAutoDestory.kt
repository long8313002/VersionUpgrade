package com.zhangzheng.versionupgrade.library

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel

internal class ScopeAutoDestory(var context: Context, var uiScope: CoroutineScope) :
    Application.ActivityLifecycleCallbacks {

    fun autoDestory() {
        if (context !is Activity) {
            return
        }

        (context.applicationContext as Application).registerActivityLifecycleCallbacks(this)
    }

    override fun onActivityDestroyed(p0: Activity) {
        if (p0 != context) {
            return
        }
        uiScope.cancel()
        (context.applicationContext as Application).unregisterActivityLifecycleCallbacks(this)
    }

    override fun onActivityPaused(p0: Activity) = Unit

    override fun onActivityStarted(p0: Activity) = Unit

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) = Unit

    override fun onActivityStopped(p0: Activity) = Unit

    override fun onActivityCreated(p0: Activity, p1: Bundle?) = Unit

    override fun onActivityResumed(p0: Activity) = Unit
}