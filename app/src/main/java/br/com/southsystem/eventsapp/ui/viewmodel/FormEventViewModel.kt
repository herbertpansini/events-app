package br.com.southsystem.eventsapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import br.com.southsystem.eventsapp.remotedatasource.dto.CheckInDto
import br.com.southsystem.eventsapp.repository.EventRepository
import br.com.southsystem.eventsapp.repository.Resource

class FormEventViewModel(private val eventRepository: EventRepository) : ViewModel() {
    fun checkIn(checkInDto: CheckInDto): LiveData<Resource<Void?>> {
        return eventRepository.doCheckIn(checkInDto)
    }
}