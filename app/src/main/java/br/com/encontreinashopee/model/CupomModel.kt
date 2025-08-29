package br.com.encontreinashopee.model

import com.google.gson.annotations.SerializedName

data class CupomModel(
    @SerializedName("id")
    var id: Int,
    @SerializedName("title")
    var title: String,
    @SerializedName("cupom")
    var cupom: String,
    @SerializedName("imageCupom")
    var imageCupom: String,
    @SerializedName("linkCupom")
    var linkCupom: String?
)
