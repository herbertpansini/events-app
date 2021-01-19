package br.com.southsystem.eventsapp.remotedatasource.service

import br.com.southsystem.eventsapp.model.entity.Event
import br.com.southsystem.eventsapp.remotedatasource.dto.CheckInDto
import retrofit2.Call
import retrofit2.http.*

interface EventService {
    @GET("events")
    fun buscaTodas(): Call<List<Event>>

    @GET("events/{id}")
    fun buscaPorId(@Path("id") id: String): Call<Event>

    @POST("checkin")
    fun checkIn(@Body checkInDto: CheckInDto): Call<Void>
}