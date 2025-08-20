package br.com.encontreinashopee.retrofit

import br.com.encontreinashopee.util.UrlJson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClient {

    val baseUrl = UrlJson.URL

    private fun getRetrofit(): Retrofit {
        val gson = GsonBuilder()
            .setLenient()
            .create()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    internal inline fun <reified T> createService(): T {
        return getRetrofit().create(T::class.java)
    }
}