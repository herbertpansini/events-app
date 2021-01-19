package br.com.southsystem.eventsapp.remotedatasource.dto

data class CheckInDto(
    val eventId: String? = null,
    val name: String = "",
    val email: String = ""
)