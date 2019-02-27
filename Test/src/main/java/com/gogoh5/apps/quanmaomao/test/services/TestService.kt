package com.gogoh5.apps.quanmaomao.test.services

import android.app.Service
import android.content.Intent
import android.os.IBinder

class TestService: Service() {
    override fun onBind(intent: Intent?): IBinder? = null
}