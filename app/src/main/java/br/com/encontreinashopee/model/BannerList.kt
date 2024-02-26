package br.com.encontreinashopee.model

import androidx.compose.ui.graphics.painter.Painter

data class BannerList(
    val img: Painter,
    val url: String? = null
)