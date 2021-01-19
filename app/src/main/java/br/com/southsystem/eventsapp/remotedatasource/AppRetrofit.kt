package br.com.southsystem.eventsapp.remotedatasource

import br.com.southsystem.eventsapp.remotedatasource.service.EventService
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "http://5b840ba5db24a100142dcd8c.mockapi.io/api/"

class AppRetrofit {
    private val client by lazy {
        val interceptador = HttpLoggingInterceptor()
        interceptador.level = HttpLoggingInterceptor.Level.BODY
        OkHttpClient.Builder()
            .addInterceptor(interceptador)
            .build()
    }

    private val retrofit by lazy {
        val gson = GsonBuilder()
            //.setDateFormat("yyyy-MM-dd HH:mm:ss")
            .serializeNulls()
            .create()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
    }

    val eventService: EventService by lazy {
        retrofit.create(EventService::class.java)
    }
}