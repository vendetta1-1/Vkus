package com.vendetta.vkus

import android.app.Application
import com.vendetta.vkus.di.AppComponent
import com.vendetta.vkus.di.DaggerAppComponent

class App : Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(this)
    }
}

fun getApplicationComponent(application: Application): AppComponent {
    return (application as App).appComponent
}