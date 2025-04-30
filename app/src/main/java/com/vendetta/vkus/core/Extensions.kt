package com.vendetta.vkus.core

import android.graphics.BitmapFactory
import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

fun ByteArray.toImageBitmap(): ImageBitmap {
    return BitmapFactory.decodeByteArray(this, 0, this.size).asImageBitmap()
}

fun ComponentContext.componentScope(): CoroutineScope = CoroutineScope(
    Dispatchers.Main.immediate + SupervisorJob()
).apply {
    lifecycle.doOnDestroy { cancel() }
}

val Icons.Filled.RoundedHome: ImageVector
    get() {
        return Builder(
            name = "Home",
            defaultWidth = 26.0.dp,
            defaultHeight = 26.0.dp,
            viewportWidth = 26.0f,
            viewportHeight = 26.0f
        ).apply {
            group {
                path(
                    fill = SolidColor(Color(0x00000000)),
                    stroke = SolidColor(Color(0xFF171C26)),
                    strokeLineWidth = 1.5f,
                    strokeLineCap = Butt,
                    strokeLineJoin = Miter,
                    strokeLineMiter = 4.0f,
                    pathFillType = NonZero
                ) {
                    moveTo(9.9f, 19.19f)
                    curveTo(9.9f, 18.34f, 10.59f, 17.66f, 11.43f, 17.66f)
                    lineTo(14.55f, 17.66f)
                    curveTo(15.4f, 17.66f, 16.09f, 18.34f, 16.09f, 19.19f)
                    lineTo(16.09f, 22.5f)
                    curveTo(16.09f, 23.23f, 16.68f, 23.82f, 17.42f, 23.83f)
                    lineTo(19.54f, 23.83f)
                    curveTo(20.53f, 23.83f, 21.49f, 23.44f, 22.19f, 22.75f)
                    curveTo(22.89f, 22.05f, 23.29f, 21.11f, 23.29f, 20.12f)
                    lineTo(23.29f, 10.68f)
                    curveTo(23.29f, 9.89f, 22.93f, 9.13f, 22.32f, 8.62f)
                    lineTo(15.1f, 2.89f)
                    curveTo(13.84f, 1.89f, 12.04f, 1.92f, 10.81f, 2.97f)
                    lineTo(3.75f, 8.62f)
                    curveTo(3.11f, 9.12f, 2.72f, 9.87f, 2.7f, 10.68f)
                    lineTo(2.7f, 20.11f)
                    curveTo(2.7f, 22.16f, 4.38f, 23.83f, 6.45f, 23.83f)
                    lineTo(8.52f, 23.83f)
                    curveTo(8.88f, 23.83f, 9.22f, 23.69f, 9.47f, 23.45f)
                    curveTo(9.72f, 23.2f, 9.9f, 22.86f, 9.9f, 22.51f)
                    lineTo(9.9f, 19.19f)
                    close()
                }
            }
        }.build()
    }
