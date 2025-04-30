package com.vendetta.vkus.presentation.root

import com.arkivanov.decompose.router.pages.ChildPages
import com.arkivanov.decompose.value.Value
import com.vendetta.vkus.presentation.favourite.FavouriteComponent
import com.vendetta.vkus.presentation.home.HomeComponent

interface RootComponent {

    val pages: Value<ChildPages<*, Page>>

    fun selectPage(index: Int)

    sealed interface Page {
        data class Favourite(val component: FavouriteComponent) : Page
        data class Home(val component: HomeComponent) : Page
    }
}