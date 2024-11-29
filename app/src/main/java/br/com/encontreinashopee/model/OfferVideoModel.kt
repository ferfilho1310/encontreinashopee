package br.com.encontreinashopee.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OfferVideoModel(
    var id: String? = null,
    var procutName: String? = null,
    var productLinkVideo: String? = null,
    var productLinkAffiliate: String? = null
) : Parcelable
