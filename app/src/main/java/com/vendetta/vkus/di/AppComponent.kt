package com.vendetta.vkus.di

import android.app.Application
import com.vendetta.vkus.presentation.root.DefaultRootComponent
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(modules = [DataModule::class, PresentationModule::class])
interface AppComponent {

    fun getRootComponentFactory(): DefaultRootComponent.Factory

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance application: Application
        ): AppComponent
    }
}