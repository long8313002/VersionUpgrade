package com.zhangzheng.versionupgrade.library.impl

import com.zhangzheng.versionupgrade.library.IUpgradeRequest
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets

abstract class UpgradeRequest : IUpgradeRequest() {

    protected open var needUpdateValue: Any? = null
    protected open var isForceUpdateValue: Any? = null
    protected open var updateUrl: String? = null

    override suspend fun requestIsNeedUpdate(): Boolean {
        awaitRequest()
        return needUpdateValue is Boolean && needUpdateValue as Boolean
    }

    override suspend fun requestIsForceUpdate(): Boolean {
        awaitRequest()
        return isForceUpdateValue is Boolean && isForceUpdateValue as Boolean
    }

    override suspend fun requestUpdateUrl(): String {
        awaitRequest()
        return updateUrl ?: ""
    }

    override suspend fun requestUpdateMessage(): String {
        awaitRequest()
        return "欢迎更新"
    }


    private var hasRequest = false

    private fun awaitRequest() {
        if(hasRequest){
            return
        }
        val jsonString = sendGetRequest(url())
        parseJson(JSONObject(jsonString))
        hasRequest = true
    }

    private fun parseJson(json:JSONObject){
        val keys = json.keys()
        keys.forEach {
            try{
                val newJson = json.getJSONObject(it)
                if(newJson.length()>0){
                    parseJson(newJson)
                    return
                }
            }catch (e:Exception){

            }

            when (it) {
                isNeedUpdateKey() -> {
                    needUpdateValue = json.get(it)
                }
                isForceUpdateKey() -> {
                    isForceUpdateValue = json.get(it)
                }
                updateUrlKey() -> {
                    updateUrl = json.getString(it)
                }
            }
        }
    }

    protected abstract fun url(): String

    protected abstract fun isNeedUpdateKey(): String

    protected abstract fun isForceUpdateKey(): String

    protected abstract fun updateUrlKey(): String


    private fun sendGetRequest(urlStr: String): String {
        val url = URL(urlStr)
        val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
        conn.requestMethod = "GET"
        if (conn.responseCode == 200) {
            val result = ByteArrayOutputStream()
            val buffer = ByteArray(1024)
            var length: Int
            while (conn.inputStream.read(buffer).also { length = it } != -1) {
                result.write(buffer, 0, length)
            }
            return result.toString(StandardCharsets.UTF_8.name())
        }
        return ""
    }
}