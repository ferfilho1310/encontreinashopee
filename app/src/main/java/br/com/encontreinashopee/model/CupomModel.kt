package br.com.encontreinashopee.model

import com.google.gson.annotations.SerializedName

data class CupomModel(
    @SerializedName("title")
    var title: String,
    @SerializedName("cupom")
    var cupom: String
)
