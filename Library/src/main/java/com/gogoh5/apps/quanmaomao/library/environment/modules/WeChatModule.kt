package com.gogoh5.apps.quanmaomao.library.environment.modules

import android.content.Intent
import android.graphics.Bitmap
import com.gogoh5.apps.quanmaomao.library.environment.SysContext
import com.gogoh5.apps.quanmaomao.library.environment.constants.Constants
import com.gogoh5.apps.quanmaomao.library.toolkits.HunterManager
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import java.lang.Exception
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage
import com.tencent.mm.opensdk.modelmsg.WXImageObject
import java.io.ByteArrayOutputStream

class WeChatModule: IWXAPIEventHandler {
    companion object {
        const val TYPE_AUTH = 0
        internal val instance: WeChatModule by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { WeChatModule() }
    }

    val api: IWXAPI
    val hunterManager: HunterManager

    init {
        api = WXAPIFactory.createWXAPI(SysContext.getApp(), Constants.WX_APP_ID, false)
        hunterManager = HunterManager()
    }

    fun handleIntent(intent: Intent?) {
        api.handleIntent(intent, this)
    }

    fun isInstallWx(): Boolean = api.isWXAppInstalled

    fun sendAuth(): Boolean {
        try {
            val req = SendAuth.Req()
            req.scope = "snsapi_userinfo"
            api.sendReq(req)
            return true
        }
        catch (e : Exception) {
            e.printStackTrace()
        }
        return false
    }

    fun sendImage(isFriend: Boolean, bitmap: Bitmap): Boolean {
        try {
            val imgObj = WXImageObject(bitmap)
            val msg = WXMediaMessage()
            msg.mediaObject = imgObj

            val thumbBmp = Bitmap.createScaledBitmap(bitmap, 120, 120, true)
//            bitmap.recycle()

            val stream = ByteArrayOutputStream()
            thumbBmp.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            val byteArray = stream.toByteArray()
            thumbBmp.recycle()
            msg.thumbData = byteArray

            val req = SendMessageToWX.Req()
            req.message = msg
            if(isFriend)
                req.scene = SendMessageToWX.Req.WXSceneSession
            else req.scene = SendMessageToWX.Req.WXSceneTimeline
            api.sendReq(req)

        }
        catch (e: Exception) {

        }
        return true
    }

    override fun onReq(p0: BaseReq?) {
    }

    override fun onResp(resp: BaseResp?) {
        if(resp == null)
            return

        when (resp) {
            is SendAuth.Resp -> {
                if(resp.errCode == BaseResp.ErrCode.ERR_OK)
                    hunterManager.doEat(TYPE_AUTH, true, resp.code)
                else hunterManager.doEat(TYPE_AUTH, false)
            }
        }

    }

}