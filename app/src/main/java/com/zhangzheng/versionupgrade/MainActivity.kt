package com.zhangzheng.versionupgrade

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zhangzheng.versionupgrade.library.VersionUpgrade

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        VersionUpgrade(TestUpgradeRequest()).requestUpdate(this)
    }
}
