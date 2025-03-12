package com.vendetta.vkus

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import com.vendetta.data.service.MusicPlayerService.Companion.CHANNEL_ID
import com.vendetta.data.service.MusicPlayerService.Companion.CHANNEL_NAME
import com.vendetta.vkus.di.appModule
import com.vendetta.vkus.di.domainModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(level = Level.DEBUG)
            androidContext(this@App)
            modules(
                appModule,
                domainModule
            )
        }
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }


}