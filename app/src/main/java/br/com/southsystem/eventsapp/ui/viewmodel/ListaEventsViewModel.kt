package br.com.southsystem.eventsapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import br.com.southsystem.eventsapp.model.entity.Event
import br.com.southsystem.eventsapp.repository.EventRepository
import br.com.southsystem.eventsapp.repository.Resource

class ListaEventsViewModel(private val eventRepository: EventRepository) : ViewModel() {
    fun buscaTodos() : LiveData<Resource<List<Event>?>> {
        return eventRepository.buscaTodos()
    }
}