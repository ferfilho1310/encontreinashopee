package br.com.encontreinashopee.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClient {

    private fun getRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    internal inline fun <reified T> createService(baseUrl: String): T {
        return getRetrofit(baseUrl).create(T::class.java)
    }
}