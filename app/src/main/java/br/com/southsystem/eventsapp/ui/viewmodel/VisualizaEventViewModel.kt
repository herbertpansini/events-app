package br.com.southsystem.eventsapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import br.com.southsystem.eventsapp.repository.EventRepository

class VisualizaEventViewModel(private val id: String,
                              private val eventRepository: EventRepository) : ViewModel() {
    val eventEncontrado = eventRepository.buscaPorId(id)
}