package br.com.southsystem.eventsapp.repository

class Resource<T>(
    val dado: T?,
    val erro: String? = null
)