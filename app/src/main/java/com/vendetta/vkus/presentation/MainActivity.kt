package com.vendetta.vkus.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.arkivanov.decompose.defaultComponentContext
import com.vendetta.vkus.getApplicationComponent
import com.vendetta.vkus.presentation.root.RootContent
import com.vendetta.vkus.presentation.ui.theme.VkusTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val appComponent = getApplicationComponent(application)
            val rootComponentFactory = appComponent.getRootComponentFactory()
            VkusTheme {
                RootContent(component = rootComponentFactory.create(defaultComponentContext()))
            }
        }
    }
}


