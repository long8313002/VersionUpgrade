package com.zhangzheng.versionupgrade

import com.zhangzheng.versionupgrade.library.impl.UpgradeRequest

class TestUpgradeRequest :UpgradeRequest() {
    override fun url()="http://qa-yapi.amh-group.com/mock/481/columbus-app/advert/banner/workbench"

    override fun isNeedUpdateKey()="isNeedUpdate"

    override fun isForceUpdateKey()="isForceUpdate"

    override fun updateUrlKey()="url"
}