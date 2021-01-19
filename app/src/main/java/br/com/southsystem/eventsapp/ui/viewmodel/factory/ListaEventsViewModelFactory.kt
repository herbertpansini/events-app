package br.com.southsystem.eventsapp.ui.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.southsystem.eventsapp.repository.EventRepository
import br.com.southsystem.eventsapp.ui.viewmodel.ListaEventsViewModel

class ListaEventsViewModelFactory(private val eventRepository: EventRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ListaEventsViewModel(eventRepository) as T
    }
}